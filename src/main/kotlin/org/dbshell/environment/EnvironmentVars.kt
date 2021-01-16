package org.dbshell.environment

import java.util.Map

class EnvironmentVars {
    companion object {
        fun getCurrentContextAndJndi(): Pair<String, String> {
            val envContext = System.getProperty("currentContext")
            val envJndi = System.getProperty("currentJndi")
            return Pair(envContext, envJndi)
        }
    }
}