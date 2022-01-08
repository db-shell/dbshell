package org.dbshell.providers

import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport
import org.springframework.stereotype.Component

@Component
class SchemaValueProvider: ValueProviderSupport() {
    override fun complete(
        parameter: MethodParameter?,
        completionContext: CompletionContext?,
        hints: Array<out String>?
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext?.currentWord()
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use {conn ->
            return DatabaseMetadata.getSchemas(conn.metaData)
                .map { s -> s.schemaName }
                .filter { s -> s.contains(currentInput.toString()) }
                .map { cp -> CompletionProposal(cp) }
                .toMutableList()
        }
    }
}