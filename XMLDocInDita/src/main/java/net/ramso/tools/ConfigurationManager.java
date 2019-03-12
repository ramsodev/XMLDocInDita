package net.ramso.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Gestion la configuración de una aplicación
 *
 * @author jescudero
 * @author cjrequena
 *
 */

public class ConfigurationManager {
	/**
	 * Tipos admitido de log para configurar
	 *
	 * @author jescudero
	 *
	 */
	public enum LOG_TYPES {
		jdk, log4j, logback, simple
	}

	/**
	 * Nombre del fichero de configuración
	 */
	public static String PROPERTIESNAME = "configuration.properties";
	/**
	 * Propiedad del fichero de configuración del log
	 */
	public static String FILE_CONF_LOG = "file.conf.log";

	/**
	 * Propiedad con el nombre del log
	 */
	public static String LOG_NAME = "log.name";

	/**
	 * Tipo de log a usar en la aplicación
	 */
	public static String LOG_TYPE = "log.type";

	/**
	 * Propieda con el path del directorio de configuración
	 */
	public static String CONF_DIR = null;;

	/**
	 * 
	 */
	final static protected Properties defaults = new Properties();
	/**
	 * 
	 */
	final static protected Properties properties = new Properties(defaults);

	/**
	 * Obtiene la propiedad y la convierte a boolean si la propiedad no existe
	 * retorna false
	 * @param propName
	 * @return
	 */
	final static public boolean getBooleanProperty(String propName) {
		return getBooleanProperty(propName, false);
	}

	/**
	 * Obtiene la propiedad y la convierte a boolean si la propiedad no existe
	 * retorna defaultValue
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public boolean getBooleanProperty(String propName, boolean defaultValue) {
		try {
			return Boolean.valueOf(properties.getProperty(propName, Boolean.toString(defaultValue))).booleanValue();
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Obtiene la propiedad y la convierte a byte si la propiedad no existe retorna
	 * (byte)0
	 * @param propName
	 * @return
	 */
	final static public byte getByteProperty(String propName) {
		return getByteProperty(propName, (byte) 0);
	}

	/**
	 * Obtiene la propiedad y la convierte a byte si la propiedad no existe retorna
	 * defaultValue
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public byte getByteProperty(String propName, byte defaultValue) {
		try {
			return Byte.parseByte(properties.getProperty(propName));
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Obtiene la propiedad y la convierte a double si la propiedad no existe
	 * retorna (short)0
	 * @param propName
	 * @return
	 */
	final static public double getDoubleProperty(String propName) {
		return getDoubleProperty(propName, 0.0);
	}

	/**
	 * Obtiene la propiedad y la convierte a double si la propiedad no existe
	 * retorna defaultValue
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public double getDoubleProperty(String propName, double defaultValue) {
		try {
			return Double.parseDouble(properties.getProperty(propName));
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Obtiene la propiedad y la convierte a double si la propiedad no existe
	 * retorna (short)0
	 * @param propName
	 * @return
	 */
	final static public float getFloatProperty(String propName) {
		return getFloatProperty(propName, (float) 0.0);
	}

	/**
	 * Obtiene la propiedad y la convierte a double si la propiedad no existe
	 * retorna defaultValue
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public float getFloatProperty(String propName, float defaultValue) {
		try {
			return Float.parseFloat(properties.getProperty(propName));
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Obtiene la propiedad y la convierte a int si la propiedad no existe retorna
	 * (int)0
	 * @param propName
	 * @return
	 */
	final static public int getIntProperty(String propName) {
		return getIntProperty(propName, 0);
	}

	/**
	 * Obtiene la propiedad y la convierte a int si la propiedad no existe retorna
	 * defaultValue
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public int getIntProperty(String propName, int defaultValue) {
		try {
			return Integer.parseInt(properties.getProperty(propName));
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Obtiene la propiedad y la convierte a long si la propiedad no existe retorna
	 * (long)0
	 * @param propName
	 * @return
	 */
	final static public long getLongProperty(String propName) {
		return getLongProperty(propName, 0L);
	}

