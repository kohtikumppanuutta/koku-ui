package fi.arcusys.koku.palvelut.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class for managing connections.
 * 
 * @author Jon Haikarainen
 */

public class ConnectionHelper {
	
    private static final Log LOG = LogFactory.getLog(ConnectionHelper.class);
    
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_COOKIE = "Cookie";
    private static final String HEADER_SET_COOKIE = "Set-Cookie";
    private static final String HEADER_REFERER = "Referer";
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_CONTENT_LOCATION = "Content-Location";
    private static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    private static final String HEADER_LOCATION = "Location";
    private static final String HEADER_HOST = "Host";
    private static final int BUFFER_SIZE = 2048;
    
    public ConnectionHelper() {
    }

    public static void prepareConnectionRequest(HttpServletRequest req, HttpURLConnection urlCon) {
        prepareConnectionRequest(req, urlCon, false);
    }

    /**
     * Sets the headers of a created connection's request to equal those of the original request.
     * If boolean setCookies is true, the cookies are set as well.
     * 
     * @param req
     * @param urlCon
     * @param setCookies
     */
    @SuppressWarnings("unchecked")
	public static void prepareConnectionRequest(HttpServletRequest req, HttpURLConnection urlCon, boolean setCookies) {
        for (Enumeration e = req.getHeaderNames();e.hasMoreElements();) {
            String header = (String) e.nextElement();
            if (!setCookies && header.equalsIgnoreCase(HEADER_COOKIE)) {
                continue;
            }
            if (!header.equalsIgnoreCase(HEADER_HOST) && 
                !header.equalsIgnoreCase(HEADER_CONTENT_LENGTH) && 
                !header.equalsIgnoreCase(HEADER_REFERER) ) {
                urlCon.setRequestProperty(header, req.getHeader(header));
            }
        }
    }

    public static void prepareResponse(HttpServletRequest req, HttpServletResponse res, HttpURLConnection urlCon) {    
        prepareResponse(req, res, urlCon, false);
    }

    /**
     * Sets the headers received from the response of an HttpURLConnection to an actual response.
     * Enabling boolean setCookies sets the received cookies as well.
     * 
     * @param req
     * @param res
     * @param urlCon
     * @param setCookies
     */
    public static void prepareResponse(HttpServletRequest req, HttpServletResponse res, HttpURLConnection urlCon, boolean setCookies) {    
        String headerName=null;
        StringBuilder sb = new StringBuilder();
        sb.append("Connection response headers:\n");
        for (int i=1; (headerName = urlCon.getHeaderFieldKey(i))!=null; i++) {
            sb.append(" "+headerName+"="+urlCon.getHeaderField(i)+"\n");
            if (headerName.equalsIgnoreCase(HEADER_SET_COOKIE) && setCookies) {                  
                String cookieStr = urlCon.getHeaderField(i);
                /*
                //replace eventual references in the path -value of the cookie
                Pattern p = Pattern.compile("/discoverer");
                String replaceStr = req.getContextPath() + ProxyConfig.PROXYFILTER_PATH + "/discoverer";
                cookieStr = p.matcher(cookieStr).replaceAll(replaceStr);
                */
                res.addHeader(HEADER_SET_COOKIE, cookieStr);
            }
            
            else if (!headerName.equalsIgnoreCase(HEADER_SET_COOKIE) && !headerName.equalsIgnoreCase(HEADER_CONTENT_LENGTH) &&
                    !headerName.equalsIgnoreCase(HEADER_CONTENT_LOCATION) && !headerName.equalsIgnoreCase(HEADER_TRANSFER_ENCODING)) {
                String headerStr = urlCon.getHeaderField(i);
                if (headerStr != null && !headerStr.equals("") && !res.containsHeader(headerStr)) {
                    res.setHeader(headerName, headerStr);
                }
            }
        }
        LOG.debug(sb.toString());
    }

    /**
     * Stream the contents from an inputstream to an outputstream.
     * 
     * @param is
     * @param os
     */
    public static void streamContents(InputStream is, OutputStream os) throws IOException {
        /*Stream the contents from an inputstream to an outputstream*/
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
                        
        try {
            while((n=is.read(buf, 0, BUFFER_SIZE)) != -1 ) {
                os.write(buf, 0, n);
            }
        }
        catch (IOException e) {
            throw new ProxyFilterException(e);
        }
        finally {
            is.close();
            os.flush();
            os.close();
        }
    }
    
