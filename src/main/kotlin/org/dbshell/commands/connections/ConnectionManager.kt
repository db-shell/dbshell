package org.dbshell.commands.connections

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentVars
import org.dbshell.reflection.utils.DatabaseMetadataUtil
import org.slf4j.LoggerFactory
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import org.springframework.shell.table.BeanListTableModel
import org.springframework.shell.table.BorderStyle
import org.springframework.shell.table.TableBuilder
import java.sql.SQLException
import java.util.*

@ShellComponent
class ConnectionManager {

    data class ConnectionEntries(val key: String, val value: String)

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionManager::class.java)

        private fun getFormattedPrimitivesFromDbMetadata(): Set<ConnectionEntries> {

            val (envContext, envJndi) = EnvironmentVars.getCurrentContextAndJndi()

            return try {

                val connection = JNDIUtils.getJndiConnection(envJndi, envContext)
                val dbmd = connection.metaData
                val methodList = DatabaseMetadataUtil.getPrimitivesFromDBMetadata(dbmd)

                methodList.map { m ->
                    val retval =
                        try {
                            m.value.invoke(dbmd)
                        } catch (ex: Exception) {
                            "Not Supported by this driver"
                        }

                    if (retval == null)
                        ConnectionEntries(m.key, "")
                    else {
                        val formattedRetval =
                            if (retval.toString().length > 77) {
                                retval.toString().substring(0, 77) + "..."
                            } else {
                                retval.toString()
                            }
                        ConnectionEntries(m.key, formattedRetval)
                    }
                }.sortedBy { e -> e.key }.toSet()
            } catch (sqlEx: SQLException) {
                val message =
                    "Error when creating connection to context $envContext and jndi $envJndi: ${sqlEx.message}"
                logger.error(message)
                throw sqlEx
            }
        }

        private fun <T> renderAttributeTable(iter: Iterable<T>) {
            val headers = LinkedHashMap<String, Any>()
            headers["key"] = "Connection Property"
            headers["value"] = "Value"

            val model = BeanListTableModel(iter, headers)
            val tableBuilder = TableBuilder(model)
            tableBuilder.addInnerBorder(BorderStyle.fancy_light)
            tableBuilder.addHeaderBorder(BorderStyle.fancy_double)
            println(tableBuilder.build().render(80))
        }
    }

    @ShellMethod("Get active connection information")
    fun getCurrentConnectionInfo() {
        try {
            val entries = getFormattedPrimitivesFromDbMetadata()
            renderAttributeTable(entries)
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }

    @ShellMethod("Get specific attribute of active connection information")
    fun getCurrentConnectionAttributeInfo(@ShellOption(valueProvider = DatabaseMdPrimitiveProvider::class) attributeName: String) {
        try {
            val entries = getFormattedPrimitivesFromDbMetadata()
            val entryAttribute = entries.filter {ce -> ce.key == attributeName}
            renderAttributeTable(entryAttribute)
        } catch (ex: Exception) {
            println("Error getting current connection information: ${ex.message}")
        }
    }
}