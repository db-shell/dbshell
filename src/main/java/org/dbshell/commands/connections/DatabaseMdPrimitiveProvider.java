package org.dbshell.commands.connections;

import org.apache.commons.io.FilenameUtils;
import org.bradfordmiller.simplejndiutils.JNDIUtils;
import org.dbshell.environment.ContextAndJndi;
import org.dbshell.environment.EnvironmentVars;
import org.dbshell.reflection.utils.DatabaseMetadataUtil;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseMdPrimitiveProvider extends ValueProviderSupport {
    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        String currentInput = completionContext.currentWordUpToCursor();

        ContextAndJndi ctxJndi = EnvironmentVars.Companion.getCurrentContextAndJndi();
        DatabaseMetaData dbmd = null;
        try {
            Connection connection = JNDIUtils.getJndiConnection(ctxJndi.getJndi(), ctxJndi.getContext());
            dbmd = connection.getMetaData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return DatabaseMetadataUtil.getPrimitivesFromDBMetadata(dbmd)
                .keySet()
                .stream()
                .filter(c -> c.contains(currentInput))
                .map(p -> FilenameUtils.removeExtension(new File(p).getName()))
                .map(CompletionProposal::new)
                .collect(Collectors.toList());
    }
}
