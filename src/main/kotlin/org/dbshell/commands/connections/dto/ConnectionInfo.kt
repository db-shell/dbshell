package org.dbshell.commands.connections.dto

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.environment.EnvironmentVars
import java.sql.Connection
import java.sql.SQLException

data class ConnectionInfo(val contextName: String, val jndiName: String, val connection: Connection)

class ConnectionInfoUtil {
    companion object {
        @Throws(SQLException::class)
        fun getConnectionFromCurrentContextJndi(): ConnectionInfo {
            val ctxJndi = EnvironmentVars.currentContextAndJndi
            val ctx = ctxJndi.context
            val jndi = ctxJndi.jndi
            val conn = JNDIUtils.getJndiConnection(jndi, ctx)
            return ConnectionInfo(ctxJndi.context, ctxJndi.jndi, conn)
        }
    }
}