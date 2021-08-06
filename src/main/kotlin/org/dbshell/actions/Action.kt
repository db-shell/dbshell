package org.dbshell.actions

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.vavr.control.Either
import java.util.*

typealias ActionResult = Either<List<ActionLog>, Array<Array<Any>>>

data class ActionLog(val event: String, val date: Date = Date())

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@type")
interface Action {
    fun execute(): ActionResult
}