package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.shellmethods.dto.ConnectionInfoUtil

data class SetCurrentSchema(val schema: String): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Setting current schema to $schema"))
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            connection.schema = schema
            EnvironmentVars.currentSchema = schema
            EnvironmentProps.setCurrentSchema(schema)
        }
        actionList.add(ActionLog("Current schema is set to $schema"))
        return Either.left(actionList)
    }
}