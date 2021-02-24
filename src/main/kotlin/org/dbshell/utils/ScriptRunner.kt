package org.dbshell.utils

import org.apache.ibatis.jdbc.ScriptRunner
import java.io.BufferedReader
import java.io.StringReader
import java.sql.Connection

class ScriptRunner {
    companion object {
        fun executeScript(sql: String, connection: Connection) {
            BufferedReader(StringReader(sql)).use { br ->
                val sr = ScriptRunner(connection)
                sr.setEscapeProcessing(false)
                sr.runScript(br)
            }
        }
    }
}