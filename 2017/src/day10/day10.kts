package day10

import java.nio.file.Files
import java.nio.file.Path

val inputString: String = Files.readString(Path.of("input.txt"))
val input1 = inputString.split(',').map { it.toInt() }

val result1 = run(input1, 1)
println(result1[0] * result1[1])

println(computeHash(inputString, 64))