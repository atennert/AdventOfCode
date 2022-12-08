import java.nio.file.Files
import java.nio.file.Path

val offsets1 = Files.readAllLines(Path.of("input.txt"))
    .map { it.toInt() }
    .toMutableList()
val offsets2 = offsets1.toMutableList()

var i = 0
var jumps = 0
while (i < offsets1.size) {
    offsets1[i] = offsets1[i] + 1
    i += offsets1[i] - 1
    jumps++
}

println(jumps)

i = 0
jumps = 0
while (i < offsets2.size) {
    val offset = offsets2[i]
    offsets2[i] = if (offset > 2) offset - 1 else offset + 1
    i += offset
    jumps++
}

println(jumps)
