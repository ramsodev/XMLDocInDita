package net.ramso.docindita.db.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import net.ramso.docindita.db.Config;

public abstract class BaseTest {
	public static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	public static String protocol = "jdbc:derby:";
	public static String db = "DB/BirtSample";
	private static Validator topicValidator;
	private static Validator bookmapValidator;

	protected static Connection getConnection()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final String path = Thread.currentThread().getContextClassLoader().getResource(db).getPath();
		Class.forName(driver).getDeclaredConstructor().newInstance();
		return DriverManager.getConnection(protocol + path);
	}

	protected void clean() {
		clean(Config.getOutputDir());
	}

	protected void clean(String path) {
		final File dir = new File(path);
		if (dir.exists()) {
			for (final File file : dir.listFiles()) {
				if (file.isDirectory()) {
					clean(file.getAbsolutePath());
				}
				file.delete();
			}
		}
	}

	protected boolean valid() throws MalformedURLException {
		boolean valid = true;
		final File files = new File(Config.getOutputDir());
		for (final File file : files.listFiles()) {
			if (file.getAbsolutePath().endsWith(".dita")) {
				valid = validateXMLSchema(getTopicValidator(), file);
				if (!valid) {
					break;
				}
			} else if (file.getAbsolutePath().endsWith(".ditamap")) {
				valid = validateXMLSchema(getBookmapValidator(), file);
				if (!valid) {
					break;
				}
			}
		}
		return valid;
	}

	private Validator getTopicValidator() throws MalformedURLException {
		if (topicValidator == null) {
			final URL XSD_TOPIC = new URL(
					"http://docs.oasis-open.org/dita/v1.2/os/DITA1.2-xsds/xsd1.2-url/technicalContent/xsd/topic.xsd");
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema;
			try {
				schema = factory.newSchema(XSD_TOPIC);
				topicValidator = schema.newValidator();
			} catch (final SAXException e) {
				e.printStackTrace();
			}
		}
		return topicValidator;
	}

	private Validator getBookmapValidator() throws MalformedURLException {
		if (bookmapValidator == null) {
			final URL XSD_BOOKMAP = new URL(
					"http://docs.oasis-open.org/dita/v1.2/os/DITA1.2-xsds/xsd1.2-url/bookmap/xsd/bookmap.xsd");
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema;
			try {
				schema = factory.newSchema(XSD_BOOKMAP);
				bookmapValidator = schema.newValidator();
			} catch (final SAXException e) {
				e.printStackTrace();
			}
		}
		return bookmapValidator;
	}

	private static boolean validateXMLSchema(Validator validator, File xml) {
		try {
			validator.validate(new StreamSource(xml));
		} catch (final IOException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		} catch (final SAXException e1) {
			System.out.println("SAX Exception: " + e1.getMessage());
			return false;
		}

		return true;

	}

}
