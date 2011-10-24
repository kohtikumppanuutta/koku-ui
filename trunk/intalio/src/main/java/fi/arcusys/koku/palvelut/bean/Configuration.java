package fi.arcusys.koku.palvelut.bean;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.ListenerManager;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

public class Configuration {

	private static Configuration instance = new Configuration();

	private String tokenServiceEndpoint;
	private String taskManagerServiceEndpoint;

	/*
	 * private int _pagingLength; private int _refreshTime = 5; private int
	 * _sessionTimeout = 10; private String _baseUrl; private String _feedUrl;
	 */

	private Configuration() {
		// dkudinov: hack for fixing 'Connection timeout' issue when Intalio
		// uses axis2 to access web services
		ConfigurationContext configurationContext = ListenerManager.defaultConfigurationContext;
		if (configurationContext == null) {
			try {
				configurationContext = ConfigurationContextFactory
						.createConfigurationContextFromFileSystem(null, null);
			} catch (final AxisFault e) {
				throw new RuntimeException(e);
			}
			ListenerManager.defaultConfigurationContext = configurationContext;
		}
		final HttpClient cachedClient = (HttpClient) configurationContext
				.getProperty(HTTPConstants.CACHED_HTTP_CLIENT);
		if (cachedClient != null) {
			cachedClient
					.getHttpConnectionManager()
					.getParams()
					.setDefaultMaxConnectionsPerHost(
							MultiThreadedHttpConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS);
		} else {
			MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();

			HttpConnectionManagerParams params = new HttpConnectionManagerParams();
			params.setDefaultMaxConnectionsPerHost(20);
			multiThreadedHttpConnectionManager.setParams(params);
			HttpClient httpClient = new HttpClient(
					multiThreadedHttpConnectionManager);
			configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT,
					httpClient);
		}
	}

	public static Configuration getInstance() {
		return instance;
	}

	public String getTokenServiceEndpoint() {
		return tokenServiceEndpoint;
	}

	public void setTokenServiceEndpoint(String tokenServiceEndpoint) {
		this.tokenServiceEndpoint = tokenServiceEndpoint;
	}

	public String getTaskManagerServiceEndpoint() {
		return taskManagerServiceEndpoint;
	}

	public void setTaskManagerServiceEndpoint(String taskManagerServiceEndpoint) {
		this.taskManagerServiceEndpoint = taskManagerServiceEndpoint;
	}
}
