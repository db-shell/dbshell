package org.dbshell.db.metadata

import org.dbshell.db.metadata.dto.Schema
import org.dbshell.db.metadata.dto.Catalog
import org.dbshell.db.metadata.dto.Column
import org.dbshell.db.metadata.dto.Table
import java.sql.DatabaseMetaData

class DatabaseMetadata {
    companion object {
        fun getSchemas(dbmd: DatabaseMetaData): List<Schema> {
            val schemaList = arrayListOf<Schema>()
            dbmd.schemas.use {rs ->
                while (rs.next()) {
                    schemaList.add(Schema(rs.getString(1)))
                }
            }
            return schemaList
        }
        fun getCatalogs(dbmd: DatabaseMetaData): List<Catalog> {
            val catalogList = arrayListOf<Catalog>()
            dbmd.catalogs.use {rs ->
                while (rs.next()) {
                    catalogList.add(Catalog(rs.getString(1)))
                }
            }
            return catalogList
        }
        fun getTables(dbmd: DatabaseMetaData, catalog: String, schema: String, types: Array<String>): MutableList<Table> {
            val tableList = arrayListOf<Table>()
            dbmd.getTables(catalog, schema, null, types).use {rs ->
                while(rs.next()) {
                    tableList.add(Table(rs.getString(3), rs.getString(4)))
                }
            }
            return tableList
        }
        fun getColumns(dbmd: DatabaseMetaData, catalog: String, schema: String, table: String): List<Column> {
            val primaryKeys = HashSet<String>()
            dbmd.getPrimaryKeys(catalog, schema, table).use {rs ->
                while(rs.next()) {
                    primaryKeys.add(rs.getString("COLUMN_NAME"))
                }
            }
            val foreignKeys = HashMap<String, String>()
            dbmd.getExportedKeys(catalog, schema, table).use {rs ->
                while(rs.next()) {
                    val pkColumn = rs.getString("PKCOLUMN_NAME")
                    foreignKeys[pkColumn] = rs.getString("FKTABLE_NAME") + "--->" + rs.getString("FKCOLUMN_NAME")
                }
            }
            val columnList = arrayListOf<Column>()
            dbmd.getColumns(catalog, schema, table, null).use {rs ->
                while(rs.next()) {
                    val columnName = rs.getString(4)
                    val isPk = primaryKeys.contains(columnName)
                    var isFk = false
                    var fkDesc: String? = ""
                    if(foreignKeys.contains(columnName)) {
                        isFk = true
                        fkDesc = foreignKeys[columnName]
                    }
                    columnList.add(
                        Column(
                        columnName,
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getBoolean(11),
                        rs.getString(12),
                        rs.getString(13),
                        isPk,
                        isFk,
                        fkDesc!!
                        )
                    )
                }
            }
            return columnList
        }
    }
}