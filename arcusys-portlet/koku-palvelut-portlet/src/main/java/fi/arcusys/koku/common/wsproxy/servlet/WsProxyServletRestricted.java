package fi.arcusys.koku.common.wsproxy.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fi.arcusys.koku.util.KokuWebServicesJS;
import fi.koku.settings.KoKuPropertiesUtil;

public class WsProxyServletRestricted extends HttpServlet implements Servlet {
    private static final long serialVersionUID = 1L;
    
    private static final Set<String> RESTRICTED_ENDPOINTS = new HashSet<String>(); 
	private static final Log LOG = LogFactory.getLog(WsProxyServletRestricted.class);

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
    		RESTRICTED_ENDPOINTS.add(value);    			
    		LOG.info("Added new endpoint to WsProxyServlet: "+value);
    	}
    }
    
    /**
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        if (config.getInitParameter("endpoints") != null) {
//            for (final String parameter : config.getInitParameter("endpoints").split(",")) {
//                final String endpoint = parameter.trim();
//                if (!endpoint.isEmpty()) {
//                    restrictedEndpoints.add(endpoint);
//                }
//            }
//        }
        
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String s = processRequest(request);
        response.setContentType("text/xml; charset=UTF-8");
        PrintWriter printwriter = response.getWriter();
        printwriter.write(s);
        printwriter.close();
    }

    protected String processRequest(HttpServletRequest request) {
        if (request.getParameter("endpoint") == null) {
            return generateErrorResponse("Missing parameter: endpoint");
        }
        
        final String endpoint = request.getParameter("endpoint").trim();
        if (!RESTRICTED_ENDPOINTS.contains(endpoint)) {
            return generateErrorResponse("Endpoint '" + endpoint + "' is not allowed for proxying.");
        }

        OMElement omelement;
        try {
            String message = request.getParameter("message");

            if (message == null) {
                BufferedReader bodyreader = request.getReader();
                StringBuilder messagebuilder = new StringBuilder();
                do
                    messagebuilder.append(bodyreader.readLine());
                while (bodyreader.ready());
                message = messagebuilder.toString();
            }

            omelement = parseRequest(message);
        } catch (Exception e) {
            return generateErrorResponse("Malformed message: " + e.getMessage());
        }
        Options options = new Options();

//      String address = request.getScheme() + "://"
//              + request.getServerName() + ":" + request.getServerPort();
//      address = address + request.getParameter("endpoint");
        options.setTo(new EndpointReference(endpoint));

        String action = request.getParameter("action");

        if (action == null) {
        	action = request.getHeader("soapaction");
        }
        options.setAction(action);

        return send(omelement, options);
    }

    protected OMElement parseRequest(String s) throws Exception {
        OMElement omelement = null;
        StringReader stringreader = new StringReader(s);
        XMLStreamReader xmlstreamreader = XMLInputFactory.newInstance()
                .createXMLStreamReader(stringreader);
        StAXOMBuilder staxombuilder = new StAXOMBuilder(xmlstreamreader);
        OMElement omelement1 = staxombuilder.getDocumentElement();

        OMElement omelement2 = null;
        for (Iterator children = omelement1.getChildElements(); children
                .hasNext();) {
            omelement2 = (OMElement) children.next();
            if (omelement2.getText().toLowerCase().contains("body")) {
                break;
            }

        }

        omelement = omelement2.getFirstElement();
        return omelement;
    }

    protected String send(OMElement omelement, Options options) {
        String response = null;
        try {
            ServiceClient serviceclient = new ServiceClient();
            serviceclient.setOptions(options);

            OMElement omelement1 = serviceclient.sendReceive(omelement);
            response = generateSuccessResponse(omelement1);
        } catch (Exception exception) {
            response = generateErrorResponse("Error sending to tempo. "
                    + exception);
        }

        return response;
    }

    protected String generateErrorResponse(String s) {
        return generateResponse("error", s, null);
    }

    protected String generateSuccessResponse(OMElement omelement) {
        return generateResponse("success", null, omelement);
    }

    protected String generateResponse(String s, String s1, OMElement omelement) {
        OMFactory omfactory = OMAbstractFactory.getOMFactory();
        OMElement omelement1 = omfactory.createOMElement("response", null);
        OMElement omelement2 = createTextElement(omfactory, "status", s);
        omelement1.addChild(omelement2);

        if (s1 != null) {
            OMElement omelement3 = createTextElement(omfactory, "message", s1);
            omelement1.addChild(omelement3);
        }

        if (omelement != null) {
            OMElement omelement4 = omfactory.createOMElement("payload", null);
            omelement4.addChild(omelement);
            omelement1.addChild(omelement4);
        }

        System.out.println("Test output:" + omelement1.toString());
        return omelement1.toString();
    }

    protected OMElement createTextElement(OMFactory omfactory, String s,
            String s1) {
        OMElement omelement = omfactory.createOMElement(s, null);
        OMText omtext = omfactory.createOMText(omelement, s1);
        omelement.addChild(omtext);
        return omelement;
    }
    
    public static void main(String[] args) {
        System.out.println(256 + "");
    }
}
