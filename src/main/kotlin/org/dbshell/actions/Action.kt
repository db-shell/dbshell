package org.dbshell.actions

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.vavr.API.Left
import io.vavr.API.Right
import io.vavr.control.Either
import org.dbshell.jobqueue.JobQueueWrapper
import org.dbshell.ui.TablesUtil
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
interface UIResult

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
data class UIArrayResult(val data: Array<Array<Any>>): UIResult

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
data class UIBeanArrayResult<T>(val headers: LinkedHashMap<String, Any>, val iter: Iterable<T>): UIResult

typealias ActionResult = Either<List<ActionLog>, UIResult>

data class ActionLog(val event: String, val date: Date = Date()) {
    override fun toString(): String {
        return "$date: $event"
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
interface Action {
    fun execute(): ActionResult
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
                when(val ui = result.get()) {
                    is UIArrayResult -> {
                        TablesUtil.renderAttributeTable(ui.data)
                    }
                    is UIBeanArrayResult<*> -> {
                        TablesUtil.renderAttributeTable(ui.headers, ui.iter)
                    }
                }
            }
        }
    }
}