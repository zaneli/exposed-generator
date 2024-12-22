package com.zaneli.exposedgenerator.core

import java.sql.DriverManager

object MetaDataReader {
    fun read(config: Config, tableName: String): Result<TableMetaData> {
        return DriverManager.getConnection(config.url, config.user, config.password).use { con ->
            val primaryKeys = con.metaData.getPrimaryKeys(config.dbName, config.schemaName, tableName).use { rs ->
                val primaryKeys = mutableListOf<String>()
                while (rs.next()) {
                    primaryKeys.add(rs.getString("COLUMN_NAME"))
                }
                primaryKeys.toList()
            }

            con.metaData.getColumns(config.dbName, config.schemaName, tableName,"%").use { rs ->
                var rsTableName: String? = null
                val columns = mutableListOf<ColumnMetaData>()
                while (rs.next()) {
                    rsTableName = rs.getString("TABLE_NAME") ?: return Result.failure(IllegalStateException())
                    if (tableName != rsTableName) {
                        return Result.failure(IllegalStateException())
                    }

                    val columnName = rs.getString("COLUMN_NAME") ?: return Result.failure(IllegalStateException())
                    if (config.ignoreColumns.contains(columnName)) {
                        continue
                    }
                    val typeName = rs.getString("TYPE_NAME")  ?: return Result.failure(IllegalStateException())
                    val columnSize = rs.getInt("COLUMN_SIZE")
                    columns.add(ColumnMetaData(columnName, typeName, columnSize))
                }
                rsTableName?.let { Result.success(TableMetaData(it, columns, primaryKeys)) } ?: Result.failure(IllegalStateException())
            }
        }
    }
}
