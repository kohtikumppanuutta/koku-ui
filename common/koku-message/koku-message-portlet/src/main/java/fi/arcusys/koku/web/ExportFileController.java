package fi.arcusys.koku.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.request.KokuRequest;
import fi.arcusys.koku.request.RequestHandle;
import fi.arcusys.koku.requestservice.Answer;
import fi.arcusys.koku.requestservice.Question;
import fi.arcusys.koku.requestservice.Response;

/**
 * Show task form page and store the current query information on the jsp page
 * 
 * @author Jinhua Chen
 * 
 */
@Controller("exportFileController")
@RequestMapping(value = "VIEW")
public class ExportFileController {

	@ResourceMapping(value = "exportFileOld")
	public ModelAndView download_old(@RequestParam(value = "newRequestId") String requestId) {

		RequestHandle reqhandle = new RequestHandle();
		KokuRequest req = reqhandle.getKokuRequestById(requestId);
		Map map = new HashMap();
		map.put("kokuRequest", req);

		return new ModelAndView("downloadView", map);

	}

	@ResourceMapping(value = "exportFile")
	public void download(@RequestParam(value = "newRequestId") String requestId,
			ResourceRequest resourceRequest, ResourceResponse response) {
		response.setContentType("text/csv; charset=utf-8");
		response.setProperty("Content-Disposition", "attachment; filename=response.csv");
		RequestHandle reqhandle = new RequestHandle();
		KokuRequest kokuRequest = reqhandle.getKokuRequestById(requestId);

		try {
			BufferedWriter writer = new BufferedWriter(response.getWriter());

			if (kokuRequest == null) {
				return;
			} else {
				writer.write(addQuote("Response Summary"));
				writer.newLine();
				writer.write(addQuote()+",");

				Iterator<Question> it_q = kokuRequest.getQuestions().iterator();

				while (it_q.hasNext()) {
					Question q = it_q.next();
					writer.write(addQuote(q.getDescription()) + "," +addQuote()+ ",");
				}
				writer.newLine();
				writer.write(addQuote("Respondent")+",");

				for (int i = 0; i < kokuRequest.getQuestions().size(); i++) {
					writer.write(addQuote("Answer")+","+addQuote("Comment")+",");
				}
				writer.newLine();

				Iterator<Response> it_res = kokuRequest.getRespondedList()
						.iterator();
				while (it_res.hasNext()) {
					Response res = it_res.next();
					writer.write(addQuote(res.getName()) + ",");

					Iterator<Answer> it_ans = res.getAnswers().iterator();
					while (it_ans.hasNext()) {
						Answer answer = it_ans.next();
						writer.write(addQuote(answer.getAnswer()) + ","
								+ addQuote(answer.getComment()) + ",");
					}
					writer.newLine();
				}
				writer.newLine();
				writer.write(addQuote("Missed"));
				writer.newLine();
				Iterator<String> it_unres = kokuRequest.getUnrespondedList()
						.iterator();
				while (it_unres.hasNext()) {
					String name = it_unres.next();
					writer.write(addQuote(name));
					writer.newLine();
				}

				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String addQuote(String s) {
		String q = "\"";
		
		return q+s+q;
	}
	
	private String addQuote() {
		return "\"\"";
	}

}
