#!/usr/bin/env kscript

import kotlin.math.abs

val (minX, maxX, minY, maxY) = readln()
    .replace(Regex(".+x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)"), "$1,$2,$3,$4")
    .split(',')
    .map(String::toInt)

fun computeYs(): List<Pair<Int, List<Int>>> {
    var y = minY
    val findings = mutableListOf<Pair<Int, List<Int>>>()
    while (true) {
        val count = simulateY(y, minY, maxY)
        if (count.isNotEmpty()) {
            findings.add(Pair(y, count))
        } else if (findings.isNotEmpty() && abs(y - findings.last().first) > 100) {
            break
        }
        y++
    }
    return findings
}

fun simulateY(a: Int, aMin: Int, aMax: Int): List<Int> {
    var next = 0
    var count = 0
    val results = mutableListOf<Int>()
    while (true) {
        if (next in aMin..aMax) {
            results.add(count)
        } else if (next < aMin) {
            return results
        }
        next += a - count
        count++
    }
}

fun simulateX(a: Int, aMin: Int, aMax: Int): List<Int> {
    var next = 0
    var count = 0
    val results = mutableListOf<Int>()
    while (true) {
        if (next in aMin..aMax) {
            if (a - count == 0) {
                for (c in count..(count + 500)) {
                    results.add(c)
                }
                return results
            }
            results.add(count)
        } else if (next > aMax) {
            return results
        }
        next += a - count
        count++
    }
}

fun maxHeight(y: Int): Int {
    var height = 0
    for (hDiff in 0..y) {
        height += hDiff
    }
    return height
}

var ys = computeYs()
println("max height: ${maxHeight(ys.last().first)}")

val lowestXSpeed = getLowestXSpeed(1, 0)
fun getLowestXSpeed(speed: Int, currentX: Int): Int {
    return if (currentX + speed >= minX)
        speed
    else
        getLowestXSpeed(speed + 1, currentX + speed)
}

var xs = mutableListOf<Pair<Int, List<Int>>>()
for (x in lowestXSpeed..maxX) {
    val count = simulateX(x, minX, maxX)
    if (count.isNotEmpty()) {
        xs.add(Pair(x, count))
    }
}

var results = mutableSetOf<Pair<Int, Int>>()
for ((x, cx) in xs) {
    for ((y, cy) in ys) {
        for (cxx in cx) {
            for (cyy in cy) {
                if (cxx == cyy) {
                    results.add(Pair(x, y))
                }
            }
        }
    }
}

println("initial values: ${results.size}")
