package org.dbshell.environment

import java.io.File
import java.util.*

class EnvironmentProps {
    companion object {

        private val f = File("conf/settings.properties")
        private val exists = f.exists()

        fun getCurrentContextAndJndi(): Pair<String, String> {
            if(exists) {
                val p = Properties()
                f.inputStream().use { fis ->
                    p.load(fis)
                    return Pair(p.getProperty("currentContext"), p.getProperty("currentJndi"))
                }
            } else {
                return Pair("", "")
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
    }
}