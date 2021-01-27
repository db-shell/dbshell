package org.dbshell.environment;

public class EnvironmentVars {
    public static ContextAndJndi getCurrentContextAndJndi() {
        String envContext = System.getProperty("currentContext");
        String envJndi = System.getProperty("currentJndi");
        return new ContextAndJndi(envContext, envJndi);
    }
    public static void setCurrentContextAndJndi(String context, String jndi) {
        if(context != null)
            System.setProperty("currentContext", context);
        if(jndi != null)
            System.setProperty("currentJndi", jndi);
    }
    public static String getCurrentCatalog() {
        return System.getProperty("currentCatalog");
    }
    public static void setCurrentCatalog(String catalog) {
        if(catalog != null) {
            System.setProperty("currentCatalog", catalog);
        }
    }
}
