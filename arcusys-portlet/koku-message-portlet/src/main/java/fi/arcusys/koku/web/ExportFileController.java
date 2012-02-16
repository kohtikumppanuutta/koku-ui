package fi.arcusys.koku.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Resource;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.model.KokuAnswer;
import fi.arcusys.koku.kv.model.KokuQuestion;
import fi.arcusys.koku.kv.model.KokuRequest;
import fi.arcusys.koku.kv.model.KokuResponse;
import fi.arcusys.koku.kv.request.employee.EmployeeRequestHandle;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Generates csv file containing response summary information
 * @author Jinhua Chen
 * Aug 4, 2011
 */
@Controller("exportFileController")
@RequestMapping(value = "VIEW")
public class ExportFileController {

	private static final Logger LOG = LoggerFactory.getLogger(ExportFileController.class);
	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
	private final static String SEPARATOR 					= ";";
	private final static String TEXT_DELIMITER				= "\"";
	private final static String NEW_LINE					= "\n";
	private final static String FILTER						= "\\r\\n|\\r|\\n|"+SEPARATOR+"|"+TEXT_DELIMITER;
	
	private final static Comparator<KokuAnswer> SORT_BY_ANSWER_NUMBER = new Comparator<KokuAnswer>() {
			@Override
			public int compare(KokuAnswer o1, KokuAnswer o2) {
				if (o1.getQuestionNumber() > o2.getQuestionNumber()) {
					return 1;
				} else if (o1.getQuestionNumber() < o2.getQuestionNumber()) {
					return -1;
				} else {
					return 0;
				}
			}
		};
	
	/**
	 * Generates the request summary with given request id in csv format 
	 * for downloading
	 * @param requestId
	 * @param resourceRequest
	 * @param response
	 */
	@ResourceMapping(value = "exportFile")
	public void download(@RequestParam(value = "newRequestId") String requestId,
			ResourceRequest resourceRequest, ResourceResponse response) {
		response.setContentType("text/csv; charset=utf-8");
		EmployeeRequestHandle reqhandle = new EmployeeRequestHandle();
		KokuRequest kokuRequest = null;
		try {
			kokuRequest = reqhandle.getKokuRequestById(requestId);
		} catch (KokuServiceException kse) {
			LOG.error("Error when trying to create CSV export. WS doesn't work properly. requestId: '"+requestId+"'", kse);
		}
		String requestSubject = kokuRequest.getSubject();
		if (requestSubject == null || requestSubject.isEmpty()) {
			requestSubject = "vastaus";
		}
		
		/* Note: Do not change Pragma and Cache-Control values. Othervise IE doesn't recognize
		 * CSV file properly when using HTTPS. See http://support.microsoft.com/kb/316431 for more details.
		 * This is issue with IE5 - IE9 versions. */
		response.setProperty("Pragma", "private");
		response.setProperty("Cache-Control", "private, must-revalidate");

		response.setProperty("Content-Disposition", "attachment; filename="+requestSubject+".csv");
		response.setProperty("Content-Type", "text/xml, charset=UTF-8; encoding=UTF-8");


		final String username = resourceRequest.getUserPrincipal().getName();
		final Locale locale = MessageUtil.getLocale();		
		try {
			BufferedWriter writer = new BufferedWriter(response.getWriter());
			/* UTF-8 BOM (Do not remove, otherwise Excel won't recognize characters correctly!) */
			writer.append('\uFEFF');
			if (kokuRequest != null) {
				/* Headers */
				writer.write(addQuote(messageSource.getMessage("export.responseSummary", null, locale)));
				writer.write(NEW_LINE);
				writer.write(addQuote(messageSource.getMessage("export.respondent", null, locale))+SEPARATOR);

				for (KokuQuestion q : kokuRequest.getQuestions()) {
					writer.write(addQuote(q.getDescription()) + SEPARATOR);
				}				
				writer.write(addQuote(messageSource.getMessage("export.comment", null, locale)));
				writer.write(NEW_LINE);
				
				/* Data */
				for (KokuResponse res : kokuRequest.getRespondedList()) {
					
					writer.write(addQuote(res.getName())+SEPARATOR);					
					Collections.sort(res.getAnswers(), SORT_BY_ANSWER_NUMBER);
					for (KokuAnswer answer : res.getAnswers()) {
						if (answer != null) {
							writer.write(addQuote(answer.getAnswer()) + SEPARATOR);													
						}
					}		
					writer.write(addQuote(res.getComment()));
					writer.write(NEW_LINE);
				}
				
				writer.write(NEW_LINE);
				writer.write(addQuote(messageSource.getMessage("export.missed", null, locale)));
				writer.write(NEW_LINE);

				for (KokuUser name : kokuRequest.getUnrespondedList()) {
					writer.write(addQuote(name.getFullName()));
					writer.write(NEW_LINE);
				}
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			LOG.error("Generate csv file failed. Username: '"+username+"'  RequestId: '"+kokuRequest.getRequestId()+"'");
		}
	}
	
	/**
	 * Adds the quotation mark char '"' to the string
	 * @param s
	 * @return
	 */
	private String addQuote(String s) {		
		return TEXT_DELIMITER+s.replaceAll(FILTER, "")+TEXT_DELIMITER;
	}

}
