package fi.arcusys.koku.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Koku common properties
 * 
 * 
 * @deprecated Use {@link KokuPropertiesUtil}
 * 
 * @author Toni Turunen
 */

public class PropertiesUtil {
	
	private PropertiesUtil() { /* Not instantiable */ }
	
	private final static Logger LOG = Logger.getLogger(PropertiesUtil.class);
	private final static Properties PROPERTIES = new Properties();
	private final static String FILENAME = "koku-portlets.properties";
	// JBoss properties http://community.jboss.org/wiki/JBossProperties
	// GateIn seems to be using same properties as jboss
	private final static String PATH_PROPERTY = "jboss.server.home.dir";
	private final static String CONFIG_DIR = "/conf/";

	
	static {
		String path = System.getProperty(PATH_PROPERTY);		
		if (path == null) {
			LOG.error("Coulnd't find System.properties key: '" + PATH_PROPERTY + "'!");
		} else {
			path += CONFIG_DIR;
			FileInputStream in = null;
			try {
					in = new FileInputStream(path + FILENAME);
					PROPERTIES.load(in);
					in.close();
					LOG.info("Koku properties loaded successfully!");
			} catch (FileNotFoundException e) {
				LOG.error("File '"+ path + FILENAME+"' not found! Portlets can't connect to WebServices!");
			} catch (SecurityException se) {
				LOG.error("Coulnd't read properties file. Access denied. Portlets can't connect to WebServices! ", se);
			} catch (IOException e) {
				LOG.error("Error while reading '"+FILENAME+"'. Please check that file is not corrupted. ");
			}
		}
	}

	public static int size() {
		return PROPERTIES.size();
	}

	public static boolean isEmpty() {
		return PROPERTIES.isEmpty();
	}

	public static Enumeration<Object> keys() {
		return PROPERTIES.keys();
	}

	public static Enumeration<Object> elements() {
		return PROPERTIES.elements();
	}

	public static boolean contains(Object value) {
		return PROPERTIES.contains(value);
	}

	public static boolean containsValue(Object value) {
		return PROPERTIES.containsValue(value);
	}

	public static boolean containsKey(Object key) {
		return PROPERTIES.containsKey(key);
	}

	public static Object get(Object key) {
		return PROPERTIES.get(key);
	}

	public static Set<Object> keySet() {
		return PROPERTIES.keySet();
	}

	public static Set<Entry<Object, Object>> entrySet() {
		return PROPERTIES.entrySet();
	}

	public static Collection<Object> values() {
		return PROPERTIES.values();
	}

	public static String getProperty(String key) {
		return PROPERTIES.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return PROPERTIES.getProperty(key, defaultValue);
	}

}
