package com.zaneli.exposedgenerator.postgresql

import com.zaneli.exposedgenerator.core.Config
import com.zaneli.exposedgenerator.core.MetaDataReader

fun main(args: Array<String>) {
    if (args.size != 7) {
        throw IllegalArgumentException("usege: `./gradlew run --args \"localhost 5432 user_name password db_name schema_name table_name\"`")
    }

    // TODO: Extract to yaml file or else
    val host = args[0]
    val port = args[1]
    val userName = args[2]
    val password = args[3]
    val dbName = args[4]
    val schemaName = args[5]
    val tableName = args[6]
    val url = "jdbc:postgresql://${host}:${port}/${dbName}"
    val config = Config(
        url,
        userName,
        password,
        dbName,
        schemaName,
        "com.zaneli.exposedgenerator.example.entity",
        "com.zaneli.exposedgenerator.example.table",
        "com.zaneli.exposedgenerator.example.repository",
        "com.zaneli.exposedgenerator.example.repository.impl",
        setOf()
    )
    val metaData = MetaDataReader.read(config, tableName).getOrThrow()
    println("======= data class =======")
    println(metaData.toDataClass(config))
    println("======= data class =======")

    println("======= table class =======")
    println(metaData.toTableClass(config))
    println("======= table class =======")

    println("======= repository interface =======")
    println(metaData.toRepositoryInterface(config))
    println("======= repository interface =======")

    println("======= repository impl class =======")
    println(metaData.toRepositoryImplClass(config))
    println("======= repository impl class =======")
}
