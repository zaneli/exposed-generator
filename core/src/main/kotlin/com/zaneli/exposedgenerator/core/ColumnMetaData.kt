package com.zaneli.exposedgenerator.core

data class ColumnMetaData(val columnName: String, val typeName: String, val columnSize: Int?) {
    val ktColumnName: String = columnName.toCamelCase()

    val ktTypeName: String
        get() {
            // TODO: Add import statement
            // TODO: Add custom type associated with name
            return when(typeName) {
                "varchar" -> "String"
                "uuid" -> "UUID"
                "timestamp" -> "LocalDateTime"
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

    private fun String.toCamelCase(): String {
        val pair = this.fold(listOf<String>() to false) { acc, c ->
            if (acc.second) {
               acc.first + c.uppercaseChar().toString() to false
            } else if (c == '_') {
                acc.first to true
            } else {
                acc.first + c.toString() to false
            }
        }
        return pair.first.joinToString("")
    }
}
