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
            if (exists) {
                f.inputStream().use { fis ->
                    val p = Properties()
                    p.load(fis)
                    p.setProperty("currentContext", context)
                    p.setProperty("currentJndi", jndi)
                    f.outputStream().use { fos ->
                        p.store(fos, null)
                    }
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
                f.inputStream().use {fis ->
                    val p = Properties()
                    p.load(fis)
                    p.setProperty("currentCatalog", catalog)
                    f.outputStream().use {fos ->
                        p.store(fos, null)
                    }
                }
            }
        }
    }
}