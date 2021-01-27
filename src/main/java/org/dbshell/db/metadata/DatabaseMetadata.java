package org.dbshell.db.metadata;

import org.dbshell.db.metadata.dto.Schema;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetadata {
    public static List<Schema> getSchemas(DatabaseMetaData dbmd) throws SQLException {
        try {
            ResultSet rs = dbmd.getSchemas();
            List<Schema> schemaList = new ArrayList<>();

            while (rs.next()) {
                schemaList.add(new Schema(rs.getString(1), rs.getString(2)));
            }
            rs.close();
            return schemaList;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
    }

    public static List<String> getCatalogs(DatabaseMetaData dbmd) throws SQLException {
        try {
            ResultSet rs = dbmd.getCatalogs();
            List<String> schemaList = new ArrayList<>();

            while (rs.next()) {
                schemaList.add(rs.getString(1));
            }
            rs.close();
            return schemaList;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
    }


}
