package org.dbshell.commands

import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class MyCommands {
    @ShellMethod("Add two integers together.", key = ["sum"])
    fun add(a: Int, b: Int): Int {
        return a + b
    }
}
