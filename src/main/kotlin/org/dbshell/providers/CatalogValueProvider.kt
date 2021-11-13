package org.dbshell.providers

import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport
import org.springframework.stereotype.Component

@Component
class CatalogValueProvider: ValueProviderSupport() {
    override fun complete(parameter: MethodParameter?, completionContext: CompletionContext?, hints: Array<out String>?): MutableList<CompletionProposal> {
        val currentInput = completionContext?.currentWord()
        val dbmd = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.metaData

        return DatabaseMetadata.getCatalogs(dbmd)
            .map{c -> c.catalogName}
            .filter{s -> s.contains(currentInput!!)}
            .map{cp -> CompletionProposal(cp)}
            .toMutableList()
    }
}