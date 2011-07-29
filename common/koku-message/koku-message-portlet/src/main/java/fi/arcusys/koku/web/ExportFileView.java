package fi.arcusys.koku.web;

import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import fi.arcusys.koku.request.KokuRequest;
import fi.arcusys.koku.requestservice.Answer;
import fi.arcusys.koku.requestservice.Question;
import fi.arcusys.koku.requestservice.Response;

/**
 * Implement ajax view for ajax response in json format
 * @author Jinhua Chen
 *
 */
@Component("downloadView")
public class ExportFileView extends AbstractView {

	public ExportFileView() {
		super();
		super.setContentType("text/csv; charset=utf-8");
	}
    
    @Override
    protected void renderMergedOutputModel (Map<String, Object> map, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	response.reset();
    	response.setContentType("application/octet-stream");
    	response.setCharacterEncoding("UTF-8");
    	String fileName = "response.csv";
    	response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    	System.out.println("rocky good");
    	BufferedWriter writer = new BufferedWriter(response.getWriter());
    	
        if (map == null || map.isEmpty ()) {
            return;
        }else {
        	KokuRequest kokuRequest = (KokuRequest) map.get("kokuRequest");
        	writer.write("Response Summary");
        	writer.newLine();
        	writer.write(",");
        	
        	Iterator<Question> it_q = kokuRequest.getQuestions().iterator();
        	
        	while(it_q.hasNext()) {
        		Question q = it_q.next();
        		writer.write(q.getDescription() + "," + ",");
        	}
        	writer.newLine();
        	writer.write("Respondent,");
        	
        	for(int i=0; i < kokuRequest.getQuestions().size(); i++) {
        		writer.write("Answer,Comment,");
        	}
        	writer.newLine();
        	
        	Iterator<Response> it_res = kokuRequest.getRespondedList().iterator();
        	while(it_res.hasNext()) {
        		Response res = it_res.next();
        		writer.write(res.getName() + ",");
        		
        		Iterator<Answer> it_ans = res.getAnswers().iterator();
        		while(it_ans.hasNext()){
        			Answer answer = it_ans.next();
        			writer.write(answer.getAnswer() + "," + answer.getComment() + ",");
        		}
        		writer.newLine();
        	}
        	writer.newLine();
        	writer.write("Missed");
        	writer.newLine();
        	Iterator<String> it_unres = kokuRequest.getUnrespondedList().iterator();
        	while(it_unres.hasNext()) {
        		String name = it_unres.next();
        		writer.write(name);
        		writer.newLine();
        	}
        	
        	writer.flush();
        	writer.close();
        } 

    }

}
