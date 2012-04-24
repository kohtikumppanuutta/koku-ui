package fi.arcusys.koku.users;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

/**
 * Logs SOAP in/out messages.
 * @author mlind
 *
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {
	private static final Logger LOG = Logger.getLogger(SOAPLoggingHandler.class);		

	// private PrintStream out = ;

	@Override
	public final Set<QName> getHeaders() {
		return null;
	}

	@Override
	public final boolean handleMessage(SOAPMessageContext smc) {
//		logToSystemOut(smc);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			smc.getMessage().writeTo(out);
			LOG.info(out.toString());
		} catch (SOAPException e) {
			LOG.error(e);
		} catch (IOException ioe) {
			LOG.error(ioe);
		}
		LOG.info("SOAP - '" + out.toString()+"'");
		return true;
	}

	@Override
	public final boolean handleFault(SOAPMessageContext smc) {
		// logToSystemOut(smc);
		return true;
	}

	@Override
	public void close(MessageContext messageContext) {
	}
	
//	private void logToSystemOut(SOAPMessageContext smc) {
//		LOG.info("LOG SOAP ?!");
//
//		Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
//		
//		if (outboundProperty.booleanValue()) {
//			out.println("\n["+new Date()+"] Outbound message:");
//		} else {
//			out.println("\n["+new Date()+"] Inbound message:");
//		}
//
//		SOAPMessage message = smc.getMessage();
//		try {
//			message.writeTo(out);
//			LOG.
//			out.println(""); // just to add a newline
//			LOG.error("SOAP msg - '" + out.toString() + "'");
//			
//		} catch (Exception e) {
//			LOG.error("Exception in handler: "+ e);
//			out.println("Exception in handler: " + e);
//		}
//	}
	
	@SuppressWarnings("rawtypes")
	public static void addSOAPLogger(BindingProvider bp) {
		LOG.info("Add SOAP logger");

		//add logging
		Binding binding = bp.getBinding();
		List<Handler> handlerList = binding.getHandlerChain();
		if (null == handlerList) {
			handlerList = new ArrayList<Handler>();
		}			
		handlerList.add(new SOAPLoggingHandler());
		binding.setHandlerChain(handlerList);
	}

}
