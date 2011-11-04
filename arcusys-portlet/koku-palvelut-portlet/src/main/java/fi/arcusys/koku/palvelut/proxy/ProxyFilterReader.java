package fi.arcusys.koku.palvelut.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Custom BufferedReader used by the filter to replace certain parts in the response.
 * 
 * @author Jon Haikarainen
 */

public class ProxyFilterReader extends BufferedReader{

    private static final Log logger = LogFactory.getLog(ProxyFilterReader.class);

		private ProxyConfig _proxyConfig;
		
    public ProxyFilterReader(InputStreamReader isr, ProxyConfig proxyConfig) {
        super(isr);
				this._proxyConfig = proxyConfig;
    }

    /**
     * Reads a line from the input replacing the matches found in the replaceMap -attribute of this class.
     * 
     * @return A line of text with matches replaced
     */
    public String readLine() throws IOException{
        String ln;
        try {
            ln = super.readLine();
        } catch (IOException e) {
            throw new ProxyFilterException(e);
        }
        if (ln != null) {
        	ln = replaceMatches(ln) + '\n';
        }
        return ln;
    }

    /**
     * Searches the given string for matches according to the keys specified in the replaceMap -attribute
     * of this class. Each match is replaced with its corresponding replace value.
     * 
     * @param str
     */
    private String replaceMatches(String str){
				logger.debug("replaceMatches(): string length is " + str.length());
        // TODO: this should be changed if problems appear due to too long lines. In our test environment
        // the max length of the lines varies between 36k and 52k
        String res = str;
        Map<Pattern, String> replaceMap = _proxyConfig.getReplaceMap();
        for (Pattern matchRegEx: (Pattern[]) replaceMap.keySet().toArray(new Pattern[replaceMap.size()])) {
            String targetRegEx = replaceMap.get(matchRegEx);
            Matcher matcher = matchRegEx.matcher(res);
            res = matcher.replaceAll(targetRegEx);
        }
        return res;
    }

}
