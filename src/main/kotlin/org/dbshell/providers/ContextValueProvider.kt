package org.dbshell.providers

import org.apache.commons.io.FilenameUtils
import org.bradfordmiller.simplejndiutils.JNDIUtils
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport
import org.springframework.stereotype.Component
import java.io.File

@Component
class ContextValueProvider: ValueProviderSupport() {
    override fun complete(
        parameter: MethodParameter?,
        completionContext: CompletionContext?,
        hints: Array<out String>?
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext?.currentWordUpToCursor()
        return JNDIUtils.getAvailableJndiContexts(null)
            .filter{c -> c.contains(currentInput!!)}
            .map{p -> FilenameUtils.removeExtension(File(p).name)}
            .map{cp -> CompletionProposal(cp)}
            .toMutableList()
    }
}