package net.ramso.doc.dita.xml;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.ramso.doc.Config;
import net.ramso.doc.dita.xml.wsdl.GenerateWsdl;

class GenreateWSDLTest {

	private GenerateWsdl generate;

	@BeforeEach
	void setUp() throws Exception {
		Config.start();
		generate = new GenerateWsdl();
		
	}

//	 @Test
//	 @DisplayName("Procesar echo")
//	 void testGenerateWSDL() {
//	 URL echo =
//	 Thread.currentThread().getContextClassLoader().getResource("echo.wsdl");
//	 try {
//	 generate.generateWSDL(echo);
//	 } catch (Exception e) {
//	
//	 e.printStackTrace();
//	 fail(e.getLocalizedMessage());
//	 }
//	 assertTrue(true);
//	 }
	//
	@Test
	@DisplayName("Procesar Simulacion")
	void testGenerateWSDL2() {
		URL echo = Thread.currentThread().getContextClassLoader().getResource("simulacion.wsdl");
		try {
			generate.generateWSDL(echo);
		} catch (Exception e) {

			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		assertTrue(true);
	}
	//
	// @Test
	// @DisplayName("Procesar Tarificador")
	// void testGenerateWSDL3() {
	// URL echo;
	// try {
	// echo = new URL("https://svd.almudenaseguros.es/services/tarificador?wsdl");
	// generate.generateWSDL(echo);
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	// }
	// assertTrue(true);
	// }

	// @Test
	// @DisplayName("Procesar Sepa")
	// void testGenerateWSDL4() {
	//
	// try {
	// URL echo = Thread.currentThread().getContextClassLoader()
	// .getResource("Entities/WSDLs/Exposed_WS_ENVIO_TRANSFERENCIA.wsdl");
	//
	// generate.generateWSDL(echo);
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	// }
	// assertTrue(true);
	// }
//	@Test
//	@DisplayName("Procesar as400")
//	void testGenerateWSDL5() {
//
//		try {
//			URL echo = Thread.currentThread().getContextClassLoader().getResource("btw020ws.wsdl");
//
//			generate.generateWSDL(echo);
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			fail(e.getLocalizedMessage());
//		}
//		assertTrue(true);
//	}

	
}
