package org.dbshell.actions.jndi

import io.vavr.control.Either
import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.actions.ActionResult
import org.dbshell.actions.UIAction
import org.dbshell.shellmethods.JndiMethods
import javax.naming.InitialContext

data class GetEntries(val context: String): UIAction() {
    override val headers: MutableSet<String>
        get() = mutableSetOf("Jndi Entry", "Value")

    override fun execute(): ActionResult {
        val initCtx = InitialContext()
        val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)
        val entries =
            JNDIUtils.getEntriesForJndiContext(mc).map { kvp -> JndiMethods.JndiEntries(kvp.key, kvp.value) }.toList()
        val result = entries.map { e -> listOf(e.key, e.value) }
        val gridResult = getGridResult(result)
        return Either.right(gridResult)
    }
}