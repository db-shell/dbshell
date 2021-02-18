package org.dbshell.db.metadata;

import org.dbshell.commands.connections.dto.ConnectionInfo;
import org.dbshell.commands.connections.dto.ConnectionInfoUtil;
import org.dbshell.environment.EnvironmentVars;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableProvider extends ValueProviderSupport {

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        String currentInput = completionContext.currentWord();
        List<CompletionProposal> proposals = new ArrayList<>();
        String[] types = new String[] {"TABLE"};

        try {
            DatabaseMetaData dbmd = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().getConnection().getMetaData();
            String catalog = EnvironmentVars.getCurrentCatalog();
            String schema = EnvironmentVars.getCurrentSchema();

            return DatabaseMetadata.getTables(dbmd, catalog, schema, types)
                    .stream()
                    .map(t -> t.getTableName())
                    .filter(s -> s.contains(currentInput))
                    .map(CompletionProposal::new)
                    .collect(Collectors.toList());

        } catch(SQLException sqlEx) {
            sqlEx.getMessage();
            return proposals;
        }
    }
}
