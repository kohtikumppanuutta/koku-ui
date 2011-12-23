package fi.arcusys.koku.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.annotation.Resource;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.log4j.Logger;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.kv.model.KokuAnswer;
import fi.arcusys.koku.kv.model.KokuQuestion;
import fi.arcusys.koku.kv.model.KokuRequest;
import fi.arcusys.koku.kv.model.KokuResponse;
import fi.arcusys.koku.kv.request.employee.EmployeeRequestHandle;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Generates csv file containing response summary information
 * @author Jinhua Chen
 * Aug 4, 2011
 */
@Controller("exportFileController")
@RequestMapping(value = "VIEW")
public class ExportFileController {

	private static final Logger LOG = Logger.getLogger(ExportFileController.class);
	
	@Resource
	private ResourceBundleMessageSource messageSource;
	
	private final static String SEPARATOR 					= ";";
	private final static String TEXT_DELIMITER				= "\"";
	private final static String NEW_LINE					= "\n";
	private final static String FILTER						= "\\r\\n|\\r|\\n|"+SEPARATOR+"|"+TEXT_DELIMITER;

	
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
		KokuRequest kokuRequest = reqhandle.getKokuRequestById(requestId);
		String requestSubject = kokuRequest.getSubject();
		if (requestSubject == null || requestSubject.isEmpty()) {
			requestSubject = "vastaus";
		}
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
					
					int length = res.getAnswers().size()+1;
					String[] answers = new String[length];
					
					for (KokuAnswer answer : res.getAnswers()) {
						answers[answer.getQuestionNumber()] =  answer.getAnswer();
					}
					
					for (String answer : answers) {
						if (answer != null) {
							writer.write(addQuote(answer) + SEPARATOR);													
						}
					}					
					writer.write(addQuote(res.getComment()));
					writer.write(NEW_LINE);
				}
				
				writer.write(NEW_LINE);
				writer.write(addQuote(messageSource.getMessage("export.missed", null, locale)));
				writer.write(NEW_LINE);

				for(String name : kokuRequest.getUnrespondedList()) {
					writer.write(addQuote(name));
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
