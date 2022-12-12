package day12

import java.nio.file.Files
import java.nio.file.Path

val directChannels = mutableMapOf<Int, List<Int>>()

Files.lines(Path.of("input.txt"))
    .forEach { line ->
        val (source, targets) = line.split(" <-> ")
        directChannels[source.toInt()] = targets.split(", ").map { it.toInt() }
    }

fun findAllConnected(root: Int): List<Int> {
    val connected = mutableListOf(root)
    var i = 0

    while (i < connected.size) {
        (directChannels[connected[i]] ?: emptyList())
            .filterNot { connected.contains(it) }
            .let { connected.addAll(it) }
        i++
    }
    return connected
}

println(findAllConnected(0).size)

val accounted = mutableSetOf<Int>()
var groups = 0

for (p in directChannels.keys) {
    if (!accounted.contains(p)){
        groups++
        accounted.addAll(findAllConnected(p))
    }
}

println(groups)
