package org.dbshell.commands.connections;

import org.dbshell.commands.connections.dto.ConnectionInfoUtil;
import org.dbshell.db.metadata.DatabaseMetadata;
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
public class SchemaValueProvider extends ValueProviderSupport {
    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {

        String currentInput = completionContext.currentWord();
        List<CompletionProposal> proposals = new ArrayList<>();

        try {
            DatabaseMetaData dbmd = ConnectionInfoUtil.getConnectionFromCurrentContextJndi().getConnection().getMetaData();

            return DatabaseMetadata.getSchemas(dbmd)
                    .stream()
                    .map(s -> s.getSchema())
                    .filter(s -> s.contains(currentInput))
                    .map(CompletionProposal::new)
                    .collect(Collectors.toList());

        } catch(SQLException sqlEx) {
            sqlEx.getMessage();
            return proposals;
        }
    }
}