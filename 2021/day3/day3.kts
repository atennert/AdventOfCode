#!/usr/bin/env kscript

var mostCommon = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
while (true) {
    mostCommon = readlnOrNull()
        ?.toCharArray()
        ?.map(Char::digitToInt)
        ?.map { if (it == 0) -1 else it }
        ?.zip(mostCommon) { a, b -> a + b } ?: break
}

var gammaRate = 0
var epsilonRate = 0
mostCommon.forEach { i ->
    gammaRate = gammaRate.shl(1)
    epsilonRate = epsilonRate.shl(1)
    if (i > 0) {
        gammaRate = gammaRate.or(1)
    } else {
        epsilonRate = epsilonRate.or(1)
    }
}

println(gammaRate * epsilonRate)