package day19

import java.nio.file.Files
import java.nio.file.Path

enum class Direction(private val offset: Pair<Int, Int>) {
    UP(Pair(0, -1)),
    DOWN(Pair(0, 1)),
    LEFT(Pair(-1, 0)),
    RIGHT(Pair(1, 0));

    fun next(pos: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(pos.first + offset.first, pos.second + offset.second)
    }

    fun reverse() = when (this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
    }
}

fun changeDirection(pos: Pair<Int, Int>, dir: Direction): Direction {
    return Direction.values()
        .filterNot { it == dir.reverse() }
        .first { d ->
            val (x, y) = d.next(pos)
            y < map.size && x < map[y].size && map[y][x] != ' '
        }
}

val map = Files.readAllLines(Path.of("input.txt"))
    .map { it.toCharArray() }

var pos = Pair(map[0].indexOf('|'), 0)
var direction = Direction.DOWN
var word = ""
var steps = 0

while (true) {
    steps++
    pos = direction.next(pos)
    when (val char = map[pos.second][pos.first]) {
        ' ' -> break
        '+' -> direction = changeDirection(pos, direction)
        else -> {
            if (char.isLetter()) {
                word += char
            }
        }
    }
}

println(word)
println(steps)
