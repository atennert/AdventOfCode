package day3

import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.abs

val input = Files.readString(Path.of("input.txt")).toInt()

var increment = 0
var current = 1

while (current < input) {
    increment += 2
    current += increment * 4
}

val diff = current - input
val remainder = diff % increment
val dist = (increment / 2) + abs((increment / 2) - remainder)

println("Distance: $dist")


val memory = mutableMapOf(Pair(0, 0) to 1)

fun MutableMap<Pair<Int, Int>, Int>.read(x: Int, y: Int) = this[Pair(x, y)] ?: 0
fun MutableMap<Pair<Int, Int>, Int>.write(x: Int, y: Int, value: Int) {
    this[Pair(x, y)] = value
}

fun getNextValue(ps: List<Pair<Int, Int>>) = ps.sumOf { (x, y) -> memory.read(x, y) }

increment = 0
var lastValue = 1
var x = 1
var y = 0
run@ while (true) {
    increment += 2
    for (i in 0 until increment) {
        lastValue = getNextValue(listOf(Pair(x - 1, y - 1), Pair(x - 1, y), Pair(x - 1, y + 1), Pair(x, y + 1)))
        memory.write(x, y, lastValue)
        if (lastValue > input) break@run
        y--
    }
    y++
    x--
    for (i in 0 until increment) {
        lastValue = getNextValue(listOf(Pair(x - 1, y + 1), Pair(x, y + 1), Pair(x + 1, y + 1), Pair(x + 1, y)))
        memory.write(x, y, lastValue)
        if (lastValue > input) break@run
        x--
    }
    x++
    y++
    for (i in 0 until increment) {
        lastValue = getNextValue(listOf(Pair(x + 1, y + 1), Pair(x + 1, y), Pair(x + 1, y - 1), Pair(x, y - 1)))
        memory.write(x, y, lastValue)
        if (lastValue > input) break@run
        y++
    }
    y--
    x++
    for (i in 0 until increment) {
        lastValue = getNextValue(listOf(Pair(x + 1, y - 1), Pair(x, y - 1), Pair(x - 1, y - 1), Pair(x - 1, y)))
        memory.write(x, y, lastValue)
        if (lastValue > input) break@run
        x++
    }
}

println("higher value: $lastValue")
