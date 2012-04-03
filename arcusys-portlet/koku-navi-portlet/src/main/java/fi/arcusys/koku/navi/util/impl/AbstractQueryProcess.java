package fi.arcusys.koku.navi.util.impl;

import static fi.arcusys.koku.util.Constants.*;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.intalio.TaskHandle;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.model.KokuFolderType;
import fi.arcusys.koku.navi.util.QueryProcess;
import fi.koku.settings.KoKuPropertiesUtil;

public abstract class AbstractQueryProcess implements QueryProcess {
	
	private static final Logger LOG = Logger.getLogger(AbstractQueryProcess.class);	
	
	public static final String RECEIVED_REQUESTS_FILTER;
	public static final String RECEIVED_INFO_REQUESTS_FILTER;
	public static final String RECEIVED_KINDERGARTED_APPLICATION_FILTER;
	public static final String RECEIVED_WARRANTS_FILTER;
	
	static {
		LOG.info("Received requests filter (Pyynnöt - Saapuneet): " + KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_REQUESTS));
		LOG.info("Received inforequests filter (Tietopyynnöt - Saapuneet): " + KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_INFO_REQUESTS));
		LOG.info("Received applications filter (Asiointipalvelut - Hakemusten vahvistuspyynnöt): " + KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_APPLICATIONS));
		LOG.info("Received warrants filter (Valtakirjat - Valtuutettuna): " + KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_WARRANTS));
		RECEIVED_REQUESTS_FILTER =  KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_REQUESTS).trim();
		RECEIVED_INFO_REQUESTS_FILTER =  KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_INFO_REQUESTS).trim();
		RECEIVED_KINDERGARTED_APPLICATION_FILTER = KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_APPLICATIONS).trim();		
		RECEIVED_WARRANTS_FILTER = KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_WARRANTS).trim();
	}
	
	private MessageSource messageSource;
	
	public AbstractQueryProcess(MessageSource messages) {
		this.messageSource = messages;
	}
	
	
	public final void setMessageSource(MessageSource messages) {
		this.messageSource = messages;
	}

	
	public final MessageSource getMessageSource() {
		return messageSource;
	}
	

	/**
	 * Returns number of new messages in the given folder type from web services
	 * 
	 * @param userId
	 * @param folderType
	 * @return number of messages
	 * @throws KokuServiceException 
	 */
	protected int getNewMessageNum(String userId, KokuFolderType folderType) throws KokuServiceException {
		MessageHandle messageHandle = new MessageHandle();
		return messageHandle.getUnreadMessages(userId, folderType);
	}

	/**
	 * Returns total amount of requests (Pyynnöt - Saapuneet)
	 * 
	 * @param userId
	 * @return number or requests
	 */
	protected int getTotalRequests(String username, String token) {
		if (username != null && !username.isEmpty() && token != null && !token.isEmpty()) {
			TaskHandle handle = new TaskHandle(token, username);
			return handle.getTasksTotalNumber(RECEIVED_REQUESTS_FILTER);
		} else {
			return 0;
		}
	}

}
