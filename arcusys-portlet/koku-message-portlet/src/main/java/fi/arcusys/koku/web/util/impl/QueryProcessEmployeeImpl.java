package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_TASKS;
import static fi.arcusys.koku.util.Constants.JSON_TOTAL_ITEMS;
import static fi.arcusys.koku.util.Constants.JSON_TOTAL_PAGES;
import static fi.arcusys.koku.util.Constants.PAGE_NUMBER;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_INFO_REQUEST_BROWSE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_INFO_REQUEST_BROWSE_REPLIED;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_INFO_REQUEST_BROWSE_SENT;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_REQUEST_DONE_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_REQUEST_OLD;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_REQUEST_REPLIED;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_REQUEST_VALID_EMPLOYEE;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS;
import static fi.arcusys.koku.util.Constants.TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_VALID;

import java.util.Collections;

import net.sf.json.JSONObject;

import org.springframework.context.MessageSource;

import fi.arcusys.koku.av.AvEmployeeServiceHandle;
import fi.arcusys.koku.hak.model.HakServiceHandle;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.request.employee.EmployeeRequestHandle;
import fi.arcusys.koku.tiva.TivaEmployeeServiceHandle;
import fi.arcusys.koku.tiva.tietopyynto.employee.KokuEmployeeTietopyyntoServiceHandle;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantHandle;

public class QueryProcessEmployeeImpl extends AbstractQueryProcess {

	public QueryProcessEmployeeImpl(MessageSource messages) {
		super(messages);
	}

