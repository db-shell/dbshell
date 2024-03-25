package org.dbshell.providers

import org.dbshell.connections.ConnectionRepository
import org.dbshell.environment.EnvironmentVars
import org.dbshell.reflection.utils.DatabaseMetadataUtil
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProvider
import org.springframework.stereotype.Component

@Component
class DatabaseMdPrimitiveProvider: ValueProvider {
    override fun complete(
        completionContext: CompletionContext
    ): MutableList<CompletionProposal> {

        val currentInput = completionContext.currentWordUpToCursor()

        val currentConnectionName = EnvironmentVars.currentConnectionName
        val dbmd = ConnectionRepository.loadConnection(currentConnectionName).getConnection().metaData

        return DatabaseMetadataUtil.getPrimitivesFromDBMetadata(dbmd)
            .keys
            .filter{c -> c.contains(currentInput!!)}
            .map {cp -> CompletionProposal(cp)}
            .toMutableList()
    }
}