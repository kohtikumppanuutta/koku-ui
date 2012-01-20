package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_FAILURE_UUID;
import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_RESULT;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;
import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.JSON_TASKS;
import static fi.arcusys.koku.util.Constants.JSON_TOTAL_ITEMS;
import static fi.arcusys.koku.util.Constants.JSON_TOTAL_PAGES;
import static fi.arcusys.koku.util.Constants.PAGE_NUMBER;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_INBOX_CITIZEN;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_ASSIGNED_CITIZEN;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_REQUEST_OLD;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_REQUEST_REPLIED;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_WARRANT_BROWSE_RECEIEVED;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_WARRANT_BROWSE_SENT;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_VALID;

import java.util.Collections;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.request.citizen.CitizenRequestHandle;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.warrant.citizens.KokuCitizenWarrantHandle;

public class QueryProcessCitizenImpl extends AbstractQueryProcess {

	private static final Logger LOG = Logger.getLogger(QueryProcessCitizenImpl.class);
	
	public QueryProcessCitizenImpl(MessageSource messages) {
		super(messages);
	}
		
	@Override
	protected void setJsonTasks(JSONObject jsonModel, String taskType, int page,
			String keyword, String field, String orderType, String userUid) {
		
		int numPerPage = PAGE_NUMBER;
		int totalTasksNum = 0;
		int totalPages;
		int first = (page-1)*numPerPage + 1; // the start index of task
		int max =  page*numPerPage; // max amount of tasks
		Object tasks = Collections.EMPTY_LIST; // Returned tasks
		
		try {
			if (taskType.equals(TASK_TYPE_REQUEST_REPLIED)) { // Pyynnöt - vastatut
				CitizenRequestHandle handle = new CitizenRequestHandle();
				tasks = handle.getRepliedRequests(userUid, first, max);
				totalTasksNum = handle.getTotalRepliedRequests(userUid);
			}  else if (taskType.equals(TASK_TYPE_REQUEST_OLD)) { // Pyynnöt - vanhat
				CitizenRequestHandle handle = new CitizenRequestHandle();
				tasks = handle.getOldRequests(userUid, first, max);
				totalTasksNum = handle.getTotalOldRequests(userUid);
			} else if (taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_CITIZEN)
					|| taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN_OLD)
					|| taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_CITIZEN)) {	// Tapaamiset - Vastausta odottavat / vastatut 
				AvCitizenServiceHandle handle = new AvCitizenServiceHandle(userUid);
				handle.setMessageSource(getMessageSource());
				tasks = handle.getAppointments(userUid, first, max, taskType);
				totalTasksNum = handle.getTotalAppointmentsNum(userUid, taskType);
			} else if (taskType.equals(TASK_TYPE_CONSENT_ASSIGNED_CITIZEN)) { // consent (Valtakirja / Suostumus) Kansalaiselle saapuneet pyynnöt(/suostumukset) 
				TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
				tivaHandle.setMessageSource(getMessageSource());
				tasks = tivaHandle.getAssignedConsents(userUid, first, max);
				totalTasksNum = tivaHandle.getTotalAssignedConsents(userUid);
			} else if(taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS)) { // consent (Valtakirja / Suostumus) Kansalaiselle vastatut pyynnöt(/suostumukset) 
				TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
				tivaHandle.setMessageSource(getMessageSource());
				tasks = tivaHandle.getOwnConsents(userUid, first, max);
				totalTasksNum = tivaHandle.getTotalOwnConsents(userUid);
			} else if(taskType.equals(TASK_TYPE_CONSENT_CITIZEN_CONSENTS_OLD)) { // consent (Valtakirja / Suostumus) Kansalaiselle vastatut vanhentuneet pyynnöt(/suostumukset) 
				TivaCitizenServiceHandle tivaHandle = new TivaCitizenServiceHandle();
				tivaHandle.setMessageSource(getMessageSource());
				tasks = tivaHandle.getOwnOldConsents(userUid, first, max);
				totalTasksNum = tivaHandle.getTotalOwnOldConsents(userUid);
			} else if(taskType.equals(TASK_TYPE_WARRANT_BROWSE_RECEIEVED)) {	// Kuntalainen: Valtuuttajana TIVA-11
				KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();
				warrantHandle.setMessageSource(getMessageSource());
				tasks = warrantHandle.getReceivedAuthorizations(userUid, first, max);
				totalTasksNum = warrantHandle.getTotalReceivedAuthorizations(userUid);
			} else if(taskType.equals(TASK_TYPE_WARRANT_BROWSE_SENT)) {	// Kuntalainen: Valtuutettuna TIVA-11
				KokuCitizenWarrantHandle warrantHandle = new KokuCitizenWarrantHandle();
				warrantHandle.setMessageSource(getMessageSource());
				tasks = warrantHandle.getSentWarrants(userUid, first, max);
				totalTasksNum = warrantHandle.getTotalSentAuthorizations(userUid);
			} else { // for message
				MessageHandle msgHandle = new MessageHandle();
				msgHandle.setMessageSource(getMessageSource());
				tasks = msgHandle.getMessages(userUid, taskType, keyword, field, orderType, first, max);			
				totalTasksNum = msgHandle.getTotalMessageNum(userUid, taskType, keyword, field);
			}
			jsonModel.put(JSON_RESULT, RESPONSE_OK);
		} catch (KokuServiceException kse) {
			LOG.error("Koku WS query failed", kse);
			jsonModel.put(JSON_RESULT, RESPONSE_FAIL);
			jsonModel.put(JSON_FAILURE_UUID, kse.getUuid());
		}
		
		totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);
		jsonModel.put(JSON_TASKS, tasks);
		jsonModel.put(JSON_TOTAL_ITEMS, totalTasksNum);
		jsonModel.put(JSON_TOTAL_PAGES, totalPages);
		jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_VALID);
	}

}
