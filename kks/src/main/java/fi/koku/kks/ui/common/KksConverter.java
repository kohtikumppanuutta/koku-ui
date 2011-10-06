package fi.koku.kks.ui.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import fi.koku.kks.model.CollectionState;
import fi.koku.kks.model.Entry;
import fi.koku.kks.model.EntryValue;
import fi.koku.kks.model.KKSCollection;
import fi.koku.kks.model.KksService;
import fi.koku.services.entity.kks.v1.EntryValuesType;
import fi.koku.services.entity.kks.v1.KksCollectionType;
import fi.koku.services.entity.kks.v1.KksEntriesType;
import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksEntryType;
import fi.koku.services.entity.kks.v1.KksEntryValueType;

/**
 * Class for converting ui classes to ws types and vice versa
 * 
 * @author Ixonos / tuomape
 * 
 */
public class KksConverter {

  private KksService kksService;

  public KksConverter(KksService kksService) {
    this.kksService = kksService;
  }

  public KKSCollection fromWsType(KksCollectionType collection, boolean generateEmptyEntries, String user) {

    KKSCollection tmp = new KKSCollection(collection.getId(), collection.getName(), collection.getDescription(),
        fromWsType(collection.getStatus()), collection.getCreated().getTime(), collection.getVersion().intValue(),
        kksService.getCollectionClassType(collection.getCollectionClassId(), user));

    tmp.setModifier(collection.getCreator());
    tmp.setPrevVersion(collection.getPrevVersion());
    tmp.setNextVersion(collection.getNextVersion());
    tmp.setBuildFromExisting(collection.getNextVersion() != null);
    tmp.setVersioned(collection.getNextVersion() != null);
    tmp.setCreator(collection.getCreator());
    for (KksEntryType entryType : collection.getKksEntries().getEntries()) {

      KksEntryClassType metaEntry = kksService.getEntryClassType(entryType.getEntryClassId(), user);

      if (metaEntry != null) {
        Entry e = fromWsType(entryType, metaEntry);
        tmp.addEntry(e);
      }

    }
    if (generateEmptyEntries) {
      tmp.generateEmptyEntries(user);
    }
    return tmp;
  }

  public KksCollectionType toWsType(KKSCollection collection, String customer) {
    return toWsType(collection, customer, false, false);
  }

  public KksCollectionType toWsType(KKSCollection collection, String customer, boolean createVersion, boolean empty) {
    KksCollectionType tmp = new KksCollectionType();
    tmp.setId(collection.getId());
    tmp.setName(collection.getName());
    tmp.setDescription(collection.getDescription());
    tmp.setCollectionClassId(collection.getCollectionClass().getId());
    tmp.setCustomerId(customer);
    tmp.setCreator(collection.getCreator());
    tmp.setPrevVersion(collection.getPrevVersion());
    tmp.setNextVersion(collection.getNextVersion());
    tmp.setStatus(collection.getState().getState().toString());

    KksEntriesType entriesType = new KksEntriesType();
    Calendar cal = new GregorianCalendar();
    cal.setTime(new Date());

    if (!empty) {
      for (Entry entry : collection.getEntries().values()) {
        KksEntryType kksEntryType = new KksEntryType();
        kksEntryType.setId(entry.getId());
        kksEntryType.setCreator(entry.getRecorder());
        kksEntryType.setCustomerId(customer);
        kksEntryType.setEntryClassId(entry.getType().getId());
        kksEntryType.setModified(cal);

        EntryValuesType values = new EntryValuesType();
        for (EntryValue ev : entry.getEntryValues()) {
          KksEntryValueType value = new KksEntryValueType();
          value.setId(ev.getId());
          value.setValue(ev.getValue());
          value.setModifier(ev.getModifier());

          if (ev.getModified() == null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(ev.getModified());
            value.setModified(cal);
          }
          values.getEntryValue().add(value);

        }

        kksEntryType.setEntryValues(values);
        entriesType.getEntries().add(kksEntryType);
      }
    }

    Calendar c = new GregorianCalendar();
    c.setTime(collection.getCreationTime());
    tmp.setCreated(c);
    tmp.setKksEntries(entriesType);
    tmp.setNewVersion(createVersion);
    tmp.setVersion(new BigInteger("" + collection.getVersion()));
    tmp.setVersioned(collection.getNextVersion() != null);
    return tmp;
  }

  public CollectionState fromWsType(String status) {
    if (State.ACTIVE.toString().equals(status)) {
      return new CollectionState(State.ACTIVE);
    }
    return new CollectionState(State.LOCKED);
  }

  public Entry fromWsType(KksEntryType entry, KksEntryClassType metaEntry) {
    List<KksEntryValueType> values = entry.getEntryValues().getEntryValue();
    return fromWsType(entry, values, metaEntry);
  }

  public Entry fromWsType(KksEntryType entry, KksEntryValueType val, KksEntryClassType metaEntry) {
    String valId = val == null ? "" : val.getId();
    String value = val == null ? "" : val.getValue();

    Entry e = new Entry(entry.getId(), entry.getModified().getTime(), entry.getVersion().toString(),
        entry.getCreator(), metaEntry);

    EntryValue entryValue = new EntryValue();
    entryValue.setId(valId);
    entryValue.setValue(value);
    entryValue.setModified(val.getModified().getTime());
    entryValue.setModifier(val.getModifier());

    if (metaEntry.getDataType().equals(DataType.MULTI_SELECT.toString())) {
      String tmp[] = value.split(",");
      List<String> values = new ArrayList<String>();

      for (String s : tmp) {
        values.add(s);
      }
      entryValue.setValues(values);
    }

    e.addEntryValue(entryValue);

    return e;
  }

  public Entry fromWsType(KksEntryType entry, List<KksEntryValueType> values, KksEntryClassType metaEntry) {

    Entry e = new Entry(entry.getId(), entry.getModified().getTime(), entry.getVersion().toString(),
        entry.getCreator(), metaEntry);

    for (KksEntryValueType kev : values) {
      EntryValue entryValue = new EntryValue();
      entryValue.setId(kev.getId());
      entryValue.setValue(kev.getValue());

      if (kev.getModified() == null) {
        entryValue.setModified(new Date());
      } else {
        entryValue.setModified(kev.getModified().getTime());
      }
      entryValue.setModifier(kev.getModifier());
      e.addEntryValue(entryValue);
    }
    return e;
  }

}
