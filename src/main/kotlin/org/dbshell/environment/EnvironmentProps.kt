package org.dbshell.environment

import java.io.File
import java.util.*

class EnvironmentProps {
    companion object {

        private val f = File("conf/settings.properties")
        private val exists = f.exists()

        fun getCurrentContextAndJndi(): ContextAndJndi {
            if(exists) {
                val p = Properties()
                f.inputStream().use { fis ->
                    p.load(fis)
                    return ContextAndJndi(p.getProperty("currentContext"), p.getProperty("currentJndi"))
                }
            } else {
                return ContextAndJndi("", "")
            }
        }
        fun setCurrentContextandJndi(context: String, jndi: String) {
            if(exists) {
                f.outputStream().use {fos ->
                    val p = Properties()
                    p.setProperty("currentContext", context)
                    p.setProperty("currentJndi", jndi)
                    p.store(fos, null)
                }
            }
        }
        fun getCurrentCatalog(): String {
            if(exists) {
                val p = Properties()
                f.inputStream().use{fis ->
                    p.load(fis)
                    return p.getProperty("currentCatalog")
                }
            } else {
                return ""
            }
        }
        fun setCurrentCatalog(catalog: String) {
            if(exists) {
                f.outputStream().use {fos ->
                    val p = Properties()
                    p.setProperty("currentCatalog", catalog)
                    p.store(fos, null)
                }
            }
        }
    }
}