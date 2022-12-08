import java.nio.file.Files
import java.nio.file.Path

val input = Files.readString(Path.of("input.txt"))
    .split("")
    .filterNot { it.isEmpty() }
    .map { it.toInt() }

var sum1 = 0
var sum2 = 0
val halfInputSize = input.size / 2
for (i in input.indices) {
    if (input[i] == input[(i + 1) % input.size]) {
        sum1 += input[i]
    }
    if (input[i] == input[(i + halfInputSize) % input.size]) {
        sum2 += input[i]
    }
}

println(sum1)
println(sum2)