package fi.arcusys.koku.palvelut.util;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

public class ValidatorTest {
	

	public static final String soapGetMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://soa.av.koku.arcusys.fi/\"> " +
											   "<soapenv:Header/>" +
											   "<soapenv:Body>" +
											      "<soa:getAppointmentById>" +
											         "<appointmentId>1</appointmentId>" +
											      "</soa:getAppointmentById>" +
											   "</soapenv:Body>" +
											"</soapenv:Envelope>";
	
	public static final String soapFindMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://soa.av.koku.arcusys.fi/\"> " +
											   "<soapenv:Header/>" +
											   "<soapenv:Body>" +
											      "<soa:findAppointmentById>" +
											         "<appointmentId>1</appointmentId>" +
											      "</soa:findAppointmentById>" +
											   "</soapenv:Body>" +
											"</soapenv:Envelope>";
	
	
	public static final String soapSetMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://soa.av.koku.arcusys.fi/\"> " +
											   "<soapenv:Header/>" +
											   "<soapenv:Body>" +
											      "<soa:setAppointmentById>" +
											         "<appointmentId>1</appointmentId>" +
											      "</soa:setAppointmentById>" +
											   "</soapenv:Body>" +
											"</soapenv:Envelope>";
	
	public static final String soapBroken = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soa=\"http://soa.av.koku.arcusys.fi/\"> " +
											   "<soapenv:Header/>" +
											   "<soapenv:Body>" +
											      "soa:getAppointmentById>" +
											         "<appointmentId>1</appointmentId>" +
											      "</soa:getAppointmentById>" +
											   "</soapenv:Body>" +
											"</soapenv:Envelope>";
	
	public final String ILLEGAL_OPERATIONS_REGEX = "<soa:get|<soa:find";
	
	
	@Test
	public void regexTest() {		
		Pattern pa = Pattern.compile(ILLEGAL_OPERATIONS_REGEX);
		assertTrue(pa.matcher(soapGetMsg).find());		 
		assertFalse(pa.matcher(soapSetMsg).find());
		assertTrue(pa.matcher(soapFindMsg).find());
		assertFalse(pa.matcher(soapBroken).find());
	}
	
	@Test
	public void validatorTest() {
		OperationsValidator validate = new OperationsValidatorImpl();
		
		assertFalse(validate.isValid(soapGetMsg));
		assertTrue(validate.isValid(soapSetMsg));
		assertFalse(validate.isValid(soapFindMsg));
		assertTrue(validate.isValid(soapBroken));	
		
	}

}
