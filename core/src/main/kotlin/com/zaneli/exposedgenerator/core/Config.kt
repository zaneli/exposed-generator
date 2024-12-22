package com.zaneli.exposedgenerator.core

data class Config(
    val url: String,
    val user: String,
    val password: String,
    val dbName: String,
    val schemaName: String,
    val dataPackage: String,
    val tablePackage: String,
    val repositoryPackage: String,
    val repositoryImplPackage: String,
    val ignoreColumns: Set<String>
)
