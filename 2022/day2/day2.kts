#!/usr/bin/env kscript

val scores1 = mapOf(
    "A X" to 3 + 1,
    "A Y" to 6 + 2,
    "A Z" to 0 + 3,
    "B X" to 0 + 1,
    "B Y" to 3 + 2,
    "B Z" to 6 + 3,
    "C X" to 6 + 1,
    "C Y" to 0 + 2,
    "C Z" to 3 + 3,
)

val scores2 = mapOf(
    "A X" to 0 + 3,
    "A Y" to 3 + 1,
    "A Z" to 6 + 2,
    "B X" to 0 + 1,
    "B Y" to 3 + 2,
    "B Z" to 6 + 3,
    "C X" to 0 + 2,
    "C Y" to 3 + 3,
    "C Z" to 6 + 1,
)

var myScore1 = 0
var myScore2 = 0
while (true) {
    val round = readlnOrNull() ?: break
    myScore1 += scores1.getValue(round)
    myScore2 += scores2.getValue(round)
}

println(myScore1)
println(myScore2)
