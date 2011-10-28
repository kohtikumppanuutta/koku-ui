package fi.arcusys.koku.palvelut.util;


/**
 * Class to resolve endpoint and and parses SOAP message
 * from given data
 * 
 * @author Toni Turunen
 *
 */
public class EndPointResolverImpl implements EndPointResolver {
	
	private static final String XML_HEADER_REGEX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String ENDPOINT_REGEX = "assembly=";
	
	private String endPoint;
	private String soap;
	
	public EndPointResolverImpl(String ajaxData) {
		filterData(ajaxData);
	}
	
	private void filterData(String data) {
		int soapMsgBeginIndex = data.indexOf(XML_HEADER_REGEX, 0);
		soap = data.substring(soapMsgBeginIndex);
		
		int serviceIndex = data.indexOf(ENDPOINT_REGEX, 0) + ENDPOINT_REGEX.length();
		int serviceEndIndex = data.indexOf("&", serviceIndex);
		endPoint = data.substring(serviceIndex, serviceEndIndex);		
	}	

	@Override
	public final String getEndPoint() {
		return endPoint;
	}
	 
	@Override
	public final String getSoap() {
		return soap;
	}


}
