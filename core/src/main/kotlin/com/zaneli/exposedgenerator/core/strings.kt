package com.zaneli.exposedgenerator.core

fun String.toCamelCase(): String {
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
