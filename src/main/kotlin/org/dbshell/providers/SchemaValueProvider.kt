package org.dbshell.providers

import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport

class SchemaValueProvider: ValueProviderSupport() {
    override fun complete(
        parameter: MethodParameter?,
        completionContext: CompletionContext?,
        hints: Array<out String>?
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext?.currentWord()
        val dbmd = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.metaData

        return DatabaseMetadata.getSchemas(dbmd)
                 .map{s -> s.schemaName}
                 .filter{s -> s.contains(currentInput.toString())}
                 .map{cp -> CompletionProposal(cp)}
                 .toMutableList()
    }
}