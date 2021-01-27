package org.dbshell.environment;

public class ContextAndJndi {
    String _context = "";
    String _jndi = "";
    public ContextAndJndi(String context, String jndi) {
        _context = context;
        _jndi = jndi;
    }
    public String getContext() {
        return _context;
    }
    public String getJndi() {
        return _jndi;
    }
}
