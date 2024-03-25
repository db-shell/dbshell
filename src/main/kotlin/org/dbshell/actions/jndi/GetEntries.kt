package org.dbshell.actions.jndi

import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIAction
import org.dbshell.connections.ConnectionRepository
import io.vavr.control.Either

class GetEntries(): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("Connection Name", "Url")

    override fun execute(): ActionResult {
        val entries = ConnectionRepository.loadAllConnections()
        val result = entries.map { e -> listOf(e.key, e.value.url)}
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}