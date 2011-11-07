package fi.arcusys.koku.common.wsproxy.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.arcusys.koku.util.KokuWebServicesJS;
import fi.koku.settings.KoKuPropertiesUtil;

import net.sf.json.JSONObject;

import java.util.*;
public class WSUtils extends HttpServlet implements Servlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(WsProxyServletRestricted.class);
	private static final Map<String,String> RESTRICTED_ENDPOINTS = new HashMap<String, String>(); 
	

    static {    	
    	for (KokuWebServicesJS key : KokuWebServicesJS.values()) {
    		String value = KoKuPropertiesUtil.get(key.value());
    		if (value == null) {
    			throw new ExceptionInInitializerError("Coulnd't find property '"+ key.value()+"'");
    		}
    		if (value.endsWith("?wsdl")) {
    			int end = value.indexOf("?wsdl");
    			value = value.substring(0, end);
    		} 
    		RESTRICTED_ENDPOINTS.put(key.value(), value);    			
    		LOG.info("Added new endpoint to WSUtils: "+value);
    	}
    }
	

    public  void doPost(HttpServletRequest request, HttpServletResponse  response) throws IOException, ServletException {
    	response.setContentType("application/json");
    	PrintWriter out = response.getWriter();  
    	JSONObject obj = new JSONObject();
    	obj.element("services", RESTRICTED_ENDPOINTS);    	
    	out.print(obj.toString());
    	out.flush();
    }
    
    
}
