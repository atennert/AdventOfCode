#!/usr/bin/env kscript

import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.max

class Player(var field: Int) {
    var score = 0
}

fun readPlayer() = readln()
    .replace(Regex(".*: (\\d+)"), "$1")
    .let { Player(it.toInt() - 1) }

val players = listOf(
    readPlayer(),
    readPlayer()
)

//val players = Files.readAllLines(Path.of("example.txt"))
//    .map { Player(it.replace(Regex(".*: (\\d+)"), "$1").toInt() - 1) }

val players2 = listOf(
    Player(players[0].field),
    Player(players[1].field)
)

var deterministicDice = 1
var diceRolls = 0
var playerIndex = 0
while (players.maxOf { it.score } < 1000) {
    val player = players[playerIndex]
    for (i in 1..3) {
        player.field = (player.field + deterministicDice++) % 10
    }
    player.score += player.field + 1
    playerIndex = (playerIndex + 1) % players.size
    diceRolls += 3
}

println(players.minOf { it.score } * diceRolls)


var p1Wins = 0L
var p2Wins = 0L
val rolls = List(9) { Pair(it / 3 + 1, it % 3 + 1) }
val completeRolls = mutableListOf<Pair<Int, Int>>()
for ((r1, r2) in rolls) {
    for ((s1, s2) in rolls) {
        for ((t1, t2) in rolls) {
            completeRolls.add(Pair(r1 + s1 + t1, r2 + s2 + t2))
        }
    }
}
fun rollDice(p1Field: Int, p2Field: Int, p1Score: Int, p2Score: Int) {
    if (p1Score >= 21) {
        p1Wins++
        return
    } else if (p2Score >= 21) {
        p2Wins++
        return
    }
    for ((r1, r2) in completeRolls) {
        val nextP1Field = (p1Field + r1) % 10
        val nextP1Score = p1Score + nextP1Field + 1
        val nextP2Field = (p2Field + r2) % 10
        val nextP2Score = p2Score + nextP2Field + 1
        rollDice(nextP1Field, nextP2Field, nextP1Score, nextP2Score)
    }
}


val rollList = mutableListOf<Int>()
var p1Score = 0
var p2Score = 0
var p1Field = players2[0].field
var p2Field = players2[1].field

do {
    do {

    } while (p1Score < 21 && p2Score < 21)
    if (p1Score >= 21) {
        p1Wins++
    } else {
        p2Wins++
    }
} while (rollList.firstOrNull { it < completeRolls.size - 1 } != null)
rollDice(players2[0].field, players2[1].field, 0, 0)

println(max(p1Wins, p2Wins))
