package fi.arcusys.koku.palvelut.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.web.context.support.ServletContextResource;

/**
 * Filter used to secure transmission between the portal and intalio's XFormsManager.
 * 
 * @author Jon Haikarainen
 */

public class ProxyFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(ProxyFilter.class);
    private static final boolean DEBUG = true;
    
    private static final String PROXY_STORED_COOKIES = "proxyFilter_storedCookies";
    
    private FilterConfig _filterConfig = null;
		private ProxyConfig _proxyConfig = null;
		
    public void init(FilterConfig filterConfig) throws ServletException {
        _filterConfig = filterConfig;
				
				String proxyConfigName = filterConfig.getInitParameter("proxyConfigName");
				String proxyConfigLocation = filterConfig.getInitParameter("proxyConfigLocation");
				
				if (proxyConfigName == null || proxyConfigLocation == null) {
					throw new ServletException("ProxyFilter requires proxyConfigName and proxyConfigLocation as init parameters.");
				}
				_proxyConfig = getProxyConfig(proxyConfigName, proxyConfigLocation);
    }

    public void destroy() {
        _filterConfig = null;
				_proxyConfig = null;
    }

    /**
     * The main filtering method.
     * -creates the corresponding connection to the target server
     * -changes referenses to the target server to point to the proxyFilter
     * @param request
     * @param response
     * @param chain
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)){
        	return;
        }
        
        HttpServletRequest  req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        
        if (_proxyConfig.isIgnored(req.getRequestURI())) {
            LOG.debug("Skip url: " + req.getRequestURI());
            chain.doFilter(req, res);
            return;
        }
            
        LOG.debug("FILTERING..............");
        LOG.debug("Request URL: "+new String(req.getRequestURL())+ " ___ " + req.getQueryString());
        
        if (req.getQueryString()==null&&isInit(new String(req.getRequestURL()))) {
        	logAction(req, "Käyttäjä_lähetti_lomakkeen");
        }        
              
        if(DEBUG) {
        	dumpRequestHeaders(req);
        }
        
        HttpURLConnection urlCon = createServerConnection(req);

        if (_proxyConfig.isStoreCookies()) {
        	String receivedCookies = ConnectionHelper.getReceivedCookies(urlCon);
        	HttpSession session = req.getSession();
        	
        	session.setAttribute(PROXY_STORED_COOKIES, receivedCookies);
        }
        ConnectionHelper.prepareResponse(req, res, urlCon);
        res.setStatus(urlCon.getResponseCode());
        ConnectionHelper.filterContents(urlCon.getInputStream(), res.getOutputStream(), req, res, this._proxyConfig);

        urlCon.disconnect();
    }
		
    @SuppressWarnings("unchecked")
	private void dumpRequestHeaders(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Request headers:\n");
        for(Enumeration e = req.getHeaderNames(); e.hasMoreElements(); ) {
            String header = (String)e.nextElement();
            sb.append(" " + header + "=" + req.getHeader(header) + "\n");
        }
        LOG.debug(sb.toString());
    }
    
    @SuppressWarnings("unchecked")
	private void logAction(HttpServletRequest req, String msg) {
    	String userId = "";
    	String formName = "";
    	for (Enumeration e = req.getHeaderNames(); e.hasMoreElements();) {
    		String header = (String)e.nextElement();
    		if (header.contains("referer")) {
    			String referer = req.getHeader(header);     			
    			userId = parseUser(referer);
    			formName = parseFormName(referer);
    		}
    	}
    	String customerId = _filterConfig.getServletContext().getInitParameter("loggingCustomer");
    	String applicationId = _filterConfig.getServletContext().getInitParameter("loggingApplication");
    	String message = customerId+" "+applicationId+" "+userId+" "+msg +"_" +formName;
    	LOG.debug("MESSAGE: " + message);
    }    
    
    /**
     * Parse user from request header. Different reg exp is used depending on if the 
     * the form is opened or sent. 
     * @param referer Request header 
     * @return userId
     */
    private String parseUser(String referer) {
    	String[] splitted = referer.split("user=kuntalainen%2F");    	
    	String user = splitted[1];
    	LOG.debug("USER: "+user);
    	return user;    	
    }
    
    /**
     * Parse form name from request header. Different reg exp is used depending on if the 
     * the form is opened or sent. 
     * @param referer Request header 
     * @return userId
     */
    private String parseFormName(String referer) {
    	String[] splitted = referer.split("forms%2F");
    	String[] splitted2 = splitted[1].split(".xform");
    	String formName = splitted2[0];
    	LOG.debug("FORMNAME: "+formName);
    	return formName;
    }
    
    private boolean isInit(String requestURL) {
    	String[] splitted = requestURL.split("xforms/");    	
    	if (splitted[1].equals("init")) {
    		return true;
    	}
    	return false;
    }
    

    /**
     * Creates a new connection to the actual report server based on the original request to the filter.
     * All basic headers are forwarded to the new connection and the cookies received from the report server are restored 
     * and added to the request of the new connection. In case the server replies with a redirect, all redirects that
     * follow are executed.
     * @param req
     */
    private HttpURLConnection createServerConnection(HttpServletRequest req) throws IOException {
			String urlStr = _proxyConfig.getFullPath() +
					req.getRequestURI().replaceFirst(_proxyConfig.getProxyFilterPath(), "") + 
					(req.getQueryString() != null ? "?" + req.getQueryString() : "");
			URL url = new URL(urlStr);
			LOG.debug("Trying to connect to: "+url.toString());
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
			urlCon.setInstanceFollowRedirects(false);
			ConnectionHelper.prepareConnectionRequest(req, urlCon);
			if (_proxyConfig.isStoreCookies()) {
				HttpSession session = req.getSession();
				String storedCookies = (String) session.getAttribute(PROXY_STORED_COOKIES);
				if (storedCookies != null) {
					ConnectionHelper.setConnectionCookies(urlCon, storedCookies);
				}
			}
			if(!"GET".equalsIgnoreCase(req.getMethod())) {
					urlCon.setDoOutput(true);
					urlCon.setRequestMethod(req.getMethod());
					LOG.debug("POST used.");
					InputStream is = req.getInputStream();
					OutputStream os = urlCon.getOutputStream();
					ConnectionHelper.streamContents(is, os);
					os.close();
			}
			urlCon.connect();
			
			//check redirects
			if ((urlCon.getResponseCode() >= 300) && (urlCon.getResponseCode() < 400)) {
					//redirect codes = 3xx
					urlCon = ConnectionHelper.executeRedirect(urlCon);
			}
			return urlCon;
    }
		
		private ProxyConfig getProxyConfig(String proxyConfigName, String proxyConfigLocation) {
			ServletContext context = this._filterConfig.getServletContext();
			ServletContextResource resource = new ServletContextResource(context, proxyConfigLocation);
			XmlBeanFactory beanFactory = new XmlBeanFactory(resource);
			return (ProxyConfig)beanFactory.getBean(proxyConfigName);
		}

}