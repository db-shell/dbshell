package org.dbshell.commands.connections

import io.vavr.control.Either
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.jobqueue.JobQueueWrapper
import org.dbshell.ui.TablesUtil
import java.util.*

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
    fun executeAction(action: Action, isAsync: Boolean = false) {
        if(isAsync) {
            val jobId = JobQueueWrapper.put(action)
            println("Job has been dispatched to job processor. Access the results of your job with this id: $jobId")
        } else {
            val result = action.execute()
            renderResult(result)
        }
    }
}