	@Override
	protected void setJsonTasks(JSONObject jsonModel, String taskType,
			int page, String keyword, String field, String orderType,
			String userUid) {
		
		int numPerPage = PAGE_NUMBER;
		int totalTasksNum = 0;
		int totalPages;
		int first = (page-1)*numPerPage + 1; // the start index of task
		int max =  page*numPerPage; // max amount of tasks
		Object tasks = Collections.EMPTY_LIST; // Returned tasks
		
		if (taskType.equals(TASK_TYPE_APPLICATION_KINDERGARTEN_BROWSE)) { // Asiointipalvelut - Selaa hakemuksia (päivähoito) - employee
			HakServiceHandle reqHandle = new HakServiceHandle();
			tasks = reqHandle.getApplicants(userUid, keyword, first, max);
			totalTasksNum = reqHandle.getTotalRequestedApplicants(userUid, keyword);
		} else if (taskType.equals(TASK_TYPE_REQUEST_VALID_EMPLOYEE)) { // for request (Pyynnöt) - employee Avoimet
			EmployeeRequestHandle reqHandle = new EmployeeRequestHandle();
			tasks = reqHandle.getRequests(userUid, "valid", "", first, max);
			totalTasksNum = reqHandle.getTotalRequestsNum(userUid, "valid");
		} else if (taskType.equals(TASK_TYPE_REQUEST_REPLIED)) { // Pyynnöt - vastatut
			EmployeeRequestHandle handle = new EmployeeRequestHandle();
			tasks = handle.getRepliedResponseSummaries(userUid, first, max);
			totalTasksNum = handle.getTotalResponsesRepliedNum(userUid);
		} else if (taskType.equals(TASK_TYPE_REQUEST_OLD)) { // Pyynnöt - vanhat
			EmployeeRequestHandle handle = new EmployeeRequestHandle();
			tasks = handle.getOldResponseSummaries(userUid, first, max);
			totalTasksNum = handle.getTotalResponsesOldNum(userUid);
		} else if (taskType.equals(TASK_TYPE_REQUEST_DONE_EMPLOYEE)) { // Pyynnöt - vastatut/vanhat
			// TODO: Need proper WS service
			tasks = Collections.EMPTY_LIST;
			totalTasksNum = 0;
		} else if (taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE_REPLIED)) { // Virkailija: Selaa vastaanotettuja tietopyyntöjä
			KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
			handle.setMessageSource(getMessageSource());
			tasks = handle.getRepliedRequests(userUid, keyword, first, max);
			totalTasksNum = handle.getTotalRepliedRequests(userUid, keyword);
		} else if (taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE_SENT)) { // Virkailija: Selaa lähetettyjä tietopyyntöjä
			KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
			handle.setMessageSource(getMessageSource());
			tasks = handle.getSentRequests(userUid, keyword, first, max);
			totalTasksNum = handle.getTotalSentRequests(userUid, keyword);
		} else if (taskType.equals(TASK_TYPE_INFO_REQUEST_BROWSE)) { // ADMIN: Selaa tietopyyntöjä
			KokuEmployeeTietopyyntoServiceHandle handle = new KokuEmployeeTietopyyntoServiceHandle();
			handle.setMessageSource(getMessageSource());
			tasks = handle.getRequests(keyword, first, max);
			totalTasksNum = handle.getTotalRequests(keyword);
		} else if(taskType.equals(TASK_TYPE_APPOINTMENT_INBOX_EMPLOYEE) || taskType.equals(TASK_TYPE_APPOINTMENT_RESPONSE_EMPLOYEE)) { // Tapaamiset - Avoimet / Valmiit
			AvEmployeeServiceHandle handle = new AvEmployeeServiceHandle();
			handle.setMessageSource(getMessageSource());
			tasks = handle.getAppointments(userUid, first, max, taskType, keyword);
			totalTasksNum = handle.getTotalAppointmentsNum(userUid, taskType, keyword);
		} else if(taskType.equals(TASK_TYPE_CONSENT_EMPLOYEE_CONSENTS)) { // Virkailijan lähetetyt suostumus pyynnöt
			TivaEmployeeServiceHandle tivaHandle = new TivaEmployeeServiceHandle();
			tivaHandle.setMessageSource(getMessageSource());
			tasks = tivaHandle.getConsents(userUid, keyword, field, first, max);
			totalTasksNum = tivaHandle.getTotalConsents(userUid, keyword, field);
		} else if(taskType.equals(TASK_TYPE_WARRANT_LIST_CITIZEN_CONSENTS)) {	// Virkailija: Selaa asiakkaan valtakirjoja TIVA-13
			if (keyword != null && !keyword.isEmpty()) {
				KokuEmployeeWarrantHandle warrantHandle = new KokuEmployeeWarrantHandle();
				warrantHandle.setMessageSource(getMessageSource());
				tasks = warrantHandle.getAuthorizationsByUserId(keyword, first, max);
				totalTasksNum = warrantHandle.getUserRecievedWarrantCount(keyword);						
			}
		} else if(taskType.equals(TASK_TYPE_WARRANT_LIST_SUBJECT_CONSENTS)) {	// Virkailija: Selaa asian valtakirjoja TIVA-14
			if (keyword != null && !keyword.isEmpty()) {
				KokuEmployeeWarrantHandle warrantHandle = new KokuEmployeeWarrantHandle();
				warrantHandle.setMessageSource(getMessageSource());
				tasks = warrantHandle.getAuthorizationsByTemplateId(keyword, first, max);
				totalTasksNum = warrantHandle.getUserRecievedWarrantCount(keyword);
			}
		} else { // for message
			MessageHandle msgHandle = new MessageHandle();
			msgHandle.setMessageSource(getMessageSource());
			tasks = msgHandle.getMessages(userUid, taskType, keyword, field, orderType, first, max);			
			totalTasksNum = msgHandle.getTotalMessageNum(userUid, taskType, keyword, field);
		}
		
		totalPages = (totalTasksNum == 0) ? 1:(int) Math.ceil((double)totalTasksNum/numPerPage);
		jsonModel.put(JSON_TASKS, tasks);
		jsonModel.put(JSON_TOTAL_ITEMS, totalTasksNum);
		jsonModel.put(JSON_TOTAL_PAGES, totalPages);
		jsonModel.put(JSON_LOGIN_STATUS, TOKEN_STATUS_VALID);		
	}	
}
