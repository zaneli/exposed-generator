package com.zaneli.exposedgenerator.core

import com.cesarferreira.pluralize.singularize

data class TableMetaData(val tableName: String, val columns: List<ColumnMetaData>, val primaryKeys: List<String>) {
    private val variableName = tableName.singularize().toCamelCase()
    private val className = variableName.capitalize()

    fun toDataClass(config: Config): String {
        return buildString {
            appendLine("package ${config.dataPackage}")
            appendLine()
            append("data class ${className}(")
            append(columns.joinToString(", ") {
                "val ${it.ktColumnName}: ${it.ktTypeName}"
            })
            append(")")
        }
    }

    fun toTableClass(config: Config): String {
        return buildString {
            appendLine("package ${config.tablePackage}")
            appendLine()
            appendLine("import org.jetbrains.exposed.sql.Table")
            appendLine()
            appendLine("object ${className}Table : Table(\"${tableName}\") {")
            columns.forEach {
                appendLine("  val ${it.ktColumnName} = ${it.dbTypeName}(\"${it.columnName}${it.tableTypeAppendix}\")")
            }
            if (primaryKeys.size == 1) {
                appendLine()
                appendLine("  override val primaryKey = PrimaryKey(${primaryKeys[0]})")
            } else if (primaryKeys.isNotEmpty()) {
                // TODO
            }
            append("}")
        }
    }

    fun toRepositoryInterface(config: Config): String {
        return buildString {
            appendLine("package ${config.repositoryPackage}")
            appendLine()
            appendLine("interface ${className}Repository {")
            appendLine("  fun create(${variableName}: ${className})")
            append("}")
        }
    }

    fun toRepositoryImplClass(config: Config): String {
        return buildString {
            appendLine("package ${config.repositoryImplPackage}")
            appendLine()
            appendLine("class ${className}RepositoryImpl : ${className}Repository {")
            appendLine("  override fun create(${variableName}: ${className})")
            appendLine("    transaction {")
            appendLine("      ProductTable.insert {")
            columns.forEach {
                appendLine("        it[${it.ktColumnName}] = ${variableName}.${it.ktColumnName}")
            }
            appendLine("      }")
            appendLine("    }")
            append("}")
        }
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.uppercaseChar() else it
        }
    }
}
