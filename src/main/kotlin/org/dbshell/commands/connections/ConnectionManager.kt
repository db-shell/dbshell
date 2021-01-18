package org.dbshell.commands.connections

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentVars
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.table.BeanListTableModel
import org.springframework.shell.table.BorderStyle
import org.springframework.shell.table.TableBuilder
import java.lang.reflect.Modifier
import java.sql.SQLException
import java.util.*

@ShellComponent
class ConnectionManager {

    data class ConnectionEntries(val key: String, val value: String)

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionManager::class.java)
    }
    @ShellMethod("Get active connection information")
    fun getCurrentConnectionInfo() {
        val (envContext, envJndi) = EnvironmentVars.getCurrentContextAndJndi()
        try {
            val connection = JNDIUtils.getJndiConnection(envJndi, envContext)
            val dbmd = connection.metaData

            val klazz = dbmd::class.java
            val methodList =
                klazz.methods.filter {m ->
                    val modifiers = m.modifiers
                    (m.name != "toString" || m.name != "hashCode") &&
                    m.parameterCount == 0 &&
                    Modifier.isPublic(modifiers) &&
                    (m.returnType == String::class.javaObjectType ||
                     m.returnType == Integer::class.javaPrimitiveType ||
                     m.returnType == Boolean::class.javaPrimitiveType
                    )
                }

            val entries =
                methodList.map {m ->
                    val retval =
                        try {
                            m.invoke(dbmd)
                        } catch(ex: Exception) {
                            "Not Supported by this driver"
                        }

                        val name =
                            m.name
                                .replace("get", "")
                                .replace("JDBC", "Jdbc")
                                .replace("SQL", "Sql")
                                .replace("URL", "Url")

                       val formattedName = name.trim().first().toUpperCase() + name.substring(1, name.length)

                    if(retval == null)
                        ConnectionEntries(name, "")
                    else {
                        val formattedRetval =
                            if(retval.toString().length > 77) {
                                retval.toString().substring(0, 77) + "..."
                            } else {
                                retval.toString()
                            }
                        ConnectionEntries(formattedName, formattedRetval)
                    }
                }.sortedBy{e -> e.key}

            val headers = LinkedHashMap<String, Any>()
            headers["key"] = "Connection Property"
            headers["value"] = "Value"

            val model = BeanListTableModel(entries, headers)
            val tableBuilder = TableBuilder(model)
            tableBuilder.addInnerBorder(BorderStyle.fancy_light)
            tableBuilder.addHeaderBorder(BorderStyle.fancy_double)

            println(tableBuilder.build().render(80))

        } catch (sqlEx: Exception) {
            val message = "Error when creating connection to context $envContext and jndi $envJndi: ${sqlEx.message}"
            logger.error(message)
        }
    }
}