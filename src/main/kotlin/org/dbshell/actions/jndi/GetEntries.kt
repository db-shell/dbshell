package org.dbshell.actions.jndi

import io.vavr.control.Either
import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.dbshell.actions.Action
import org.dbshell.actions.ActionResult
import org.dbshell.shellmethods.JndiMethods
import javax.naming.InitialContext

data class GetEntries(val context: String): Action {
    override fun execute(): ActionResult {
        val values: MutableList<Map<String, Any?>> = mutableListOf(mutableMapOf("Jndi Entry" to "Jndi Entry", "Value" to "Value"))
        val initCtx = InitialContext()
        val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)
        val entries = JNDIUtils.getEntriesForJndiContext(mc).map{ kvp -> JndiMethods.JndiEntries(kvp.key, kvp.value) }.toList()
        entries.forEach {entry ->
            values.add(mutableMapOf("Jndi Entry" to entry.key, "Value" to entry.value))
        }
        val data = values.map { v -> v.values.toTypedArray()}.toTypedArray()
        return Either.right(data)
    }
}