package org.dbshell.actions.db

import io.vavr.control.Either
import org.apache.commons.io.FileUtils
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import java.io.File

data class GetDDLForDb(val scriptFile: File, val entries:  List<String>): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Generating ddl for current database connection..."))
        entries.forEach {e ->
            FileUtils.writeStringToFile(scriptFile, e, "ISO-8859-1", true)
        }
        actionList.add(ActionLog("Generating ddl complete. Please see file ${scriptFile.absolutePath}."))
        return Either.left(actionList)
    }
}