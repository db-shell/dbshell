package org.dbshell.db.metadata

import org.dbshell.reflection.utils.DatabaseMetadataUtil
import org.dbshell.shellmethods.ConnectionMethods
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.slf4j.LoggerFactory
import java.sql.SQLException

class DatabasePrimitives {

    data class ConnectionEntries(val key: String, val value: String)

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionMethods::class.java)

        fun getFormattedPrimitivesFromDbMetadata(): Set<ConnectionEntries> {

            val connectionInfo = ConnectionInfoUtil.getConnectionFromCurrentContextJndi()

            return try {

                connectionInfo.connection.use { connection ->

                    val dbmd = connection.metaData
                    val methodList = DatabaseMetadataUtil.getPrimitivesFromDBMetadata(dbmd)

                    methodList.map { m ->
                        val retval =
                            try {
                                m.value?.invoke(dbmd)
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
                }
            } catch (sqlEx: SQLException) {
                val message =
                    "Error when creating connection to context ${connectionInfo.contextName} and jndi ${connectionInfo.jndiName}: ${sqlEx.message}"
                logger.error(message)
                throw sqlEx
            }
        }
    }
}