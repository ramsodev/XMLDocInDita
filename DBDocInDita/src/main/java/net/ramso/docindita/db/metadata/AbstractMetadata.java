package net.ramso.docindita.db.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMetadata implements IBasicMetadata {
	private String name;
	private String catalog;
	private String schema;
	private String doc;
	private DatabaseMetaData metadata;
	private List<String> labels;

	public AbstractMetadata(ResultSet resultSet, DatabaseMetaData metadata) {
		super();
		this.metadata = metadata;
		init(resultSet);
	}

	public AbstractMetadata(DatabaseMetaData metadata) {
		this.metadata = metadata;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCatalog() {
		return this.catalog;
	}

	@Override
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	@Override
	public DatabaseMetaData getMetadata() {
		return this.metadata;
	}

	@Override
	public void setMetadata(DatabaseMetaData metadata) {
		this.metadata = metadata;
	}

	@Override
	public String getSchema() {
		return this.schema;
	}

	@Override
	public void setSchema(String schema) {
		this.schema = schema;
	}

	@Override
	public String getDoc() {
		return this.doc;
	}

	@Override
	public void setDoc(String doc) {
		this.doc = doc;
	}

	@Override
	public String getId() {
		final StringBuilder st = new StringBuilder();
		if ((getCatalog() != null) && !getCatalog().isEmpty()) {
			st.append(getCatalog());
			st.append(".");
		}
		if ((getSchema() != null) && !getSchema().isEmpty()) {
			st.append(getSchema());
			st.append(".");
		}
		return st.toString();
	}

	protected void loadLabels(ResultSetMetaData metaData) throws SQLException {
		this.labels = new ArrayList<>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			this.labels.add(metaData.getColumnLabel(i).toUpperCase());
		}

	}

	protected boolean labelExist(String label) {
		return this.labels.contains(label);
	}

}
