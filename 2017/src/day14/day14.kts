package day14

import day10.computeHash
import java.nio.file.Files
import java.nio.file.Path

val input: String = Files.readString(Path.of("input.txt"))
var squares = 0
val memory = mutableListOf<CharArray>()

for (i in 0 .. 127) {
    memory.add(computeHash("$input-$i", 64)
        .toCharArray()
        .joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }
        .toCharArray())

    squares += memory.last().count { it == '1' }
}

println(squares)

fun findAdjacent(start: Pair<Int, Int>): List<Pair<Int, Int>> {
    val result = mutableListOf(start)
    var i = 0
    while (i < result.size) {
        val (x, y) = result[i]
        for ((px, py) in listOf(Pair(x + 1, y), Pair(x, y + 1), Pair(x - 1, y), Pair(x, y - 1))) {
            if (px >= 0 && py >= 0 && px < memory[y].size && py < memory.size && memory[py][px] == '1' && !result.contains(Pair(px, py))) {
                result.add(Pair(px, py))
            }
        }
        i++
    }

    return result
}

val allSeenSquares = mutableSetOf<Pair<Int, Int>>()
var groups = 0

for (y in memory.indices) {
    for (x in memory[y].indices) {
        if (allSeenSquares.contains(Pair(x, y)) || memory[y][x] == '0') {
            continue
        }
        groups++
        allSeenSquares.addAll(findAdjacent(Pair(x, y)))
    }
}

println(groups)
