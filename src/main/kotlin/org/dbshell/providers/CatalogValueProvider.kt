package org.dbshell.providers

import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class CatalogValueProvider: ValueProvider {
    override fun complete(
        completionContext: CompletionContext?
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext?.currentWord()
        val dbmd = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.metaData

        return DatabaseMetadata.getCatalogs(dbmd)
            .map{c -> c.catalogName}
            .filter{s -> s.contains(currentInput!!)}
            .map{cp -> CompletionProposal(cp)}
            .toMutableList()
    }
}