package net.ramso.docindita.xml;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.ramso.docindita.tools.DitaConstants;
import net.ramso.docindita.xml.Config;
import net.ramso.docindita.xml.wadl.GenerateWadl;

class GenreateWADLTest extends BaseTest{

	private GenerateWadl generate;

	@BeforeEach
	void setUp() throws Exception {
		Config.start();
		generate = new GenerateWadl();

	}

	@Test
	@DisplayName("Procesar storage")
	void testGenerateWADL() {
		Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator + "storage");
		clean();
		URL wadl = Thread.currentThread().getContextClassLoader().getResource("storage.wadl");
		try {
			generate.generateWADL(wadl);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		assertTrue(true);
	}
	
	@Test
	@DisplayName("Procesar options")
	void testGenerateWADL1() {
		Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator + "options");
		clean();
		URL wadl = Thread.currentThread().getContextClassLoader().getResource("options.wadl");
		try {
			generate.generateWADL(wadl);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		assertTrue(true);
	}
	
	@Test
	@DisplayName("Procesar sample")
	void testGenerateWADL2() {
		Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator + "sample1");
		clean();
		URL wadl = Thread.currentThread().getContextClassLoader().getResource("sample1.wadl");
		try {
			generate.generateWADL(wadl);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
		assertTrue(true);
	}


}