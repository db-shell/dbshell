package org.dbshell.commands.connections

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class SqlManager {
    @ShellMethod("Run a query against the current connection")
    fun runQuery(query: String) {

    }
}