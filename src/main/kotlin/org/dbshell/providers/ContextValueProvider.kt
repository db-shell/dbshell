package org.dbshell.providers

import org.dbshell.connections.ConnectionRepository
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class ConnectionValueProvider: ValueProvider {

    override fun complete(
        completionContext: CompletionContext
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext.currentWordUpToCursor()

        val proposals: MutableList<CompletionProposal> =

            ConnectionRepository.loadAllConnections()
            .filter{c -> c.key.contains(currentInput!!)}
            .map{cp -> CompletionProposal(cp.key)}
            .toMutableList()

        return proposals
    }
}