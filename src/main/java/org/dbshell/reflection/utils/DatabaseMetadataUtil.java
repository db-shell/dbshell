package org.dbshell.reflection.utils;

import org.dbshell.reflection.utils.dto.Schema;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseMetadataUtil {

    public static Map<String, Method> getPrimitivesFromDBMetadata(DatabaseMetaData dbmd) {
        Class klazz = dbmd.getClass();
        return Arrays.stream(klazz.getMethods())
                .filter(m -> {
                    Integer modifier = m.getModifiers();
                    return m.getParameterCount() == 0 &&
                            Modifier.isPublic(modifier) &&
                            (m.getReturnType().equals(String.class) ||
                                    m.getReturnType().equals(Integer.TYPE) ||
                                    m.getReturnType().equals(Boolean.TYPE) ||
                                    m.getReturnType().equals(Long.TYPE));
                }).collect(
                        Collectors.toMap(
                                n -> {
                                    String name =
                                            n.getName()
                                                    .replace("get", "")
                                                    .replace("JDBC", "Jdbc")
                                                    .replace("SQL", "Sql")
                                                    .replace("URL", "url");

                                    String formattedName = name.trim().substring(0, 1).toUpperCase() + name.substring(1);

                                    return formattedName;
                                },
                                m -> m
                        )
                );
    }

    public static List<Schema> getSchemas(DatabaseMetaData dbmd) throws SQLException {
        try {
            ResultSet rs = dbmd.getSchemas();
            ResultSetMetaData rsmd = rs.getMetaData();
            List<Schema> schemaList = new ArrayList<>();

            // Display the result set data.
            while (rs.next()) {
                schemaList.add(new Schema(rs.getString(1), rs.getString(2)));
            }
            rs.close();
            return schemaList;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
    }
}
