#!/usr/bin/env kscript

import kotlin.math.max
import kotlin.math.min

val blocked = mutableSetOf<Pair<Int, Int>>()

while (true) {
    val path = readlnOrNull()
        ?.split(" -> ")
        ?.map { it.split(',').let { (x, y) -> Pair(x.toInt(), y.toInt()) } } ?: break

    for (i in 0 until path.size - 1) {
        val (x1, y1) = path[i]
        val (x2, y2) = path[i + 1]
        for (x in min(x1, x2) .. max(x1, x2)) {
            for (y in min(y1, y2) .. max(y1, y2)) {
                blocked.add(Pair(x, y))
            }
        }
    }
}

fun addSand(fall: (Int, Int) -> Pair<Int, Int>?, isFloor: (Int) -> Boolean = { false }): Pair<Int, Int>? {
    var x = 500
    var y = 0
    if (blocked.contains(Pair(500, 0))) {
        return null
    }

    while (true) {
        val nextStop = fall(x, y)
        if (nextStop == null) {
            return null
        } else {
            y = nextStop.second - 1
        }

        if (!blocked.contains(Pair(x - 1, y + 1)) && !isFloor(y + 1)) {
            x -= 1
            y += 1
            continue
        }
        if (!blocked.contains(Pair(x + 1, y + 1)) && !isFloor(y + 1)) {
            x += 1
            y += 1
            continue
        }
        break
    }
    return Pair(x, y)
}

fun fall1(targetX: Int, targetY: Int): Pair<Int, Int>? {
    return blocked.filter { (x, y) -> x == targetX && y > targetY }.minByOrNull { it.second }
}

var sand = 0
while (true) {
    addSand({ x, y -> fall1(x, y) })?.also { blocked.add(it) } ?: break
    sand++
}
println(sand)


fun fall2(targetX: Int, targetY: Int): Pair<Int, Int> {
    return fall1(targetX, targetY) ?: Pair(targetX, floorY)
}

var floorY = blocked.maxOf { it.second } + 2
while (true) {
    addSand({ x, y -> fall2(x, y) }, { it == floorY })?.also { blocked.add(it) } ?: break
    sand++
}
println(sand)
