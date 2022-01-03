package org.dbshell.actions.jndi

import io.vavr.control.Either
import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.actions.GridResult
import org.dbshell.shellmethods.JndiMethods
import javax.naming.InitialContext

data class GetEntries(val context: String): Action {
    override fun execute(): ActionResult {
       val headers = setOf("Jndi Entry", "Value")
        val initCtx = InitialContext()
        val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)
        val entries =
            JNDIUtils.getEntriesForJndiContext(mc).map{ kvp -> JndiMethods.JndiEntries(kvp.key, kvp.value) }.toList()
        val result = entries.map {e -> listOf(e.key, e.value)}
        val gridResult = GridResult(headers, result)
        return Either.right(gridResult)
    }
}