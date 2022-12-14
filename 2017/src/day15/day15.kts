package day15

import java.nio.file.Files
import java.nio.file.Path

val lines = Files.readAllLines(Path.of("input.txt"))
    .map { it.split(' ').last().toLong() }
var a = lines[0]
var b = lines[1]

fun generate(x: Long, factor: Long): Long = (x * factor) % 2147483647

var matches = 0
for (i in 1..40_000_000) {
    a = generate(a, 16807)
    b = generate(b, 48271)

    if (a and 0xffff == b and 0xffff) {
        matches++
    }
}
println(matches)


a = lines[0]
b = lines[1]
matches = 0
for (i in 1..5_000_000) {
    do {
        a = generate(a, 16807)
    } while (a % 4 != 0L)
    do {
        b = generate(b, 48271)
    } while (b % 8 != 0L)

    if (a and 0xffff == b and 0xffff) {
        matches++
    }
}
println(matches)
