package org.dbshell.commands.connections;

import com.github.mnadeem.TableNameParser;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SqlValueProvider extends ValueProviderSupport {
    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        String currentInput = completionContext.currentWordUpToCursor();
        return new TableNameParser(currentInput).tables()
                .stream()
                .filter(s -> s.contains(currentInput))
                .map(CompletionProposal::new)
                .collect(Collectors.toList());



    }
}
