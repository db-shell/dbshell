package org.dbshell.providers

import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component
import javax.naming.InitialContext

@Component
class JndiValueProvider: ValueProvider {
    override fun complete(
        completionContext: CompletionContext
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext?.currentWord()
        val words = completionContext?.words
        val initCtx = InitialContext()
        val contextIdx = words?.indexOf("--context")?.plus(1)
        val context = words?.get(contextIdx!!)
        val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)

        if(context.equals("") || mc == null) {
            return mutableListOf()
        } else {
            return JNDIUtils.getEntriesForJndiContext(mc)
                .keys
                .filter { c -> c.contains(currentInput!!) }
                .map { cp -> CompletionProposal(cp) }
                .toMutableList()
        }
    }
}