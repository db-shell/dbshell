package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.utils.ScriptRunner
import java.io.File
import java.nio.file.Files

data class RunSqlScript(val scriptFile: File): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Executing script ${scriptFile.absolutePath}..."))
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val content = String(Files.readAllBytes(scriptFile.toPath()))
            ScriptRunner.executeScript(content, connection)
        }
        actionList.add(ActionLog("Execution of script complete."))
        return Either.left(actionList)
    }
}