	/**
	 * Obtiene la propiedad y la convierte a short si la propiedad no existe retorna
	 * defaultValue
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public long getLongProperty(String propName, long defaultValue) {
		try {
			return Long.parseLong(properties.getProperty(propName));
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * @param propName
	 * @return
	 */
	final static public String getPassword(String propName) {
		String pass = properties.getProperty(propName);
		if (PasswordManager.IsEncrypted(pass)) {
			try {
				return PasswordManager.decrypt(pass);
			} catch (final Exception e) {
				LogManager.error("Error al desencriptar password", e);
				pass = "";
			}
		} else {
			try {
				setProperty(propName, PasswordManager.encrypt(pass));
				save();
			} catch (final Exception e) {
				LogManager.error("Error al encriptar password", e);
			}
		}
		return pass;
	}

	/**
	 * @param prefix
	 * @return
	 */
	final static public Properties getProperties(String prefix) {
		final Properties prop = new Properties();
		for (final Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
			final String name = (String) e.nextElement();
			if ((name).startsWith(prefix)) {
				prop.put(name, properties.get(name));
			}
		}
		return prop;
	}

	/** Obtiene una Propiedad como un String si no Existe devuelve "" 
	 * @param key
	 * @return
	 */
	final public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	/** Obtiene una Propiedad como un String sino su valor por defecto 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	final public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Obtiene la propiedad y la convierte a short si la propiedad no existe retorna
	 * (short)0
	 
	 * @param propName
	 * @return
	 */
	final static public short getShortProperty(String propName) {
		return getShortProperty(propName, (short) 0);
	}

	/**
	 * Obtiene la propiedad y la convierte a short si la propiedad no existe retorna
	 * defaultValue
	 * 
	 * @param propName
	 * @param defaultValue
	 * @return
	 */
	final static public short getShortProperty(String propName, short defaultValue) {
		try {
			return Short.parseShort(properties.getProperty(propName));
		} catch (final Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Metodo de inicialición del gestor de configuraciones. Configura el log y la
	 * base de datos en base a los parametros recibidos
	 *
	 * @throws ConfigurationException
	 */
	public static void init() throws ConfigurationException {
		String path = "";
		boolean inCP = true;
		if (CONF_DIR != null) {
			path = CONF_DIR;
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}
			inCP = false;
		}
		if (!load(inCP, path + PROPERTIESNAME))
			throw new ConfigurationException("Configuration properties " + path + PROPERTIESNAME + " not found");
		final String name = getProperty(LOG_NAME);
		final LOG_TYPES logtype = LOG_TYPES.valueOf(getProperty(LOG_TYPE));
		LogManager.init(name, logtype, path + getProperty(FILE_CONF_LOG), inCP);
		LogManager.info("Configuration roperties loaded correctly");
		LogManager.info("Aplication initialized");
	}

	public static boolean load(boolean inCp, String file) {
		boolean result = false;
		if (inCp) {
			result = load(FileTools.getStream(file));
		} else {
			result = load(new File(file));
		}
		return result;
	}

	final static boolean load(File file) {
		boolean result = true;
		FileInputStream propsFile = null;
		try {
			propsFile = new FileInputStream(file);
			properties.load(propsFile);
		} catch (final Exception e) {
			result = false;
			// Ignoramos las
		} finally {
			try {
				propsFile.close();
			} catch (final Exception e) {
				// ignoramos
			}
		}
		return result;
	}

	@SuppressWarnings("null")
	final static boolean load(FileReader file) {
		boolean result = true;
		final FileInputStream propsFile = null;
		try {
			properties.load(file);
		} catch (final Exception e) {
			result = false;
			// Ignoramos las
		} finally {
			try {
				propsFile.close();
			} catch (final Exception e) {
				// ignoramos
			}
		}
		return result;
	}

	final static boolean load(InputStream propsFile) {
		boolean result = true;
		// FileInputStream propsFile = null;
		try {
			// propsFile = new FileInputStream(file);
			properties.load(propsFile);
		} catch (final Exception e) {
			result = false;
			// Ignoramos las
		} finally {
			try {
				propsFile.close();
			} catch (final Exception e) {
				// ignoramos
			}
		}
		return result;
	}

	final static public void putdefault(Object key, Object value) {
		defaults.put(key, value);
	}

	final static public void save() throws FileNotFoundException, IOException {
		String path = "";
		if (CONF_DIR != null) {
			path = CONF_DIR;
			if (!path.endsWith(File.separator)) {
				path += File.separator;
			}
		}
		properties.store(new FileOutputStream(path + PROPERTIESNAME), "Modificado ");

	}

	final static public void setProperty(String propName, String value) {
		properties.setProperty(propName, value);

	}
}