package org.dbshell.providers

import org.dbshell.environment.EnvironmentVars
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class CliPromptProvider: PromptProvider {
    override fun getPrompt(): AttributedString {
        val contextAndJndi = EnvironmentVars.getCurrentContextAndJndi()
        val catalog = EnvironmentVars.getCurrentCatalog()

        val currentContext =
            if(contextAndJndi.context == null)
                "context:Not Set"
            else
                "context:${contextAndJndi.context}"

        val currentJndi =
            if(contextAndJndi.context == null || contextAndJndi.jndi == null)
                "jndi:Not Set"
            else
                "jndi:${contextAndJndi.jndi}"

        val currentCatalog =
            if(catalog.isNullOrEmpty()) {
                "catalog:Not Set"
            } else {
                "catalog:$catalog"
            }

        return AttributedString("db-shell $currentContext::$currentJndi::$currentCatalog :>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
    }
}