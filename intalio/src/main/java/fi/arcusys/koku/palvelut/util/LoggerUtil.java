package fi.arcusys.koku.palvelut.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import javax.portlet.PortletRequest;
//import javax.portlet.RenderRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

public class LoggerUtil {

	private static Logger logger = Logger.getLogger(LoggerUtil.class);
	public static void logAction(String message){
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(d);
		XMLGregorianCalendar date2 = null;
		String date = null;
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			date = date2.toString();
		} catch (DatatypeConfigurationException e)
		{}
		String action = new String("loggingservice "+message+" " + date);
		
		logger.warn(action);
	}
}
