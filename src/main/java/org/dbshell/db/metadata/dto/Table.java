package org.dbshell.db.metadata.dto;

public class Table {
    private String _tableName = "";
    private String _tableType = "";

    public Table(String tableName, String tableType) {
        _tableName = tableName;
        _tableType = tableType;
    }

    public String getTableName() {
        return _tableName;
    }
    public String getTableType() {
        return _tableType;
    }
}
