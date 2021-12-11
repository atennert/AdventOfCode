#!/usr/bin/env kscript

import kotlin.math.max
import kotlin.math.min

val energyMap = mutableListOf<MutableList<Int>>()
while (true) {
    val line = readlnOrNull()?.toCharArray()
        ?.map(Char::digitToInt)
        ?.toMutableList() ?: break
    energyMap.add(line)
}

fun MutableList<Int>.incrementLine() = this.forEachIndexed { index, el -> this[index] = el + 1 }

fun MutableList<MutableList<Int>>.increment() = forEach { line -> line.incrementLine() }

fun runFlash(x: Int, y: Int) {
    energyMap[y][x] = 0
    for (b in max(0, y - 1)..min(y + 1, energyMap.size - 1)) {
        for (a in max(0, x - 1)..min(x + 1, energyMap[b].size - 1)) {
            if (energyMap[b][a] != 0) {
                energyMap[b][a] = energyMap[b][a] + 1
            }
        }
    }
}

fun runFlashes(): Int {
    var flashes = 0
    do {
        var hasNewFlashes = false
        for (y in 0 until energyMap.size) {
            for (x in 0 until energyMap[y].size) {
                if (energyMap[y][x] > 9) {
                    flashes++
                    hasNewFlashes = true
                    runFlash(x, y)
                }
            }
        }
    } while (hasNewFlashes)
    return flashes
}

var flashes = 0
for (i in 1..100) {
    energyMap.increment()
    flashes += runFlashes()
}

println("flashes: $flashes")

// assuming they didn't already flash simultaneously ...

var steps = 100
while (true) {
    steps++
    energyMap.increment()
    if (runFlashes() == 100) {
        break
    }
}

println("steps to simultaneous flashing: $steps")
