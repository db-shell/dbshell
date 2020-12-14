package org.dbshell.providers

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class CliPromptProvider: PromptProvider {
    override fun getPrompt(): AttributedString {
        return AttributedString("CLI-DEMO:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
    }
}