package org.dbshell.commands.connections.dto;

import java.sql.Connection;

public class ConnectionInfo {
    private String _contextName = "";
    private String _jndiName = "";
    private Connection _connection;

    public ConnectionInfo(String contextName, String jndiName, Connection connection) {
       _contextName = contextName;
       _jndiName = jndiName;
       _connection = connection;
    }

    public String getContextName() {
        return _contextName;
    }

    public String getJndiName() {
        return _jndiName;
    }

    public Connection getConnection() {
        return _connection;
    }
}
