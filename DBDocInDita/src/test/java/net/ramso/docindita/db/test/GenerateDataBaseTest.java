package net.ramso.docindita.db.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.event.TreeWillExpandListener;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import net.ramso.docindita.db.Config;
import net.ramso.docindita.db.GenerateDataBase;
import net.ramso.docindita.db.metadata.ConnectionMetadata;
import net.ramso.docindita.tools.DitaConstants;

class GenerateDataBaseTest extends BaseTest {
	@ClassRule
	public static MariaDBContainer container = (MariaDBContainer) new MariaDBContainer()
			.withInitScript("sakila-schema.sql");
	// .withClasspathResourceMapping("pg", "/var/lib/postgresql/data",
	// BindMode.READ_WRITE);

	private static GenerateDataBase generate;

	@BeforeAll
	static void setUp() throws Exception {

		Config.start();
		// Connection con = getConnection();
		 container.start();

		String jdbcUrl = container.getJdbcUrl();
		System.out.println(jdbcUrl);
		String username = container.getUsername();
		String password = container.getPassword();
		try {
			Connection con = DriverManager.getConnection(jdbcUrl, username, password);
			generate = new GenerateDataBase(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterAll
	static void tearDown() throws Exception {
		// disconnect(con);
		// if (postgresContainer.isRunning()) {
		// postgresContainer.stop();
		// }

	}

	// @Test
	// @DisplayName("Catalogos")
	// void testGenerateCatalog() {
	// Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator +
	// "catalogs");
	// clean();
	// try {
	// this.generate.generateCatalogs();
	// assertTrue(valid(), "Fallo en la validación de los xml");
	// } catch (final Exception e) {
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	// }
	// assertTrue(true);
	// }
	//
	// @Test
	// @DisplayName("schemas")
	// void testGenerateSchemas() {
	// Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator +
	// "schemas");
	// clean();
	// try {
	// this.generate.generateSchemas();
	// assertTrue(valid(), "Fallo en la validación de los xml");
	// } catch (final Exception e) {
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	// }
	// }

	@Test
	@DisplayName("Default schema")
	void testGenerateSchema() {
		Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator + "schema");
		clean();
		try {
			this.generate.generateSchema();
			assertTrue(valid(), "Fallo en la validación de los xml");
		} catch (final Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	// @Test
	// @DisplayName("Schema EMPEX")
	// void testGenerateEMPEX() {
	// Config.setOutputDir(DitaConstants.OUTDIR_DEFAULT + File.separator + "EMPEX");
	// clean();
	// try {
	// this.generate.generateSchema("EMPEX");
	// assertTrue(valid(), "Fallo en la validación de los xml");
	// } catch (final Exception e) {
	// e.printStackTrace();
	// fail(e.getLocalizedMessage());
	// }
	// }

}
