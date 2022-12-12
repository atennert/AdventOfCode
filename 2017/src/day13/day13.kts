package day13

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

val layers: List<Triple<Int, Int, Int>> = Files.lines(Path.of("input.txt"))
    .map { line ->
        val (layer, depth) = line.split(": ").map { it.toInt() }
        Triple(layer, depth, 2 + (depth - 2) * 2)
    }
    .collect(Collectors.toList())

fun check(t: Int): Pair<Boolean, Int> {
    return layers.fold(Pair(false, 0)) { (caught, severity), (layer, depth, cycleTime) ->
        val caughtInLayer = (t + layer) % cycleTime == 0
        Pair(caught || caughtInLayer, severity + (if (caughtInLayer) layer * depth else 0))
    }
}

println(check(0).second)

var delay = 0
while (check(delay).first) {
    delay++
}
println(delay)
