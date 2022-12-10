package day10

import java.nio.file.Files
import java.nio.file.Path

val inputString: String = Files.readString(Path.of("input.txt"))
val input1 = inputString.split(',').map { it.toInt() }
val input2 = inputString.toCharArray().map { it.code } + listOf(17, 31, 73, 47, 23)

fun run(input: List<Int>, times: Int): List<Int> {
    var position = 0
    var skipSize = 0
    var numbers = List(256) { it }

    for (i in 1 .. times) {
        input.forEach {
            numbers = numbers.drop(position) + numbers.take(position)
            numbers = numbers.take(it).reversed() + numbers.drop(it)
            numbers = numbers.drop(numbers.size - position) + numbers.take(numbers.size - position)
            position = (position + it + skipSize) % numbers.size
            skipSize++
        }
    }
    return numbers
}

val result1 = run(input1, 1)
println(result1[0] * result1[1])

val sparseHash = run(input2, 64)
val denseHash = sparseHash.chunked(16)
    .map { it.reduce { acc, i -> acc xor i } }
    .joinToString("") { it.toString(16).padStart(2, '0') }
println(denseHash)