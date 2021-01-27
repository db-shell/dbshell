package org.dbshell.db.metadata.dto;

public class Schema {

    private String _schemaName = "";
    private String _catalog = "";

    public Schema(String schemaName, String catalog) {
        _schemaName = schemaName;
        _catalog = catalog;
    }

    public String getSchema() {
        return _schemaName;
    }
    public String getCatalog() {
        return _catalog;
    }
}