package org.dbshell.shellmethods.dto

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentVars
import org.osjava.datasource.SJDataSource
import org.osjava.sj.jndi.MemoryContext
import java.sql.Connection
import java.sql.SQLException
import javax.naming.InitialContext
import javax.naming.Name

data class ConnectionInfo(val contextName: String, val jndiName: String, val connection: Connection)
data class Credentials(val url: String, val username: String, val password: String)

class ConnectionInfoUtil {

    companion object {
        private fun stringifyField(sjds: SJDataSource, field: String): String {
            val entry = sjds.javaClass.getDeclaredField(field)
            entry.isAccessible = true
            val finalString = entry.get(sjds) as String
            return finalString
        }

        @Throws(SQLException::class)
        fun getConnectionFromCurrentContextJndi(): ConnectionInfo {
            val ctxJndi = EnvironmentVars.currentContextAndJndi
            val ctx = ctxJndi.context
            val jndi = ctxJndi.jndi
            val conn = JNDIUtils.getJndiConnection(jndi, ctx)
            return ConnectionInfo(ctxJndi.context, ctxJndi.jndi, conn)
        }

        @Throws(SQLException::class)
        fun getCredentialsFromCurrentConnection(): Credentials {
            val url =
                getConnectionFromCurrentContextJndi().connection.use {conn ->
                    val md = conn.metaData
                    md.url
                }
            val ctxJndi = EnvironmentVars.currentContextAndJndi
            val ctx = ctxJndi.context
            val jndi = ctxJndi.jndi
            val mc: MemoryContext = JNDIUtils.getMemoryContextFromInitContext(InitialContext(), ctx)
            val field = mc.javaClass.getDeclaredField("namesToObjects")
            field.isAccessible = true
            val fieldMap = field.get(mc) as Map<Name, SJDataSource>
            val stringified =
                fieldMap.entries.map{e ->
                    e.key.toString() to
                            Credentials(url,
                                stringifyField(e.value, "username"),
                                stringifyField(e.value, "password")
                            )
                }.toMap()

            return stringified[jndi]!!
        }
    }
}