/*package org.dbshell

import org.springframework.shell.CommandRegistry
import org.springframework.shell.MethodTarget
import org.springframework.shell.Shell
import org.springframework.util.ReflectionUtils.invokeMethod
import javax.validation.constraints.NotNull

open class BaseCommandTest {
    protected operator fun <T> invoke(methodTarget: MethodTarget, vararg args: Any?): T {
        return invokeMethod(methodTarget.method, methodTarget.bean, args) as T
    }

    protected fun lookupCommand(
        @NotNull registry: CommandRegistry,
        @NotNull command: String?
    ): MethodTarget? {
        return registry.listCommands()[command]
    }

    protected fun lookupCommand(
        @NotNull shell: Shell,
        @NotNull command: String?
    ): MethodTarget? {
        return shell.listCommands()[command]
    }
}
*/