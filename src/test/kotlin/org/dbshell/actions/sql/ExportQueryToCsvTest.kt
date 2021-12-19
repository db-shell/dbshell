package org.dbshell.actions.sql

import org.dbshell.BaseCommandTest
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.shellmethods.SqlMethods
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.shell.ConfigurableCommandRegistry
import org.springframework.shell.Shell
import org.springframework.shell.jline.InteractiveShellApplicationRunner
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.io.File
import javax.naming.Context
import org.springframework.util.ReflectionUtils.findMethod

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(properties = [InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=" + false ])
class ExportQueryToCsvTest: BaseCommandTest() {

    init {
        if (!File("conf/jndi.properties").isFile) {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.osjava.sj.SimpleContextFactory")
            System.setProperty("org.osjava.sj.jndi.shared", "true")
            System.setProperty("org.osjava.sj.root", "src/deploy/bin/conf/jndi")
            System.setProperty("org.osjava.sj.colon.replace", "--")
            System.setProperty("org.osjava.sj.delimiter", "/")
        }

        val context = "default_ds"
        val jndi = "SqliteChinook"

        EnvironmentVars.currentContextAndJndi(context, jndi)
    }

    private val COMMAND_CLASS_UNDER_TEST: Class<SqlMethods> = SqlMethods::class.java

    @Autowired
    private val shell: Shell? = null

    @Test
    fun testExportTooCsv() {
        val command = "export-query-to-csv"
        val commandMethod = "exportQueryToCsv"

        val commandTarget = lookupCommand(shell!!, command)
        val group = commandTarget?.group
        val help = commandTarget?.help
        val commandMethodRunTime = commandTarget?.method
        val isAvailable = commandTarget?.availability?.isAvailable

        shell.evaluate{"set-active-connection --context default_ds --jndi SqliteChinook"}
        val sql = "SELECT\t*\tFROM\talbums"
        val result = shell.evaluate { "$command --sql $sql --output-file src/test/resources/data/outputData/csvtest.csv" }
        println(result)

        assert(commandTarget != null)
        assert(group == "Sql Methods")
        assert(help == "Export SQL query to csv")
        assert(commandMethodRunTime!! ==
                findMethod(
                    COMMAND_CLASS_UNDER_TEST,
                    commandMethod,
                    String::class.java,
                    File::class.java,
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    Boolean::class.java,
                    Boolean::class.java
                )
        )
        assert(isAvailable == true)


    }

}