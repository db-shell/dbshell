package org.dbshell.actions

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.vavr.API.Left
import io.vavr.API.Right
import io.vavr.control.Either
import org.dbshell.jobqueue.JobQueueWrapper
import org.dbshell.ui.TablesUtil
import java.util.*

data class GridResult(val headers: Set<String>, val data: List<List<Any?>>)
typealias ActionResult = Either<List<ActionLog>, GridResult>

data class ActionLog(val event: String, val date: Date = Date()) {
    override fun toString(): String {
        return "$date: $event"
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
interface Action {
    fun execute(): ActionResult?
}

interface ActionExecutor: ActionRenderer  {
    fun executeAction(action: Action, isAsync: Boolean = false): Either<UUID, ActionResult> {
        return if(isAsync) {
            val jobId = JobQueueWrapper.put(action)
            Left(jobId)
        } else {
            val result = action.execute()
            Right(result)
        }
    }
}

interface ActionRenderer {
    fun renderResult(result: Either<UUID, ActionResult>) {
        when (result) {
            is Either.Left -> {
                println(
                    "Job has been dispatched to job processor. Access the result of the job with this id: ${result.left}"
                )
            }
            is Either.Right -> {
                val ar = result.get()
                renderAction(ar)
            }
        }
    }
    fun renderAction(result: ActionResult) {
        when (result) {
            is Either.Left -> {
                val actionLog = result.left.sortedBy { it.date }
                actionLog.forEach { println(it) }
            }
            is Either.Right -> {
                val gridResult = result.get()
                val headers: Array<Array<Any?>> = arrayOf(gridResult.headers.toTypedArray())
                val data = gridResult.data.map {d -> d.toTypedArray()}.toTypedArray()
                val formattedResult = headers.plus(data)
                TablesUtil.renderAttributeTable(formattedResult)
            }
        }
    }
}