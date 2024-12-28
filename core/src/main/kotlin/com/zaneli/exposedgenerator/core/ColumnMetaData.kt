package com.zaneli.exposedgenerator.core

data class ColumnMetaData(val columnName: String, val typeName: String, val columnSize: Int?) {
    val ktColumnName: String = columnName.toCamelCase()

    val ktTypeName: String
        get() {
            // TODO: Add import statement
            // TODO: Add custom type associated with name
            return when(typeName) {
                "varchar" -> "String"
                "uuid" -> "java.util.UUID"
                "int4" -> "Int"
                "timestamp" -> "LocalDateTime"
                else -> typeName
            }
        }

    val dbTypeName: String
        get() {
            return when(typeName) {
                "int4" -> "integer"
                else -> typeName
            }
        }

    val tableTypeAppendix: String
        get() {
            return when (typeName) {
                "varchar" -> columnSize?.let { ", $it" } ?: ""
                else -> ""
            }
        }
}
