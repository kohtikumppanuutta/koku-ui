package fi.arcusys.koku.palvelut.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.workflow.task.Task;

public class URLUtil {
	
	private URLUtil() {
		// No need to instantiate
	}

	private static class FormManagerInformation {
		public enum FormType {XFORMS, AJAXFORMS};
		public FormType type;
		public String url;
	}

	private static Log LOG = LogFactory.getLog(URLUtil.class);
	
	private static final String ORBEON_FORMS_URL_SCHEME = "oxf://";
	
	// Local form manager urls for different types of forms
	private static final String XFORMS_FORM_MANAGER_URL = "/palvelut-portlet/xforms/init";
	private static final String AJAXFORMS_FORM_MANAGER_URL_PREFIX = "/palvelut-portlet/ajaxforms";
	
	// The web applicatiot part in the remote ajax form urls
	private static final String REMOTE_AJAXFORMS_WEB_APP_URL_PART = "/gi/";
	private static final String LOCAL_AJAXFORMS_WEB_APP_URL_PART = "/palvelut-portlet/ajaxforms/";
	private static final String CHARACTER_ENCODING = "UTF-8";

	/*
	* Returns a task specific URL pointing to the formManager for displaying the form.
	* 
	* @param t The task in question.
	* @param formManagerUrl The URL to the form manager.
	* @param ticket the user's authentication token.
	* @param user The user name.
	*/
	public static String getFormURLForTask(Task t, String ticket, PortletRequest request) {
		
		String userName = null;
		
		try {

			String realm = MigrationUtil.getCompanyWebId(request);
			userName = realm + "/" + MigrationUtil.getUser(request);
			
		} catch (Exception e) {
			LOG.error("Couldn't fetch realm or username from request.", e);
			return null;
		}

		FormManagerInformation formManagerInformation = getFormManagerURLForTask(t);
		return getFormURLForTask(t, formManagerInformation, ticket, userName);
	}
	
	private static String getFormURLForTask(Task task, FormManagerInformation formManagerInformation, String ticket, String user) {
	
		switch (formManagerInformation.type) {
			case XFORMS: return getXFormURLForTask(task, ticket, user);
			case AJAXFORMS: return getAjaxFormURLForTask(task, formManagerInformation.url, ticket, user);
			default:
				LOG.error("Unknown form manager type: " + formManagerInformation.type);
				return null;
		}
	}
	
	private static String getXFormURLForTask(Task t, String ticket, String userIncludingRealm) {
		String url = t.getFormURLAsString();
		try {
			Object[] params = new Object[] { XFORMS_FORM_MANAGER_URL, t.getID(), t.getClass().getSimpleName(),
				URLEncoder.encode(url, CHARACTER_ENCODING), ticket,
				URLEncoder.encode(userIncludingRealm, CHARACTER_ENCODING) };
			return MessageFormat.format("{0}?id={1}&type={2}&url={3}&token={4}&user={5}", params);
		}
		catch (UnsupportedEncodingException e) {
			LOG.error("Unsupported encoding encountered while producing form URL");
			throw new RuntimeException(e);
		}
	}
	
	private static String getAjaxFormURLForTask(Task t, String formManagerUrl, String ticket, String userIncludingRealm) {
		// Replace remote web app with the local one in the url.
		String url = t.getFormURLAsString().replace(REMOTE_AJAXFORMS_WEB_APP_URL_PART, LOCAL_AJAXFORMS_WEB_APP_URL_PART);
		try {
			Object[] params = new Object[] { formManagerUrl, t.getID(), t.getClass().getSimpleName(),
				URLEncoder.encode(url, CHARACTER_ENCODING), ticket,
				URLEncoder.encode(userIncludingRealm, CHARACTER_ENCODING) };
			return MessageFormat.format("{0}?id={1}&type={2}&url={3}&token={4}&user={5}", params);
		}
		catch (UnsupportedEncodingException e) {
			LOG.error("Unsupported encoding encountered while producing form URL");
			throw new RuntimeException(e);
		}
	}
	
	/**
	* Returns the correct formManager url for the type of task's form.
	*
	* @param t The task in question.
	*/
	private static FormManagerInformation getFormManagerURLForTask(Task t) {			
		FormManagerInformation formManagerInformation = new FormManagerInformation();
		
		String formUrl = t.getFormURLAsString();
		LOG.debug("Form url: " + formUrl);
		if (formUrl.startsWith(ORBEON_FORMS_URL_SCHEME)) { 
			formManagerInformation.type = FormManagerInformation.FormType.XFORMS;
			formManagerInformation.url = XFORMS_FORM_MANAGER_URL; 
		} else { 
			formManagerInformation.type = FormManagerInformation.FormType.AJAXFORMS;
			// The ajax form manager url is derived from each form's url.
			formManagerInformation.url = AJAXFORMS_FORM_MANAGER_URL_PREFIX + "/" + getAjaxFormManagerUrlSuffix(formUrl); 
		}

		LOG.debug("Form manager url: " + formManagerInformation.url);
		return formManagerInformation;
	}
	
	private static String getAjaxFormManagerUrlSuffix(String url) {
		// The URL is in the following form:
		// http://host:port/[remote web app, i.e. form manager]/[form manager url suffix]
		int beginIndex = url.indexOf(REMOTE_AJAXFORMS_WEB_APP_URL_PART) + REMOTE_AJAXFORMS_WEB_APP_URL_PART.length();
		int endIndex = url.length();
		return url.substring(beginIndex, endIndex);
	}
		
}
