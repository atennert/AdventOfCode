package day17

import java.nio.file.Files
import java.nio.file.Path

val moves = Files.readString(Path.of("input.txt")).toInt()
val memory = mutableListOf(0)

var currentIndex = 0
for (i in 1 .. 2017) {
    currentIndex = ((currentIndex + moves) % i) + 1
    memory.add(currentIndex, i)
}
println(memory[memory.indexOf(2017) + 1])

currentIndex = 0
var afterZero = 0
for (i in 1 .. 50000000) {
    currentIndex = ((currentIndex + moves) % i) + 1
    if (currentIndex == 1) {
        afterZero = i
    }
}
println(afterZero)