package org.dbshell.providers

import org.dbshell.results.ResultsHashMap
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport
import org.springframework.stereotype.Component

@Component
class JobValueProvider: ValueProviderSupport() {
    override fun complete(parameter: MethodParameter?, completionContext: CompletionContext?, hints: Array<out String>?): MutableList<CompletionProposal> {
        val currentInput = completionContext?.currentWordUpToCursor()
        return ResultsHashMap.resultsMap.keys().toList().map{k -> k.toString()}.filter {k -> k.contains(currentInput!!)}.map{k -> CompletionProposal(k)}.toMutableList()
    }
}