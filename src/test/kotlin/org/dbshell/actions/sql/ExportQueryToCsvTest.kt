package org.dbshell.actions.sql

import org.apache.spark.sql.SparkSession
import org.dbshell.environment.EnvironmentVars
import org.dbshell.shellmethods.SqlMethods
import org.jline.console.CommandRegistry
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.shell.InputProvider
import org.springframework.shell.Shell
import org.springframework.shell.command.ArgumentHeaderMethodArgumentResolver
import org.springframework.shell.command.CommandContextMethodArgumentResolver
import org.springframework.shell.command.CommandExecution
import org.springframework.shell.command.CommandRegistration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.util.ReflectionUtils.findMethod
import java.io.File
import javax.naming.Context


@RunWith(SpringJUnit4ClassRunner::class)
class ExportQueryToCsvTest {

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

    private var execution: CommandExecution? = null

    @BeforeEach
    fun setupCommandExecutionTests() {
        val conversionService: ConversionService = DefaultConversionService()
        val resolvers: MutableList<HandlerMethodArgumentResolver> = ArrayList<HandlerMethodArgumentResolver>()
        resolvers.add(ArgumentHeaderMethodArgumentResolver(conversionService, null))
        resolvers.add(CommandContextMethodArgumentResolver())
        execution = CommandExecution.of(resolvers, null, null, conversionService)
    }

    @Mock
    private lateinit var inputProvider: InputProvider

    @Mock
    private lateinit var commandRegistry: CommandRegistry

    @InjectMocks
    private lateinit var shell: Shell

    @Test
    fun testSpark() {

        val spark =
            SparkSession
                .builder()
                .master("local[2]")
                .appName("db-shell - query to file")
                .orCreate

        println(spark)
    }

    @Test
    fun testExportTooCsv() {

        val connCmd = "set-active-connection"
        val cmdActiveConn =
            CommandRegistration.builder()
                .command(connCmd)
                .withTarget()
                    .method(this, connCmd)
                    .and()
                .build()

        val context = "default_ds"
        val jndi = "SqliteChinook"

        val commandSql = "export-query-to-csv"
        val cmdSql =
            CommandRegistration.builder()
                .command(commandSql)
                .withTarget()
                    .method(this, commandSql)
                    .and()
                .build()

        val connResult = execution?.evaluate(arrayOf(connCmd, "--context", context, "--jndi", jndi))
        println(connResult)

        val sql = "SELECT * FROM albums"
        val file = "src/test/resources/data/outputData/csvtest.csv"

        val result = execution?.evaluate(arrayOf(commandSql, "--sql", sql, "--output-file", file))
        println(result)

    }
}