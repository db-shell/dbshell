package org.dbshell.commands.connections.dto;

import org.bradfordmiller.simplejndiutils.JNDIUtils;
import org.dbshell.environment.ContextAndJndi;
import org.dbshell.environment.EnvironmentVars;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionInfoUtil {
    public static ConnectionInfo getConnectionFromCurrentContextJndi() throws SQLException {
        ContextAndJndi ctxJndi = EnvironmentVars.getCurrentContextAndJndi();
        String ctx = ctxJndi.getContext();
        String jndi = ctxJndi.getJndi();
        Connection conn;
        try {
            conn = JNDIUtils.getJndiConnection(jndi, ctx);
        } catch (SQLException sqlEx) {
            throw sqlEx;
        }
        return new ConnectionInfo(ctxJndi.getContext(), ctxJndi.getJndi(), conn);
    }
}
