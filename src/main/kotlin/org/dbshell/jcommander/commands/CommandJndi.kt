package org.dbshell.jcommander.commands

import com.beust.jcommander.DynamicParameter
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.beust.jcommander.SubParameter
import org.dbshell.jcommander.parameters.AddJndi
import java.util.HashMap

@Parameters(commandDescription = "JNDI Operations for database/csv connections")
class CommandJndi {
    @Parameter(names = ["-lc","--listContexts"], help = true, description = "List all jndi contexts in the current environment")
    var listContents = false

    @Parameter(names = ["-le", "--listEntries"], help = true, description = "List all entries for jndi context")
    var context: String? = null

    @Parameter(names = ["-ed", "--entryDetails"], arity = 2, help = true, description = "Get the entry details for a specific jndi name")
    var jndiEntry: List<String> = listOf()

    @Parameter(names = ["-a", "--addJndi"], arity = 2, help = true, description = "Add new JNDI entry")
    var addJndi: AddJndi? = null

    @DynamicParameter(names = ["-D"], description = "Key/Value JNDI pairs")
    var params: Map<String, String> = HashMap()
}

class AddJndiEntry {
    @Parameter(order = 0, names=["--name", "-n"], help = true, description = "Jndi name for entry")
    var jndiName: String? = null

    @Parameter(order = 1, names=["--context", "-c"], help = true, description = "Jndi context for entry")
    var contextName: String? = null

    @DynamicParameter(names = ["-D"], description = "Key/Value JNDI pairs")
    var params: Map<String, String> = HashMap()
}