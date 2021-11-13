package org.dbshell.providers

import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.environment.EnvironmentVars
import org.springframework.core.MethodParameter
import org.springframework.shell.CompletionContext
import org.springframework.shell.CompletionProposal
import org.springframework.shell.standard.ValueProviderSupport

class TableProvider: ValueProviderSupport() {
    override fun complete(parameter: MethodParameter?, completionContext: CompletionContext?, hints: Array<out String>?): MutableList<CompletionProposal> {
        val currentInput = completionContext?.currentWord()
        val types = arrayOf("TABLE")
        val dbmd = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.metaData
        val catalog = EnvironmentVars.currentCatalog
        val schema = EnvironmentVars.currentSchema

        return DatabaseMetadata.getTables(dbmd, catalog!!, schema!!, types)
                .map{ t -> t.tableName}
                .filter{ s -> s.contains(currentInput.toString())}
                .map{cp -> CompletionProposal(cp)}
                .toMutableList()
    }
}