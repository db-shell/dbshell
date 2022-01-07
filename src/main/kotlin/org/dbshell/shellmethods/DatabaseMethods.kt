package org.dbshell.shellmethods

import org.dbshell.actions.ActionExecutor
import org.dbshell.actions.db.*
import org.dbshell.shellmethods.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.providers.CatalogValueProvider
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

@ShellComponent
class DatabaseMethods: ActionExecutor {

    companion object {
        private val logger = LoggerFactory.getLogger(DatabaseMethods::class.java)
    }

    fun getCatalogAvailability(): Availability {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dbmd = connection.metaData
            if(DatabaseMetadata.getCatalogs(dbmd).isNotEmpty()) {
                return Availability.available()
            } else {
                return Availability.unavailable("This database connection does not contain any catalogs.")
            }
        }
    }

    fun getSchemaAvailability(): Availability {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dbmd = connection.metaData
            if(DatabaseMetadata.getSchemas(dbmd).isNotEmpty()) {
                return Availability.available()
            } else {
                return Availability.unavailable("This database connection does not contain any schemas.")
            }
        }
    }

    @ShellMethod("Sets the active Schema for a context and jndi if the driver supports this")
    @ShellMethodAvailability("getCatalogAvailability")
    fun setCurrentCatalog(
        @ShellOption(valueProvider = CatalogValueProvider::class) catalog: String,
        @ShellOption(defaultValue = "false") executeAsync: Boolean
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

            val types = mutableSetOf("TABLE")

            if(includeViews)
                types += "VIEW"

            if(includeAll) {
                types += "SYSTEM TABLE"
                types += "MATERIALIZED QUERY TABLE"
                types += "ALIAS"
            }

            val currentCatalog = EnvironmentVars.currentCatalog
            val currentSchema = EnvironmentVars.currentSchema
            val entries =
                DatabaseMetadata.getTables(connection.metaData, currentCatalog!!, currentSchema!!, types.toTypedArray())
            entries.sortBy {t -> t.tableName}
            val getTables = GetAllTables(entries)
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
            val currentCatalog = EnvironmentVars.currentCatalog
            val currentSchema = EnvironmentVars.currentSchema
            val entries = DatabaseMetadata.getColumns(connection.metaData, currentCatalog!!, currentSchema!!, table)
            val getAllTableColumns = GetAllTableColumns(table, entries)
            val result = executeAction(getAllTableColumns, executeAsync)
            renderResult(result)
        }
    }

    @ShellMethod("Generate DDL for table")
    fun getDdlForTable(
        @ShellOption(valueProvider = TableProvider::class) table: String,
        @ShellOption(defaultValue = "false") executeAsync: Boolean
    ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dslContext = DSL.using(connection, Settings().withRenderFormatted(true))
            val ddl = dslContext.ddl(dslContext.meta().getTables(table))
            val entries = ddl.queries().map{q -> q.sql}.toList()
            val getDDLForTable = GetDDLForTable(entries)
            val result = executeAction(getDDLForTable, executeAsync)
            renderResult(result)
        }
    }

    @ShellMethod("Generate DDL for database")
    fun getDdlForDatabase(scriptFile: File, @ShellOption(defaultValue = "false") executeAsync: Boolean) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dslContext = DSL.using(connection, Settings().withRenderFormatted(true))
            val ddl = dslContext.ddl(dslContext.meta().tables)
            val entries = ddl.queries().map{q -> q.sql}.toList()
            val getDDLForDb = GetDDLForDb(scriptFile, entries)
            val result = executeAction(getDDLForDb, executeAsync)
            renderResult(result)
        }
    }

    @ShellMethod("Run SQL script")
    fun runSqlScript(scriptFile: File, @ShellOption(defaultValue = "false") executeAsync: Boolean) {
        val runSqlScript = RunSqlScript(scriptFile)
        val result = executeAction( runSqlScript, executeAsync)
        renderResult(result)
    }
}