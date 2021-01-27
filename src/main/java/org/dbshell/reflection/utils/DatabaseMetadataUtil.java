package org.dbshell.reflection.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.DatabaseMetaData;
import java.util.Arrays;
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
}
