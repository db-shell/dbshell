package org.dbshell.commands.connections

import org.dbshell.commands.connections.dto.ConnectionInfoUtil
import org.dbshell.db.metadata.DatabaseMetadata
import org.dbshell.environment.EnvironmentProps
import org.dbshell.environment.EnvironmentVars
import org.slf4j.LoggerFactory
import org.springframework.shell.Availability
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellMethodAvailability
import org.springframework.shell.standard.ShellOption

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

    @ShellMethod("List all databases for the active connection")
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

    @ShellMethod("List all tables for the active connection and catalog")
    fun getTables() {
        ConnectionInfoUtil.getConnectionFromCurrentContextJndi().connection.use { connection ->
            val dbmd = connection.metaData
            val currentCatalog = EnvironmentVars.getCurrentCatalog()
            val tableList = DatabaseMetadata.getTables(dbmd, currentCatalog)
            tableList.forEach{t -> println(t)}
        }
    }
}