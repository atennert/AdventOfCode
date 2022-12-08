import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.max
import kotlin.math.min

val numbers = Files.readAllLines(Path.of("input.txt"))
    .map { it.split("\t").map { n -> n.toInt() } }

println(numbers.sumOf { line ->
    val result = line.fold(Pair(Int.MAX_VALUE, Int.MIN_VALUE)) { acc, i -> Pair(min(i, acc.first), max(i, acc.second)) }
    result.second - result.first
})

println(numbers.sumOf { line ->
    for (i in line.indices) {
        for (j in line.indices) {
            if (j == i) {
                continue
            }
            if (line[i] % line[j] == 0) {
                return@sumOf line[i] / line[j]
            }
        }
    }
    0
})
