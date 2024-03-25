/*package org.dbshell.providers

import org.bradfordmiller.simplejndiutils.JNDIUtils
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
        val words = completionContext.words

        val initCtx = InitialContext()
        val contextIdx: Int = words.indexOf("--context") + 1
        val context: String = words[contextIdx]
        val mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context)

        val proposals: MutableList<CompletionProposal> =

        if(context.equals("") || mc == null) {
             mutableListOf()
        } else {
            JNDIUtils.getEntriesForJndiContext(mc)
                .keys
                .filter { c -> c.contains(currentInput!!) }
                .map { cp -> CompletionProposal(cp) }
                .toMutableList()
        }

        return proposals
    }
}*/