package org.dbshell.reflection.utils.dto;

public class Schema {

    private String _schemaName = "";
    private String _catalog = "";

    public Schema(String schemaName, String catalog) {
        _schemaName = schemaName;
        _catalog = catalog;
    }
}
