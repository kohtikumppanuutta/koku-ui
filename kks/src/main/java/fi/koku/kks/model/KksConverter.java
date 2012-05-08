/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.kks.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import fi.koku.calendar.CalendarUtil;
import fi.koku.kks.ui.common.DataType;
import fi.koku.kks.ui.common.State;
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
public final class KksConverter {

  private KksService kksService;

  public KksConverter(KksService kksService) {
    this.kksService = kksService;
  }

  public KKSCollection fromWsType(KksCollectionType collection, String user) {

    KKSCollection tmp = new KKSCollection(collection.getId(), collection.getName(), collection.getDescription(),
        fromWsType(collection.getStatus()), CalendarUtil.getDate(collection.getCreated()), collection.getVersion().intValue(),
        kksService.getCollectionClassType(collection.getCollectionClassId(), user));

    tmp.setModifier(collection.getCreator());
    tmp.setPrevVersion(collection.getPrevVersion());
    tmp.setNextVersion(collection.getNextVersion());
    tmp.setBuildFromExisting(collection.getNextVersion() != null);
    tmp.setVersioned(collection.getNextVersion() != null);
    tmp.setCreator(collection.getCreator());
    tmp.setCustomer(collection.getCustomerId());
    tmp.setConsentRequested(collection.isConsentRequested());
    tmp.setUserConsentStatus(collection.getUserConsentStatus());
    for (KksEntryType entryType : collection.getKksEntries().getEntries()) {

      KksEntryClassType metaEntry = kksService.getEntryClassType(entryType.getEntryClassId(), user);

      if (metaEntry != null) {
        Entry e = fromWsType(entryType, metaEntry);
        tmp.addEntry(e);
      }

    }

    return tmp;
  }

  public KksCollectionType toWsType(KKSCollection collection, String customer, String user) {
    return toWsType(collection, customer, false, false, user);
  }

  public KksCollectionType toWsType(KKSCollection collection, String customer, boolean createVersion, boolean empty,
      String user) {
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
    tmp.setModifier(user);
    tmp.setConsentRequested(collection.isConsentRequested());
    tmp.setUserConsentStatus(collection.getUserConsentStatus());

    Calendar cal = new GregorianCalendar();
    cal.setTime(new Date());

    tmp.setModified(CalendarUtil.getXmlDateTime(cal.getTime()));

    KksEntriesType entriesType = toEntriesWsType(collection, customer, empty, user, cal);

    Calendar c = new GregorianCalendar();
    c.setTime(collection.getCreationTime());
    tmp.setCreated(CalendarUtil.getXmlDateTime(c.getTime()));
    tmp.setKksEntries(entriesType);
    tmp.setNewVersion(createVersion);
    tmp.setVersion(new BigInteger("" + collection.getVersion()));
    tmp.setVersioned(collection.getNextVersion() != null);
    return tmp;
  }

  private KksEntriesType toEntriesWsType(KKSCollection collection, String customer, boolean empty, String user,
      Calendar cal) {
    KksEntriesType entriesType = new KksEntriesType();

    if (!empty) {
      for (Entry entry : collection.getEntries().values()) {
        KksEntryType kksEntryType = new KksEntryType();
        kksEntryType.setId(entry.getId());
        kksEntryType.setCreator(entry.getRecorder());
        kksEntryType.setCustomerId(customer);
        kksEntryType.setEntryClassId(entry.getType().getId());
        kksEntryType.setModified(CalendarUtil.getXmlDateTime(cal.getTime()));
        kksEntryType.setVersion(new BigInteger(entry.getVersion()));

        EntryValuesType values = toValuesWsType(user, cal, entry);

        kksEntryType.setEntryValues(values);

        KksTagIdsType ids = new KksTagIdsType();

        for (KksTagType tag : entry.getType().getKksTags().getKksTag()) {
          ids.getKksTagId().add(tag.getId());
        }

        kksEntryType.setKksTagIds(ids);

        entriesType.getEntries().add(kksEntryType);
      }
    }
    return entriesType;
  }

  private EntryValuesType toValuesWsType(String user, Calendar cal, Entry entry) {
    EntryValuesType values = new EntryValuesType();
    for (EntryValue ev : entry.getEntryValues()) {
      KksEntryValueType value = new KksEntryValueType();
      value.setId(ev.getId());

      if (DataType.MULTI_SELECT.toString().equals(entry.getType().getDataType())) {
        value.setValue(ev.getValuesAsText());
      } else {
        value.setValue(ev.getValue());
      }

      if (!entry.getType().isMultiValue()) {
        // multivalue updates have own modifier handling
        value.setModified(CalendarUtil.getXmlDateTime(cal.getTime()));
        value.setModifier(user);
      }
      values.getEntryValue().add(value);
    }
    return values;
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

    Entry e = new Entry(entry.getId(), CalendarUtil.getDate(entry.getModified()), entry.getVersion().toString(),
        entry.getCreator(), metaEntry);

    EntryValue entryValue = new EntryValue();
    entryValue.setId(valId);
    entryValue.setValue(value);
    entryValue.setModified(CalendarUtil.getDate(val.getModified()));
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

    Entry e = new Entry(entry.getId(), CalendarUtil.getDate(entry.getModified()), entry.getVersion().toString(),
        entry.getCreator(), metaEntry);

    for (KksEntryValueType kev : values) {
      EntryValue entryValue = new EntryValue();
      entryValue.setId(kev.getId());
      entryValue.setValue(kev.getValue());

      if (metaEntry.getDataType().equals(DataType.MULTI_SELECT.toString())) {
        String tmp[] = kev.getValue().split(",");
        List<String> valueList = new ArrayList<String>();

        for (String s : tmp) {
          valueList.add(s.trim());
        }
        entryValue.setValues(valueList);
      }

      if (kev.getModified() == null) {
        entryValue.setModified(new Date());
      } else {
        entryValue.setModified(CalendarUtil.getDate(kev.getModified()));
      }
      entryValue.setModifier(kev.getModifier());
      e.addEntryValue(entryValue);
    }
    return e;
  }

}
