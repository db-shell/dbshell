package org.dbshell.db.metadata.dto;

public class Schema {

    private String _schemaName = "";

    public Schema(String schemaName) {
        _schemaName = schemaName;
    }

    public String getSchema() {
        return _schemaName;
    }
}