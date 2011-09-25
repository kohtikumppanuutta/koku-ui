package fi.koku.kks.ui.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import fi.koku.kks.model.CollectionState;
import fi.koku.kks.model.Entry;
import fi.koku.kks.model.KKSCollection;
import fi.koku.kks.model.KksService;
import fi.koku.services.entity.kks.v1.EntryValuesType;
import fi.koku.services.entity.kks.v1.KksCollectionType;
import fi.koku.services.entity.kks.v1.KksEntriesType;
import fi.koku.services.entity.kks.v1.KksEntryClassType;
import fi.koku.services.entity.kks.v1.KksEntryType;
import fi.koku.services.entity.kks.v1.KksEntryValueType;
import fi.koku.services.entity.kks.v1.KksTagIdsType;
import fi.koku.services.entity.kks.v1.KksTagType;

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
        if (metaEntry.isMultiValue()) {
          // multivalue is handled as a separate entries in UI side
          for (KksEntryValueType val : entryType.getEntryValues().getEntryValue()) {
            Entry e = fromWsType(entryType, val, metaEntry);
            tmp.addMultivalue(e);
          }

        } else {
          Entry e = fromWsType(entryType, metaEntry);
          tmp.addEntry(e);
        }
      }

    }
    if (generateEmptyEntries) {
      tmp.generateEmptyEntries();
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

    if (!empty) {
      for (Entry entry : collection.getEntries().values()) {
        entriesType.getEntries().add(toWsType(entry, customer));
      }

      for (List<Entry> entries : collection.getMultiValueEntries().values()) {
        List<Entry> tmpList = new ArrayList<Entry>();

        for (Entry entry : entries) {
          tmpList.add(entry);
        }
        entriesType.getEntries().add(toWsType(entries.get(0), customer, tmpList.toArray(new Entry[0])));
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

    Entry e = new Entry(entry.getId(), valId, entry.getModified().getTime(), entry.getVersion().toString(),
        entry.getCreator(), metaEntry);

    if (metaEntry.getDataType().equals(DataType.MULTI_SELECT.toString())) {
      String tmp[] = value.split(",");
      List<String> values = new ArrayList<String>();

      for (String s : tmp) {
        values.add(s);
      }
      e.setValues(values);

    } else {
      e.setValue(value);
    }

    return e;
  }

  public Entry fromWsType(KksEntryType entry, List<KksEntryValueType> values, KksEntryClassType metaEntry) {

    Entry e = null;
    if (values.size() > 1) {
      e = new Entry(entry.getId(), null, "", entry.getModified().getTime(), entry.getVersion().toString(),
          entry.getCreator(), metaEntry);
      List<String> tmp = new ArrayList<String>();
      for (KksEntryValueType kev : values) {
        tmp.add(kev.getValue());
      }
      e.setValues(tmp);

    } else if (values.size() > 0) {
      e = fromWsType(entry, values.get(0), metaEntry);
    }
    return e;
  }

  public KksEntryType toWsType(Entry entry, String customer) {
    return toWsType(entry, customer, entry);
  }

  public KksEntryType toWsType(Entry entry, String customer, Entry... values) {
    if (values == null) {
      return null;
    }

    KksEntryType tmp = new KksEntryType();
    tmp.setId(entry.getId());
    tmp.setCreator(entry.getRecorder());
    tmp.setCustomerId(customer);
    tmp.setEntryClassId(entry.getType().getId());

    List<Entry> vals = Arrays.asList(values);
    EntryValuesType evt = new EntryValuesType();
    for (Entry v : vals) {

      if (v.getType().getDataType().equals(DataType.MULTI_SELECT.toString())) {

        if (v.getValues() != null) {
          KksEntryValueType kevt = new KksEntryValueType();
          kevt.setId(v.getValueId());
          StringBuilder b = new StringBuilder();

          b.append("");

          for (int i = 0; i < v.getValues().size(); i++) {
            b.append(v.getValues().get(i));
            if ((i + 1) < v.getValues().size()) {
              b.append(",");
            }
          }
          kevt.setValue(b.toString());
          evt.getEntryValue().add(kevt);
        }

      } else {
        for (String value : v.getValues()) {
          KksEntryValueType kevt = new KksEntryValueType();
          kevt.setId(v.getValueId());
          kevt.setValue(value);
          evt.getEntryValue().add(kevt);
        }
      }
    }
    tmp.setEntryValues(evt);

    KksTagIdsType ids = new KksTagIdsType();

    for (KksTagType tag : entry.getType().getKksTags().getKksTag()) {
      ids.getKksTagId().add(tag.getId());
    }

    tmp.setKksTagIds(ids);
    Calendar c = new GregorianCalendar();
    c.setTime(entry.getCreationTime());
    tmp.setModified(c);
    tmp.setVersion(new BigInteger(entry.getVersion()));
    return tmp;
  }
}
