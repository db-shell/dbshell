package org.dbshell.db.metadata.dto;

public class Column {
    private String _columnName; //4
    private String _typeName; //6
    private Integer _columnSize; //7
    private Integer _decimalDigits; //9
    private Integer _precision; //10
    private Boolean _isNullable; //11
    private String _comments; //12
    private String _defaultValue; //13
    private Boolean _isPk;

    public Column(String columnName, String typeName, Integer columnSize, Integer decimalDigits, Integer precision, Boolean isNullable, String comments, String defaultValue, Boolean isPk) {
        _columnName = columnName;
        _typeName = typeName;
        _columnSize = columnSize;
        _decimalDigits = decimalDigits;
        _precision = precision;
        _isNullable = isNullable;
        _comments = comments;
        _defaultValue = defaultValue;
        _isPk = isPk;
    }

    public String getColumnName() {
        return _columnName;
    }
    public String getTypeName() {
        return _typeName;
    }
    public Integer getColumnSize() {
        return _columnSize;
    }
    public Integer getDecimalDigits() {
        return _decimalDigits;
    }
    public Integer getPrecision() {
        return _precision;
    }
    public Boolean getIsNullable() {
        return _isNullable;
    }
    public String getComments() {
        return _comments;
    }
    public String getDefaultValue() {
        return _defaultValue;
    }
    public Boolean getIsPrimaryKey() {
        return _isPk;
    }
}
