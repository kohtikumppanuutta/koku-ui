package fi.arcusys.koku.palvelut.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fi.koku.settings.KoKuPropertiesUtil;

/*
 * Configuration class for the proxy filter. Can be initialized for example as a Spring bean.
 * 
 * @author Jon Haikarainen
 */
public class ProxyConfig {

	// Singleton pattern removed by Tuomas.
	//private static ProxyConfig instance = new ProxyConfig();
	
	private static final String PROXY_URL;
	private static final Log LOG = LogFactory.getLog(ProxyConfig.class);
	
	static {		
		PROXY_URL = KoKuPropertiesUtil.get("palvelutPortlet.proxyUrl");
		if (PROXY_URL == null) {
			throw new ExceptionInInitializerError("Coulnd't find properties 'palvelutPortlet.proxyUrl': "+PROXY_URL);
		}
		LOG.info("palvelutPortlet.proxyUrl: "+PROXY_URL);
	}
	
	private String proxyFilterPath = "/veera/xforms";
	private String targetPath = "/xFormsManager";
	private Map<Pattern, String> replaceMap = new HashMap<Pattern, String>();
	private boolean storeCookies = false;
	private List<String> filterContentTypes = new ArrayList<String>();
	private Map<String, String> guessContentTypes = new HashMap<String, String>();
	private Set<String> ignorePaths = new HashSet<String>();
	
	private ProxyConfig() {
	}
	
	public static ProxyConfig getInstance() {
		LOG.debug("getInstance()");
		return new ProxyConfig();
	}
	
    /**
     * @return the ignorePaths
     */
    public boolean isIgnored(final String path) {
        return ignorePaths.contains(path);
    }

    /**
     * @param ignorePaths the ignorePaths to set
     */
    public void setIgnorePaths(List<String> ignorePaths) {
        this.ignorePaths.clear();
        this.ignorePaths.addAll(ignorePaths);
    }

    public String getProxyFilterPath() {
		LOG.debug("getProxyFilterPath(): " + proxyFilterPath);
		return proxyFilterPath;
	}

	public void setProxyFilterPath(String proxyFilterPath) {
	  LOG.debug("setProxyFilterPath(): " + proxyFilterPath);
		this.proxyFilterPath = proxyFilterPath;
	}

	public String getTargetPath() {
		LOG.debug("getTargetPath(): " + targetPath);
		return targetPath;
	}
	
	public String getProxyUrl() {
		return PROXY_URL;
	}
	
	public String getFullPath() {
		return PROXY_URL + targetPath;
	}

	public void setTargetPath(String targetPath) {
		LOG.debug("setTargetPath(): " + targetPath);
		this.targetPath = targetPath;
	}

	public Map<Pattern, String> getReplaceMap() {
		LOG.debug("getReplaceMap(): " + replaceMap);
    return replaceMap;
  }

	/*
	 * The replaceMap is a map between regular expressions to be searched for and
	 * the expressions to replace them. The filter replaces all occurrences according
	 * to what is specified in the map.
	 * 
	 * @param replaceMap
	 */
	public void setReplaceMap(Map<String, String> replaceMap) {
        this.replaceMap = new HashMap<Pattern, String>();
        for (String matchRegEx: (String[]) replaceMap.keySet().toArray(new String[replaceMap.size()])) {
						String targetRegEx = replaceMap.get(matchRegEx);
						
						LOG.debug("setReplaceMap(): " + matchRegEx + " -> " + targetRegEx);
						
            Pattern pattern = Pattern.compile(matchRegEx);
            this.replaceMap.put(pattern, targetRegEx);
        }
	}
	
	public boolean isStoreCookies() {
		LOG.debug("isStoreCookies(): " + storeCookies);
		return storeCookies;
	}

	public void setStoreCookies(boolean storeCookies) {
		LOG.debug("setStoreCookies(): " + storeCookies);
		this.storeCookies = storeCookies;
	}

	public List<String> getFilterContentTypes() {
		LOG.debug("getFilterContentTypes(): " + filterContentTypes);
		return filterContentTypes;
	}

	/*
	 * The list filterContentTypes is used to specify the content types to be processed
	 * by the filter, for example "text/html".
	 * 
	 *  @param filterContentTypes
	 */
	public void setFilterContentTypes(List<String> filterContentTypes) {
		LOG.debug("setFilterContentTypes(): " + filterContentTypes);
		this.filterContentTypes = filterContentTypes;
	}
	
	public Map<String, String> getGuessContentTypes() {
		LOG.debug("getGuessContentTypes(): " + guessContentTypes);
		return guessContentTypes;
	}

	/*
	 * The mapping from file types to guessed content types. For example files with the suffix, ".html"
	 * may be set to be processed as content types of "text/html"
	 * 
	 *  @param guessContentTypes
	 */
	public void setGuessContentTypes(Map<String, String> guessContentTypes) {
		LOG.debug("setGuessContentTypes(): " + guessContentTypes);
		this.guessContentTypes = guessContentTypes;
	}
	
}

