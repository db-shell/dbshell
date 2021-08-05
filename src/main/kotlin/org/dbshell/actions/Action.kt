package org.dbshell.actions

import io.vavr.control.Either
import java.util.*

typealias ActionResult = Either<List<ActionLog>, Array<Array<Any>>>

data class ActionLog(val event: String, val date: Date)

interface Action {
    fun execute(): ActionResult
}