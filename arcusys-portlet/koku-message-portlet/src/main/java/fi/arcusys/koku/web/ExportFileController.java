package fi.arcusys.koku.web;

import java.io.BufferedWriter;
import java.io.IOException;
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
		final String username = resourceRequest.getUserPrincipal().getName();

		final Locale locale = MessageUtil.getLocale();
		
		try {
			BufferedWriter writer = new BufferedWriter(response.getWriter());

			if (kokuRequest != null) {
				/* Headers */
				writer.write(addQuote(messageSource.getMessage("export.responseSummary", null, locale)));
				writer.newLine();
				writer.write(addQuote(messageSource.getMessage("export.respondent", null, locale))+",");

				for (KokuQuestion q : kokuRequest.getQuestions()) {
					writer.write(addQuote(q.getDescription()) + ",");
				}				
				writer.write(addQuote(messageSource.getMessage("export.comment", null, locale))+",");
				writer.newLine();
				
				/* Data */
				for (KokuResponse res : kokuRequest.getRespondedList()) {
					
					writer.write(addQuote(res.getName())+",");
					
					int length = res.getAnswers().size();
					String[] answers = new String[length];
										
					for (KokuAnswer answer : res.getAnswers()) {
						answers[answer.getQuestionNumber()] = answer.getAnswer();
					}
					
					for (String answer : answers) {
						writer.write(addQuote(answer) + ",");						
					}
					
					writer.write(addQuote(res.getComment()));
					writer.newLine();
				}
				
				writer.newLine();
				writer.write(addQuote(messageSource.getMessage("export.missed", null, locale)));
				writer.newLine();

				for(String name : kokuRequest.getUnrespondedList()) {
					writer.write(addQuote(name));
					writer.newLine();
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
		String q = "\"";
		
		return q+s+q;
	}
	
	/**
	 * Generates quotation mark char '"'
	 * @return
	 */
	private String addQuote() {
		return "\"\"";
	}

}
