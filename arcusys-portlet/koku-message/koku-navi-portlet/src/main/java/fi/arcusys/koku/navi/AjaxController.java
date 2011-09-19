package fi.arcusys.koku.navi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.users.UserIdResolver;

/**
 * Hanldes ajax request and return the response with json string
 * @author Jinhua Chen
 * Jun 22, 2011
 */

@Controller("ajaxController")
@RequestMapping(value = "VIEW")
public class AjaxController extends AbstractController {

	private static Logger LOGGER = Logger.getLogger(AjaxController.class);
	
	/**
	 * Gets the amount of new unread messages for user
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response PortletResponse
	 * @return amount of new unread messages in Json
	 */
	@ResourceMapping(value = "update")
	public String showAjax(ModelMap modelmap, PortletRequest request, PortletResponse response) {
		String username = request.getRemoteUser();
		String userId = null;
		if (username != null) {
			UserIdResolver resolver = new UserIdResolver();
			userId = resolver.getUserId(username, getPortalRole(request));			
		}
		JSONObject jsonModel = getJsonModel(userId);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}
		
	/**
	 * Gets the amount of new messages of Inbox and Archive_Inbox and puts values to model
	 * @param userId user that message belong to
	 * @return Json object contains result
	 */
	public JSONObject getJsonModel(String userId) {
		JSONObject jsonModel = new JSONObject();
		String newInboxMessageNum = "0";
		String  newArchiveMessageNum = "0";
		
		if (userId == null) {
			jsonModel.put("loginStatus", "INVALID");
		} else {
			jsonModel.put("loginStatus", "VALID");
			newInboxMessageNum = getNewMessageNum(userId, "Inbox");
			jsonModel.put("inbox", newInboxMessageNum);
			
			newArchiveMessageNum = getNewMessageNum(userId, "Archive_Inbox");
			jsonModel.put("archive_inbox", newArchiveMessageNum);
						
		}		
		
		return jsonModel;	
	}
	
	/**
	 * Gets number of new messages in the given folder type from web services
	 * @param username  user that message belong to
	 * @param folderType: message folder type such as Inbox, Archive_Inbox
	 * @return number of messages
	 */
	public String getNewMessageNum(String username, String folderType) {
		String num = "0";
		
		try {
			String xmldata = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://soa.kv.koku.arcusys.fi/\">"
					+ "<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<soa:getUnreadMessages>"
					+ "<user>" + username + "</user>"
					+ "<folderType>" + folderType + "</folderType>"
					+ "</soa:getUnreadMessages>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";

			String soapUrl = "http://127.0.0.1:8180/arcusys-koku-0.1-SNAPSHOT-kv-model-0.1-SNAPSHOT/KokuMessageServiceImpl";
			URL url = new URL(soapUrl);
			URLConnection con = url.openConnection();

			// specify that we will send output and accept input
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(20000); // long timeout, but not infinite
			con.setReadTimeout(20000);
			// con.setUseCaches (false);
			// con.setDefaultUseCaches (false);
			// tell the web server what we are sending
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream());
			writer.write(xmldata);
			writer.flush();
			writer.close();
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String xmlStr = "";
			String line;
			
			while ((line = rd.readLine()) != null) {
				xmlStr += line;
			}
									
			int pos1 = xmlStr.indexOf("<return>");
			int pos2 = xmlStr.indexOf("</return>");
			
			num = xmlStr.substring(pos1+8, pos2);			
			
		} catch (Exception e) {
			LOGGER.info("Getting the number of new messages service failed");
			//e.printStackTrace();
		}
		
		return num;
	}

	/**
	 * Creates render url mainly for gatein portal container
	 * @param newNaviType navigation tab name
	 * @param modelmap ModelMap
	 * @param request PortletRequest
	 * @param response ResourceResponse
	 * @return render url in Json 
	 */
	@ResourceMapping(value = "createNaviRenderUrl")
	public String createNaviRenderUrl(
			@RequestParam(value = "newNaviType") String newNaviType,
			ModelMap modelmap, PortletRequest request, ResourceResponse response) {
		PortletURL renderUrlObj = response.createRenderURL();
		renderUrlObj.setParameter( "myaction", "showNavi");
		renderUrlObj.setParameter( "naviType", newNaviType);	
		String renderUrlString = renderUrlObj.toString();
		JSONObject jsonModel = new JSONObject();
		jsonModel.put("renderUrl", renderUrlString);
		modelmap.addAttribute("response", jsonModel);
		
		return AjaxViewResolver.AJAX_PREFIX;
	}

}
