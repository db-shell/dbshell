package org.dbshell.actions

import io.vavr.control.Either
import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.utils.ScriptRunner

data class RunSqlCommands(val sql: String): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Executing the following sql: $sql..."))
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            ScriptRunner.executeScript(sql, connection)
        }
        actionList.add(ActionLog("Execution complete."))
        return Either.left(actionList)
    }
}