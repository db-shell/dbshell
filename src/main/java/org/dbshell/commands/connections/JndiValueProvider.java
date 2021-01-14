package org.dbshell.commands.connections;

import org.bradfordmiller.simplejndiutils.JNDIUtils;
import org.osjava.sj.jndi.MemoryContext;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JndiValueProvider extends ValueProviderSupport {

    @Override
    public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
        List<String> words = completionContext.getWords();

        int wordIdx = completionContext.getWordIndex() - 1;
        String word = words.get(wordIdx);

        if(word.equals("--jndi") && words.contains("--context"))
            return true;
        else
            return false;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        String currentInput = completionContext.currentWord();
        List<String> words = completionContext.getWords();
        List<CompletionProposal> proposals = new ArrayList<>();

        InitialContext initCtx = null;
        try {
            initCtx = new InitialContext();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        int contextIdx = words.indexOf("--context") + 1;
        String context = words.get(contextIdx);
        MemoryContext mc = JNDIUtils.getMemoryContextFromInitContext(initCtx, context);

        if(context.equals("") || mc == null) {
            return proposals;
        }

        try {
            proposals =
                JNDIUtils.getEntriesForJndiContext(mc)
                    .keySet()
                    .stream()
                    .filter(c -> c.contains(currentInput))
                    .map(CompletionProposal::new)
                    .collect(Collectors.toList());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return proposals;
    }
}
