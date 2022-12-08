#!/usr/bin/env kscript

import kotlin.math.max

class Tree(val height: Int) {
    var up = -1
    var left = -1
    var down = -1
    var right = -1
    var dup = 0
    var dleft = 0
    var ddown = 0
    var dright = 0

    fun isVisible() = height > up || height > left || height > down || height > right

    fun scenicScore() = dup * dleft * ddown * dright
}

val map = mutableListOf<List<Tree>>()

while (true) {
    val line = readlnOrNull() ?: break
    map.add(line.split("").filter { it != "" }.map { Tree(it.toInt()) })
}

for (y in 0 until map.size) {
    for (x in 0 until map.first().size) {
        val tree = map[y][x]
        if (y > 0) {
            tree.up = max(map[y-1][x].height, map[y-1][x].up)
            var y1 = y-1
            do {
                tree.dup++
                y1--
            } while (y1 >= 0 && tree.height > map[y1+1][x].height)
        }
        if (x > 0) {
            tree.left = max(map[y][x-1].height, map[y][x-1].left)
            var x1 = x-1
            do {
                tree.dleft++
                x1--
            } while (x1 >= 0 && tree.height > map[y][x1+1].height)
        }
    }
}

for (y in (map.size - 1) downTo 0) {
    for (x in (map.first().size - 1) downTo 0) {
        val tree = map[y][x]
        if (y < map.size - 1) {
            tree.down = max(map[y+1][x].height, map[y+1][x].down)
            var y1 = y+1
            do {
                tree.ddown++
                y1++
            } while (y1 < map.size && tree.height > map[y1-1][x].height)
        }
        if (x < map.first().size - 1) {
            tree.right = max(map[y][x+1].height, map[y][x+1].right)
            var x1 = x+1
            do {
                tree.dright++
                x1++
            } while (x1 < map.first().size && tree.height > map[y][x1-1].height)
        }
    }
}

println(map.sumOf { line -> line.count { tree -> tree.isVisible() } })
println(map.maxOf { line -> line.maxOf { tree -> tree.scenicScore() } })
