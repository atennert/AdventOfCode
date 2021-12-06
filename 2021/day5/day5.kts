#!/usr/bin/env kscript

import kotlin.math.abs
import kotlin.math.max

class Line(input: String) {
    private val x1: Int
    private val y1: Int
    private val x2: Int
    private val y2: Int

    init {
        val (a, b, c, d) = input
            .split(Regex("( -> )|,"))
            .map(String::toInt)
        x1 = a
        y1 = b
        x2 = c
        y2 = d
    }

    fun isVerticalOrHorizontal() = x1 == x2 || y1 == y2

    fun getPoints(): List<Pair<Int, Int>> {
        val distance = max(abs(x1 - x2), abs(y1 - y2)) + 1
        return getCoordinates(x1, x2, distance)
            .zip(getCoordinates(y1, y2, distance))
    }

    private fun getCoordinates(a: Int, b: Int, distance: Int): List<Int> {
        return when {
            a == b -> List(distance) { a }
            a < b -> (a .. b).toList()
            else -> (a downTo b).toList()
        }
    }
}

fun String.toLine() = Line(this)

val allPoints = mutableMapOf<Pair<Int, Int>, Int>()
val allPoints2 = mutableMapOf<Pair<Int, Int>, Int>()
while (true) {
    val line = readlnOrNull()?.toLine() ?: break

    if (line.isVerticalOrHorizontal()) {
        val points = line.getPoints()
        for (point in points) {
            if (allPoints.containsKey(point)) {
                allPoints[point] = allPoints[point]!! + 1
            } else {
                allPoints[point] = 1
            }
        }
    }

    val points = line.getPoints()
    for (point in points) {
        if (allPoints2.containsKey(point)) {
            allPoints2[point] = allPoints2[point]!! + 1
        } else {
            allPoints2[point] = 1
        }
    }
}

var overlappingCount = 0
for ((_, value) in allPoints) {
    if (value > 1) {
        overlappingCount += 1
    }
}

var overlappingCount2 = 0
for ((_, value) in allPoints2) {
    if (value > 1) {
        overlappingCount2 += 1
    }
}

println("overlapping: $overlappingCount")
println("overlapping: $overlappingCount2")
