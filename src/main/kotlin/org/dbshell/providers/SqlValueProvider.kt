package org.dbshell.providers

import com.github.mnadeem.TableNameParser
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport
import org.springframework.stereotype.Component

@Component
class SqlValueProvider: ValueProviderSupport() {
    override fun complete(parameter: MethodParameter?, completionContext: CompletionContext?, hints: Array<out String>?): MutableList<CompletionProposal> {
        val currentInput = completionContext?.currentWordUpToCursor()
        return TableNameParser(currentInput).tables()
            .filter{s -> s.contains(currentInput!!)}
            .map{cp -> CompletionProposal(cp)}
            .toMutableList()
    }
}