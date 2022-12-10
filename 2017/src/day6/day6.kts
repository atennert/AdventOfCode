package day6

import java.nio.file.Files
import java.nio.file.Path

fun runCycles() {
    states.clear()
    while (!states.contains(memoryBanks)) {
        states.add(memoryBanks.toList())
        val maxBlocks = memoryBanks.maxOf { it }
        val maxIndex = memoryBanks.indexOf(maxBlocks)
        memoryBanks[maxIndex] = 0
        for (i in 1..maxBlocks) {
            val bankIndex = (maxIndex + i) % memoryBanks.size
            memoryBanks[bankIndex] = memoryBanks[bankIndex] + 1
        }
    }
}

val memoryBanks = Files.readString(Path.of("input.txt"))
    .split('\t')
    .map { it.toInt() }
    .toMutableList()
val states = mutableSetOf<List<Int>>()

runCycles()
println(states.size)

runCycles()
println(states.size)
