package org.dbshell.commands.connections;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TableTypesProvider extends ValueProviderSupport {
    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        Set<String> dict = new HashSet<>(Arrays.asList("TABLE","VIEW","SYSTEM TABLE", "MATERIALIZED QUERY TABLE", "ALIAS"));
        String currentInput = completionContext.currentWord();
        String[] currentValue = currentInput.split(",");

        return dict
            .stream()
            .filter(k -> k.contains(currentInput))
            .map(CompletionProposal::new)
            .collect(Collectors.toList());
    }
}
