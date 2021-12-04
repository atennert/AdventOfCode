#!/usr/bin/env kscript

var mostCommon = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
val values = mutableListOf<String>()
while (true) {
    val input = readlnOrNull() ?: break

    mostCommon = input.toCharArray()
        .map(Char::digitToInt)
        .map { if (it == 0) -1 else it }
        .zip(mostCommon) { a, b -> a + b }

    values.add(input)
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

fun List<String>.sortByPlace(place: Int): Pair<String, String> {
    val (zeroes, ones) = this.partition { it[place] == '0' }
    val (big, small) = if (zeroes.size > ones.size)
        Pair(zeroes, ones)
    else
        Pair(ones, zeroes)
    return Pair(
        big.getRemaining(place, Pair<String, String>::first),
        small.getRemaining(place, Pair<String, String>::second)
    )
}

fun List<String>.getRemaining(place: Int, selector: (Pair<String, String>) -> String): String {
    return when (this.size) {
        0 -> "" // should be ignored
        1 -> this[0]
        else -> selector(this.sortByPlace(place + 1))
    }
}

fun String.toIntFromBinary(): Int {
    var value = 0
    this.forEach {
        value = value.shl(1)
        if (it == '1') {
            value = value.or(1)
        }
    }
    return value
}

val (oxygenGeneratorRating, scrubberRating) = values.sortByPlace(0)

println(oxygenGeneratorRating.toIntFromBinary() * scrubberRating.toIntFromBinary())
