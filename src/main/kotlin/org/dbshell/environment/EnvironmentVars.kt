package org.dbshell.environment

class EnvironmentVars {
    companion object {
        fun getCurrentContextAndJndi(): ContextAndJndi {
            val envContext = System.getProperty("currentContext")
            val envJndi = System.getProperty("currentJndi")
            return ContextAndJndi(envContext, envJndi)
        }
        fun setCurrentContextAndJndi(context: String, jndi: String) {
            if(context != null)
                System.setProperty("currentContext", context)
            if(jndi != null)
                System.setProperty("currentJndi", jndi)
        }
    }
}