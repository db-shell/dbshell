package org.dbshell.db.metadata.dto;

public class Catalog {
    private String _catalogName = "";

    public Catalog(String catalogName) {
        _catalogName = catalogName;
    }
    public String getCatalog() {
        return _catalogName;
    }
}
