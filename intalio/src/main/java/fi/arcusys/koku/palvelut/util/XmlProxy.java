package fi.arcusys.koku.palvelut.util;

import java.io.StringReader;
import java.util.Iterator;

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
import org.apache.log4j.Logger;

/**
 * XmlProxy <br/><br/>
 * Copied functionality from WsProxyServletRestricted (koku project)
 * 
 * @author Toni Turunen
 *
 */
public class XmlProxy {
	
	private static final Logger log = Logger.getLogger(XmlProxy.class);

	private final String message;
	private final String action;
	private final String endpoint;
	
	public XmlProxy(String action, String endpoint, String message) {
		
		if (message == null || action == null || endpoint == null) {
			log.error("One of given constructor parameter was null");
			throw new IllegalStateException();
		}
		
		this.action = action;
		this.message = message;
		this.endpoint = endpoint;
	}	
	
	public String send() throws Exception {
		
		OMElement omelement = parseRequest(message);	
		Options options = new Options();
		options.setTo(new EndpointReference(endpoint));
		options.setAction(action);
		return send(omelement, options);
	}
	
	protected OMElement parseRequest(String s) throws Exception  {
		OMElement omelement = null;
		StringReader stringreader = new StringReader(s);
		XMLStreamReader xmlstreamreader = XMLInputFactory.newInstance().createXMLStreamReader(stringreader);
		StAXOMBuilder staxombuilder = new StAXOMBuilder(xmlstreamreader);
		OMElement omelement1 = staxombuilder.getDocumentElement();

		OMElement omelement2 = null;
		for (Iterator children = omelement1.getChildElements(); children.hasNext();) {
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

		log.debug("Test output:" + omelement1.toString());
		return omelement1.toString();
	}
	
	protected OMElement createTextElement(OMFactory omfactory, String s,
			String s1) {
		OMElement omelement = omfactory.createOMElement(s, null);
		OMText omtext = omfactory.createOMText(omelement, s1);
		omelement.addChild(omtext);
		return omelement;
	}

	public final String getMessage() {
		return message;
	}

	public final String getAction() {
		return action;
	}

	public final String getEndpoint() {
		return endpoint;
	}
	
}
