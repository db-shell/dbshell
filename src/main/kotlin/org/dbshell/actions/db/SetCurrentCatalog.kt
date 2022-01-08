package org.dbshell.actions.db

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionLog
import org.dbshell.actions.ActionResult
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.shellmethods.dto.ConnectionInfoUtil

data class SetCurrentCatalog(val catalog: String): Action {
    override fun execute(): ActionResult {
        var actionList = mutableListOf<ActionLog>()
        actionList.add(ActionLog("Setting current catalog to $catalog"))
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            connection.catalog = catalog
            EnvironmentVars.currentCatalog = catalog
            EnvironmentProps.setCurrentCatalog(catalog)
        }
        actionList.add(ActionLog("Current schema is set to $catalog"))
        return Either.left(actionList)
    }
}