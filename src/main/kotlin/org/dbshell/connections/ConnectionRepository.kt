package org.dbshell.connections

import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/*
CREATE TABLE "sqlb_temp_table_1" (
	"name"	INTEGER,
	"url"	TEXT,
	"user"	TEXT,
	"password"	TEXT,
	PRIMARY KEY("name")
);
 */

data class DbConnection(val url: String, val user: String, val password: String) {
    fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
}
class ConnectionRepository {
    companion object {
        val properties = Properties()

        init {
            val file = this::class.java.classLoader.getResourceAsStream("application.properties")
            properties.load(file)
        }

        private fun getConnection(): Connection {
            return DriverManager.getConnection(properties.getProperty("database"))
        }

        fun loadConnection(name: String): DbConnection {
            val sql = "SELECT url, user, password from connections WHERE name = ?"
            getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name)
                    pstmt.executeQuery().use { rs ->
                        return DbConnection(
                            rs.getString("url"),
                            rs.getString("user"),
                            rs.getString("password")
                        )
                    }
                }
            }
        }

        fun loadAllConnections(): Map<String, DbConnection> {
            val connections = mutableMapOf<String, DbConnection>()
            val sql = "SELECT name, url, user, password from connections ORDER BY name ASC "
            getConnection().use { conn ->
                conn.createStatement().use {stmt ->
                    stmt.executeQuery(sql).use {rs ->
                        connections.put(
                            rs.getString("name"),
                            DbConnection(
                                rs.getString("url"),
                                rs.getString("user"),
                                rs.getString("password")
                            )
                        )
                    }
                }
            }
            return connections
        }

        fun addConnection(name: String, url: String, user: String, password: String): Int {
            val sql = "INSERT into connections (name, url, user, password) VALUES (?, ?, ?, ?)"
            getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name)
                    pstmt.setString(2, url)
                    pstmt.setString(3, user)
                    pstmt.setString(4, password)
                    return pstmt.executeUpdate()
                }
            }
        }

        fun updateConnection(name: String, url: String, user: String, password: String): Int {
            val sql = "Update connections Set url = ?, user=? , password=? WHERE name=?) VALUES (?, ?, ?, ?)"
            getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, url)
                    pstmt.setString(2, user)
                    pstmt.setString(3, password)
                    pstmt.setString(4, name)
                    return pstmt.executeUpdate()
                }
            }
        }

        fun deleteConnection(name: String): Int {
            val sql = "DELETE from connections where name = ?"
            getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, name)
                    return pstmt.executeUpdate()
                }
            }
        }
    }
}