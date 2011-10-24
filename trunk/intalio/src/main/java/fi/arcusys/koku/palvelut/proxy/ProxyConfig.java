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

/*
 * Configuration class for the proxy filter. Can be initialized for example as a Spring bean.
 * 
 * @author Jon Haikarainen
 */
public class ProxyConfig {

	// Singleton pattern removed by Tuomas.
	//private static ProxyConfig instance = new ProxyConfig();
	
	private static final Log log = LogFactory.getLog(ProxyConfig.class);
	
	private String proxyFilterPath = "/veera/xforms";
	private String targetPath = "http://intalio:8080/xFormsManager";
	private Map<Pattern, String> replaceMap = new HashMap<Pattern, String>();
	private boolean storeCookies = false;
	private List<String> filterContentTypes = new ArrayList<String>();
	private Map<String, String> guessContentTypes = new HashMap<String, String>();
	
	private Set<String> ignorePaths = new HashSet<String>();
	
	private ProxyConfig() {
	}
	
	public static ProxyConfig getInstance() {
		log.debug("getInstance()");
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
		log.debug("getProxyFilterPath(): " + proxyFilterPath);
		return proxyFilterPath;
	}

	public void setProxyFilterPath(String proxyFilterPath) {
	  log.debug("setProxyFilterPath(): " + proxyFilterPath);
		this.proxyFilterPath = proxyFilterPath;
	}

	public String getTargetPath() {
		log.debug("getTargetPath(): " + targetPath);
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		log.debug("setTargetPath(): " + targetPath);
		this.targetPath = targetPath;
	}

	public Map<Pattern, String> getReplaceMap() {
		log.debug("getReplaceMap(): " + replaceMap);
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
						
						log.debug("setReplaceMap(): " + matchRegEx + " -> " + targetRegEx);
						
            Pattern pattern = Pattern.compile(matchRegEx);
            this.replaceMap.put(pattern, targetRegEx);
        }
	}
	
	public boolean isStoreCookies() {
		log.debug("isStoreCookies(): " + storeCookies);
		return storeCookies;
	}

	public void setStoreCookies(boolean storeCookies) {
		log.debug("setStoreCookies(): " + storeCookies);
		this.storeCookies = storeCookies;
	}

	public List<String> getFilterContentTypes() {
		log.debug("getFilterContentTypes(): " + filterContentTypes);
		return filterContentTypes;
	}

	/*
	 * The list filterContentTypes is used to specify the content types to be processed
	 * by the filter, for example "text/html".
	 * 
	 *  @param filterContentTypes
	 */
	public void setFilterContentTypes(List<String> filterContentTypes) {
		log.debug("setFilterContentTypes(): " + filterContentTypes);
		this.filterContentTypes = filterContentTypes;
	}
	
	public Map<String, String> getGuessContentTypes() {
		log.debug("getGuessContentTypes(): " + guessContentTypes);
		return guessContentTypes;
	}

	/*
	 * The mapping from file types to guessed content types. For example files with the suffix, ".html"
	 * may be set to be processed as content types of "text/html"
	 * 
	 *  @param guessContentTypes
	 */
	public void setGuessContentTypes(Map<String, String> guessContentTypes) {
		log.debug("setGuessContentTypes(): " + guessContentTypes);
		this.guessContentTypes = guessContentTypes;
	}
	
}

