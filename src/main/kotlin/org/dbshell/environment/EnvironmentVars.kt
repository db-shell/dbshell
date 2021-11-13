package org.dbshell.environment

object EnvironmentVars {
    val currentContextAndJndi: ContextAndJndi
        get() {
            val envContext = System.getProperty("currentContext")
            val envJndi = System.getProperty("currentJndi")
            return ContextAndJndi(envContext, envJndi)
        }

    fun currentContextAndJndi(context: String?, jndi: String?) {
        if (context != null) System.setProperty("currentContext", context)
        if (jndi != null) System.setProperty("currentJndi", jndi)
    }

    var currentCatalog: String?
        get() = System.getProperty("currentCatalog")
        set(catalog) {
            if (catalog != null) {
                System.setProperty("currentCatalog", catalog)
            }
        }
    var currentSchema: String?
        get() = System.getProperty("currentSchema")
        set(schema) {
            if (schema != null) {
                System.setProperty("currentSchema", schema)
            }
        }
}