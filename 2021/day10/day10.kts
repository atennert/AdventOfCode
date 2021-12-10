#!/usr/bin/env kscript

import kotlin.math.floor

var syntaxErrorScore = 0
var autocompleteScores = mutableListOf<Long>()
lineLoop@ while (true) {
    val line = readlnOrNull() ?: break
    val buffer = mutableListOf<Char>()
    for (c in line) {
        when (c) {
            ')' -> if (buffer.removeLast() != '(') {
                syntaxErrorScore += 3
                continue@lineLoop
            }
            ']' -> if (buffer.removeLast() != '[') {
                syntaxErrorScore += 57
                continue@lineLoop
            }
            '}' -> if (buffer.removeLast() != '{') {
                syntaxErrorScore += 1197
                continue@lineLoop
            }
            '>' -> if (buffer.removeLast() != '<') {
                syntaxErrorScore += 25137
                continue@lineLoop
            }
            else -> buffer.add(c)
        }
    }
    autocompleteScores.add(buffer.map {
        when (it) {
            '(' -> 1L
            '[' -> 2L
            '{' -> 3L
            '<' -> 4L
            else -> error("Unknown character $it")
        }
    }.reduceRight { n, acc -> acc * 5 + n })
}

fun <T> List<T>.median() = this[floor(autocompleteScores.size / 2.0).toInt()]

println("Syntax error score: $syntaxErrorScore")
println("Autocomplete score: ${autocompleteScores.sorted().median()}")