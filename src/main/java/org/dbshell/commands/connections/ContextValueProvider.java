package org.dbshell.commands.connections;

import org.bradfordmiller.simplejndiutils.JNDIUtils;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;

@Component
class ContextValueProvider extends ValueProviderSupport {
    
    @Override
    public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
        return true;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        String currentInput = completionContext.currentWordUpToCursor();
        List<CompletionProposal> proposals = null;
        try {
            proposals =
                JNDIUtils.getAvailableJndiContexts(null)
                 .stream()
                 .filter(c -> c.contains(currentInput))
                 .map(p -> new File(p).getName())
                 .map(CompletionProposal::new)
                 .collect(Collectors.toList());

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proposals;
    }
}