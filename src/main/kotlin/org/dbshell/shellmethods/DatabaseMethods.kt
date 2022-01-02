package org.dbshell.shellmethods

import org.apache.commons.io.FileUtils
import org.dbshell.actions.ActionExecutor
import org.dbshell.actions.db.GetAllTableColumns
import org.dbshell.actions.db.GetAllTables
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.providers.CatalogValueProvider
import org.dbshell.ui.TablesUtil
import org.dbshell.providers.SchemaValueProvider
import org.dbshell.providers.TableProvider

import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.slf4j.LoggerFactory

import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellMethodAvailability
import org.springframework.shell.standard.ShellOption

import java.io.File
import java.nio.file.Files
import java.util.*
import org.dbshell.utils.ScriptRunner

@ShellComponent
class DatabaseMethods: ActionExecutor {

    companion object {
        private val logger = LoggerFactory.getLogger(DatabaseMethods::class.java)
    }

    fun getCatalogAvailability(): Availability {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dbmd = connection.metaData
            if(DatabaseMetadata.getCatalogs(dbmd).size > 0) {
                return Availability.available()
            } else {
                return Availability.unavailable("This database connection does not contain any catalogs.")
            }
        }
    }

    fun getSchemaAvailability(): Availability {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dbmd = connection.metaData
            if(DatabaseMetadata.getSchemas(dbmd).size > 0) {
                return Availability.available()
            } else {
                return Availability.unavailable("This database connection does not contain any schemas.")
            }
        }
    }

    @ShellMethod("Sets the active Schema for a context and jndi if the driver supports this")
    @ShellMethodAvailability("getCatalogAvailability")
    fun setCurrentCatalog(
        @ShellOption(valueProvider = CatalogValueProvider::class) catalog: String
        ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            connection.catalog = catalog
            EnvironmentVars.currentCatalog = catalog
            EnvironmentProps.setCurrentCatalog(catalog)
        }
    }

    @ShellMethod("Sets the active schema for a context and jndi if the driver supports this")
    @ShellMethodAvailability("getSchemaAvailability")
    fun setCurrentSchema(
        @ShellOption(valueProvider = SchemaValueProvider::class) schema: String
        ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            connection.schema = schema
            EnvironmentVars.currentSchema = schema
            EnvironmentProps.setCurrentSchema(schema)
        }
    }

    @ShellMethod("List all tables for the active connection and catalog")
    fun getTables(
        includeViews: Boolean = false,
        includeAll: Boolean = false,
        @ShellOption(defaultValue = "false") executeAsync: Boolean
    ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val getTables = GetAllTables(connection.metaData, includeViews, includeAll)
            val result = executeAction(getTables, executeAsync)
            renderResult(result)
        }
    }

    @ShellMethod("Get column info for table")
    fun getTableColumns(
        @ShellOption(valueProvider = TableProvider::class) table: String,
        @ShellOption(defaultValue = "false") executeAsync: Boolean
    ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val getAllTableColumns = GetAllTableColumns(table, connection.metaData)
            val result = executeAction(getAllTableColumns, executeAsync)
            renderResult(result)
        }
    }

    @ShellMethod("Generate DDL for table")
    fun getDdlForTable(
        @ShellOption(valueProvider = TableProvider::class) table: String
    ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dslContext = DSL.using(connection, Settings().withRenderFormatted(true))
            val ddl = dslContext.ddl(dslContext.meta().getTables(table))
            ddl.queries().forEach {q -> println(q.sql)}
        }
    }

    @ShellMethod("Generate DDL for database")
    fun getDdlForDatabase(scriptFile: File) {
        println("Generating ddl for current database connection...")
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dslContext = DSL.using(connection, Settings().withRenderFormatted(true))
            val ddl = dslContext.ddl(dslContext.meta().tables)
            ddl.queries().forEach {q ->
                FileUtils.writeStringToFile(scriptFile, q.sql, "ISO-8859-1", true)
            }
        }
        println("Generating ddl complete. Please see file ${scriptFile.absolutePath}.")
    }

    @ShellMethod("Run sql script")
    fun runSqlScript(scriptFile: File) {
        println("Executing script ${scriptFile.absolutePath}...")
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val content = String(Files.readAllBytes(scriptFile.toPath()))
            ScriptRunner.executeScript(content, connection)
        }
        println("Execution of script complete.")
    }
}