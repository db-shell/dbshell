package org.dbshell.commands.connections

import io.vavr.control.Either
import org.dbshell.actions.ActionResult
import org.dbshell.ui.TablesUtil

interface UIManager {
    fun renderResult(result: ActionResult) {
        when (result) {
            is Either.Left -> {
                val actionLog = result.left.sortedBy { it.date }
                actionLog.forEach { println(it) }
            }
            is Either.Right -> {
                val dataArray = result.get()
                TablesUtil.renderAttributeTable(dataArray)
            }
        }
    }
}