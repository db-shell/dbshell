package org.dbshell.results

import org.dbshell.actions.ActionResult
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ResultsHashMap {
    companion object {
        val resultsMap = ConcurrentHashMap<UUID, ActionResult>()
    }
}