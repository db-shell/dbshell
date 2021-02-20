package org.dbshell.commands.connections

import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.db.metadata.TableProvider
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.dbshell.ui.TablesUtil
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellMethodAvailability
import org.springframework.shell.standard.ShellOption
import org.springframework.stereotype.Component
import java.util.*

@ShellComponent
class DatabaseManager {

    companion object {
        private val logger = LoggerFactory.getLogger(ConnectionManager::class.java)
    }

    fun getCatalogAvailability(): Availability {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use {connection ->
            val dbmd = connection.metaData
            if(DatabaseMetadata.getCatalogs(dbmd).size > 0) {
                return Availability.available()
            } else {
                return Availability.unavailable("This database connection does not contain any catalogs.")
            }
        }
    }

    fun getSchemaAvailability(): Availability {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use {connection ->
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
            EnvironmentVars.setCurrentCatalog(catalog)
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
            EnvironmentVars.setCurrentSchema(schema)
            EnvironmentProps.setCurrentSchema(schema)
        }
    }

    @ShellMethod("List all tables for the active connection and catalog")
    fun getTables(
        includeViews: Boolean = false,
        includeAll: Boolean = false
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

            val dbmd = connection.metaData
            val currentCatalog = EnvironmentVars.getCurrentCatalog()
            val currentSchema = EnvironmentVars.getCurrentSchema()
            val tableList = DatabaseMetadata.getTables(dbmd, currentCatalog, currentSchema, types.toTypedArray())
            tableList.sortBy {t -> t.tableName}
            val tableHeaders = LinkedHashMap<String, Any>()
            tableHeaders["tableName"] = "Table Name"
            tableHeaders["tableType"] = "Table Type"
            TablesUtil.renderAttributeTable(tableHeaders, tableList)
        }
    }

    @ShellMethod("Get column info for table")
    fun getTableColumns(
        @ShellOption(valueProvider = TableProvider::class) table: String
    ) {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dbmd = connection.metaData
            val currentCatalog = EnvironmentVars.getCurrentCatalog()
            val currentSchema = EnvironmentVars.getCurrentSchema()
            val columnList = DatabaseMetadata.getColumns(dbmd, currentCatalog, currentSchema, table)
            val columnHeaders = LinkedHashMap<String, Any>()
            columnHeaders["columnName"] = "Column Name"
            columnHeaders["typeName"] = "Type Name"
            columnHeaders["columnSize"] = "Column Size"
            columnHeaders["decimalDigits"] = "Decimal Digits"
            columnHeaders["precision"] = "Precision"
            columnHeaders["isNullable"] = "Is Nullable"
            columnHeaders["comments"] = "Comments"
            columnHeaders["defaultValue"] = "Default Value"
            columnHeaders["isPrimaryKey"] = "Is Primary Key"
            columnHeaders["isForeignKey"] = "Is Foreign Key"
            columnHeaders["foreignKeyDescription"] = "Foreign Key Description"
            TablesUtil.renderAttributeTable(columnHeaders, columnList)
        }
    }
}