    /**
     * Stream the contents received from an inputstream to an outputstream doing some filtering
     * on the content if its type is text/html.
     * 
     * @param is
     * @param os
     * @param req
     * @param res
     */
    public static void filterContents(InputStream is, OutputStream os, HttpServletRequest req, HttpServletResponse res, ProxyConfig proxyConfig)
        throws IOException {
				//Stream the contents received from the serverconnection forward to the client doing some filtering
        
				String contentType = getContentType(req, res, proxyConfig);
				
				LOG.debug("filterContents(): Content type is " + contentType);
				
				//if (contentType == null) return;
        if (isFilterContentType(contentType, proxyConfig)) {
            BufferedReader reader = new ProxyFilterReader(new InputStreamReader(is), proxyConfig);
            String ln;
            try {
            	while((ln=reader.readLine()) != null) {
            		os.write(ln.getBytes());
            	}
            }
						catch(Exception e) {
							LOG.debug("Unable to read line! Exception:" + e);
							is.reset();
							streamContents(is, os);
						}
            finally {
                reader.close();
                os.flush();
                os.close();
            }
        } 
        //do not read line-by-line or filter anything if content other than text/html
        else {
            streamContents(is, os);
        }        
    }

		/**
		* Gets content type for the contents either by setting it based on url suffices defined in proxyConfig, or
		* by reading it from the HttpServletResponse.
		*
		*/
		private static String getContentType(HttpServletRequest req, HttpServletResponse res, ProxyConfig proxyConfig) {
			String requestURI = req.getRequestURI();
			Map<String, String> guessContentTypes = proxyConfig.getGuessContentTypes();
        for (String fileSuffix: (String[]) guessContentTypes.keySet().toArray(new String[guessContentTypes.size()])) {
            if (requestURI.endsWith(fileSuffix)) {
							return guessContentTypes.get(fileSuffix);
						}
        }
			return res.getContentType(); 
		}
		
    /**
     * Checks if the given content type is to be filtered. The content types to be processed
     * by the filter are specified in the proxy configuration.
     * 
     * @param contentType
     */
    private static boolean isFilterContentType(String contentType, ProxyConfig proxyConfig) {
    	if (contentType == null) {
    		contentType = ""; // No content type equals empty string in filterConfigTypes
    	}
		List<String> filterContentTypes = proxyConfig.getFilterContentTypes();
    	for (String filterContentType: filterContentTypes) {
    		if (contentType.toLowerCase().startsWith(filterContentType)) {
    			return true;    			
    		}
    	}
    	return false;
    }
    
    /**
     * Executes a redirect preserving the cookies set by the response.
     * 
     * @param urlCon
     */
    public static HttpURLConnection executeRedirect(HttpURLConnection urlCon) throws IOException{
        //executes the redirect preserving the cookies set by the response
        if (urlCon.getHeaderField(HEADER_LOCATION) == null)  {
        	return urlCon;
        }
        LOG.debug("Redirecting to: "+urlCon.getHeaderField(HEADER_LOCATION));
        URL redirectUrl = new URL(urlCon.getHeaderField(HEADER_LOCATION));
        
        HttpURLConnection newCon = (HttpURLConnection) redirectUrl.openConnection();
        newCon.setInstanceFollowRedirects(false);
        if (urlCon.getRequestProperty(HEADER_USER_AGENT) != null) {
        	newCon.setRequestProperty(HEADER_USER_AGENT, urlCon.getRequestProperty(HEADER_USER_AGENT));        
        }
        String headerName=null;
        for (int i=1; (headerName = urlCon.getHeaderFieldKey(i))!=null; i++) {
            if (headerName.equalsIgnoreCase(HEADER_SET_COOKIE)) {                  
                String cookieStr = urlCon.getHeaderField(i);
                cookieStr = cookieStr.substring(0,cookieStr.indexOf(';'));
                newCon.addRequestProperty(HEADER_COOKIE, cookieStr);
            }
        }
        newCon.connect();
        //check further redirects
        if ((newCon.getResponseCode() >= 300) && (newCon.getResponseCode() < 400)) {
            newCon = executeRedirect(newCon);
        }        
        return newCon;
    }

    /**
     * Sets the given cookies to the request of an HttpURLConnection. The cookies together with their values
     * should be given as a string in the form: cookie1=value1;cookie2=value2..
     * 
     * @param urlCon
     * @param cookies
     */
    public static void setConnectionCookies(HttpURLConnection urlCon, String cookies) {
        urlCon.setRequestProperty(HEADER_COOKIE, cookies);
        LOG.debug("Connection request cookies set to: "+cookies);
    }

    /**
     * Returns a string with all cookies received from the response of an HttpURLConnection.
     * 
     * @param urlCon
     */
    public static String getReceivedCookies(HttpURLConnection urlCon) {
        String headerName=null;
        String cookies = "";
        for (int i=1; (headerName = urlCon.getHeaderFieldKey(i))!=null; i++) {
            if (headerName.equalsIgnoreCase(HEADER_SET_COOKIE)) {                  
                String cookieStr = urlCon.getHeaderField(i);
                if (cookieStr.indexOf(';') != -1) {
                	cookieStr = cookieStr.substring(0,cookieStr.indexOf(';'));
                }
                cookies += cookieStr;
                if (urlCon.getHeaderFieldKey(i+1) != null){
                	cookies += ";";
                }
            }
        }
        LOG.debug("Received cookies: "+cookies);
        return cookies;
    }

}
