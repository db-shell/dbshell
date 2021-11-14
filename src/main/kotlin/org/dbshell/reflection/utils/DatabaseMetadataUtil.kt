package org.dbshell.reflection.utils

import java.lang.Boolean
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.sql.DatabaseMetaData
import java.util.*
import java.util.stream.Collectors
import kotlin.String

object DatabaseMetadataUtil {
    fun getPrimitivesFromDBMetadata(dbmd: DatabaseMetaData): Map<String, Method?> {
        val klazz: Class<*> = dbmd.javaClass
        return Arrays.stream(klazz.methods)
            .filter { m: Method ->
                val modifier = m.modifiers
                m.parameterCount == 0 && Modifier.isPublic(modifier) &&
                (m.returnType == String::class.java || m.returnType == Integer.TYPE || m.returnType == Boolean.TYPE || m.returnType == java.lang.Long.TYPE)
            }.collect(
                Collectors.toMap(
                    { n: Method ->
                        val name = n.name
                            .replace("get", "")
                            .replace("JDBC", "Jdbc")
                            .replace("SQL", "Sql")
                            .replace("URL", "url")
                        val formattedName =
                            name.trim { it <= ' ' }.substring(0, 1).toUpperCase() + name.substring(1)
                        formattedName
                    },
                    { m: Method? -> m }
                )
            )
    }
}