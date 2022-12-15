#!/usr/bin/env kscript

import kotlin.math.abs

class Sensor(val xs: Int, val ys: Int, val xb: Int, val yb: Int) {
    val distance = abs(xs - xb) + abs(ys - yb)
}

val sensors = mutableListOf<Sensor>()

while (true) {
    val line = readlnOrNull() ?: break
    val (xs, ys, xb, yb) = line
        .replace(Regex(".*x=(-?\\d+), y=(-?\\d+).*x=(-?\\d+), y=(-?\\d+)"), "$1,$2,$3,$4")
        .split(',')
        .map { it.toInt() }
    sensors.add(Sensor(xs, ys, xb, yb))
}

fun covered(y: Int): List<Pair<Int, Int>> {
    val l = mutableListOf<Pair<Int, Int>>()

    sensors.filter { s -> s.ys - s.distance <= y && y <= s.ys + s.distance }
        .map { s ->
            val dy = s.distance - abs(s.ys - y)
            Pair(s.xs - dy, s.xs + dy)
        }
        .forEach { la ->
            val found = l.filter {
                la.first <= it.first && it.first <= la.second ||
                        la.first <= it.second && it.second <= la.second ||
                        it.first <= la.first && la.first <= it.second ||
                        it.first <= la.second && la.second <= it.second
            } + la
            l.removeAll(found)
            l.add(Pair(found.minOf { it.first }, found.maxOf { it.second }))
        }

    return l
}

fun findNoBeaconsInLine(y: Int): Int {
    val beaconsInLine = sensors
        .filter { s -> s.yb == y }
        .map { it.xb }
        .toSet()

    val covered = covered(y)
    val sum = covered.fold(0) { sum, (x1, x2) -> sum + (x2 - x1) }
    return sum - beaconsInLine.count() + covered.size
}

println(findNoBeaconsInLine(2000000))

fun findMissingSpot(y: Int): List<Pair<Int, Int>>? {
    val covered = covered(y)
    if (covered.size > 1) {
        return covered
    }
    return null
}

for (y in 0 .. 4000000) {
    val found = findMissingSpot(y)?.sortedBy { it.first }
    if (found != null) {
        println(y + (found[0].second + 1L) * 4000000)
        break
    }
}
