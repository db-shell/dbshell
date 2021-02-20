package org.dbshell.db.metadata;

import org.dbshell.db.metadata.dto.Catalog;
import org.dbshell.db.metadata.dto.Column;
import org.dbshell.db.metadata.dto.Schema;
import org.dbshell.db.metadata.dto.Table;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseMetadata {
    public static List<Schema> getSchemas(DatabaseMetaData dbmd) throws SQLException {
        try {
            ResultSet rs = dbmd.getSchemas();
            List<Schema> schemaList = new ArrayList<>();

            while (rs.next()) {
                schemaList.add(new Schema(rs.getString(1)));
            }
            rs.close();
            return schemaList;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
    }

    public static List<Catalog> getCatalogs(DatabaseMetaData dbmd) throws SQLException {
        try {
            ResultSet rs = dbmd.getCatalogs();
            List<Catalog> catalogList = new ArrayList<>();

            while (rs.next()) {
                catalogList.add(new Catalog(rs.getString(1)));
            }
            rs.close();
            return catalogList;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
    }

    public static List<Table> getTables(DatabaseMetaData dbmd, String catalog, String schema, String[] types) throws SQLException {
        try {
            ResultSet rs = dbmd.getTables(catalog, schema, null, types);
            List<Table> tableList = new ArrayList<>();

            while (rs.next()) {
                tableList.add(new Table(rs.getString(3), rs.getString(4)));
            }
            rs.close();
            return tableList;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
    }

    public static List<Column> getColumns(DatabaseMetaData dbmd, String catalog, String schema, String table) throws SQLException {
        try {

            ResultSet rsPk = dbmd.getPrimaryKeys(catalog, schema, table);
            Set<String> primaryKeys = new HashSet<>();

            while(rsPk.next()) {
                primaryKeys.add(rsPk.getString("COLUMN_NAME"));
            }
            rsPk.close();

            ResultSet rsFk = dbmd.getExportedKeys(catalog, schema, table);
            Map<String, String> foreignKeys = new HashMap<>();

            while(rsFk.next()) {
                String pkColumn = rsFk.getString("PKCOLUMN_NAME");
                foreignKeys.put(pkColumn, rsFk.getString("FKTABLE_NAME") + "--->" + rsFk.getString("FKCOLUMN_NAME"));
            }
            rsFk.close();

            ResultSet rs = dbmd.getColumns(catalog, schema, table, null);
            List<Column> columnList = new ArrayList<>();
            Boolean isPk = false;
            Boolean isFk = false;
            String fkDesc = null;

            while (rs.next()) {
                String columnName = rs.getString(4);
                if(primaryKeys.contains(columnName)) {
                    isPk = true;
                } else {
                    isPk = false;
                }
                if(foreignKeys.containsKey(columnName)) {
                    isFk = true;
                    fkDesc = foreignKeys.get(columnName);
                } else {
                    isFk = false;
                    fkDesc = "";
                }
                columnList.add(
                    new Column(
                      columnName,
                      rs.getString(6),
                      rs.getInt(7),
                      rs.getInt(9),
                      rs.getInt(10),
                      rs.getBoolean(11),
                      rs.getString(12),
                      rs.getString(13),
                      isPk,
                      isFk,
                      fkDesc
                    )
                );
            }
            rs.close();
            return columnList;
        } catch(SQLException sqlEx) {
            throw sqlEx;
        }
    }
}
