package org.dbshell.actions.sql

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
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