package org.dbshell.providers

import org.dbshell.environment.EnvironmentVars
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class CliPromptProvider: PromptProvider {
    override fun getPrompt(): AttributedString {
        val (envContext, envJndi) = EnvironmentVars.getCurrentContextAndJndi()

        val currentContext = if(envContext == null) "context:Not Set" else "context:$envContext"
        val currentJndi = if(envContext == null || envJndi == null) "jndi:Not Set" else "jndi:$envJndi"

        return AttributedString("db-shell $currentContext::$currentJndi :>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
    }
}