package org.dbshell.providers

import org.dbshell.environment.EnvironmentVars
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class CliPromptProvider: PromptProvider {
    override fun getPrompt(): AttributedString {
        val contextAndJndi = EnvironmentVars.currentContextAndJndi
        val catalog = EnvironmentVars.currentCatalog
        val schema = EnvironmentVars.currentSchema
        val currentContext = "context:${contextAndJndi.context}"
        val currentJndi = "jndi:${contextAndJndi.jndi}"

        val currentCatalog =
            if(catalog.isNullOrEmpty()) {
                "catalog:Not Set"
            } else {
                "catalog:$catalog"
            }

        val currentSchema =
            if(schema.isNullOrEmpty()) {
                "schema: Not Set"
            } else {
                "schema:$schema"
            }

        return AttributedString(
            "db-shell $currentContext::$currentJndi::$currentCatalog::$currentSchema :>",
            AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)
        )
    }
}