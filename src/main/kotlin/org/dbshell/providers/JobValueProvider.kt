package org.dbshell.providers

import org.dbshell.results.ResultsHashMap
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class JobValueProvider: ValueProvider {
    override fun complete(
        completionContext: CompletionContext
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext?.currentWordUpToCursor()
        return ResultsHashMap
            .resultsMap.keys()
            .toList()
            .map{k -> k.toString()}
            .filter {k -> k.contains(currentInput!!)}
            .map{k -> CompletionProposal(k)}
            .toMutableList()
    }
}