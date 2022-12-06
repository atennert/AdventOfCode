#!/usr/bin/env kscript

fun findDistinct(line: String, count: Int): Int {
    for (i in 0..line.length - count) {
        if (line.substring(i until i + count).toSet().size == count) {
            return i + count
        }
    }
    return -1
}

val line = readlnOrNull() ?: ""
println(findDistinct(line, 4))
println(findDistinct(line, 14))
