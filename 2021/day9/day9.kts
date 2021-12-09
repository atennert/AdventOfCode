#!/usr/bin/env kscript

var heightMap = mutableListOf<List<Int>>()
while (true) {
    val line = readlnOrNull() ?: break
    heightMap.add(line.map { it.digitToInt() })
}

fun isLowPoint(x: Int, y: Int): Boolean {
    val height = heightMap[y][x]
    val northPoint = heightMap.getOrNull(y - 1)?.getOrNull(x)
    val eastPoint = heightMap.getOrNull(y)?.getOrNull(x + 1)
    val southPoint = heightMap.getOrNull(y + 1)?.getOrNull(x)
    val westPoint = heightMap.getOrNull(y)?.getOrNull(x - 1)
    return (northPoint == null || height < northPoint)
            && (eastPoint == null || height < eastPoint)
            && (southPoint == null || height < southPoint)
            && (westPoint == null || height < westPoint)
}

class Point(val x: Int, val y: Int, val height: Int) {
    override fun equals(other: Any?) = other is Point && other.x == x && other.y == y

    override fun hashCode() = 31 * x + y
}

val lowPoints = mutableListOf<Point>()
for (y in 0 until heightMap.size) {
    for (x in 0 until heightMap[y].size) {
        if (isLowPoint(x, y)) {
            lowPoints.add(Point(x, y, heightMap[y][x]))
        }
    }
}

println("low point sum: ${lowPoints.sumOf { it.height + 1 }}")

fun getPointOrNull(x: Int, y: Int): Point? {
    return heightMap.getOrNull(y)?.getOrNull(x)?.let { Point(x, y, it) }
}

fun computeBasin(point: Point, checkedPoints: MutableSet<Point>): Set<Point> {
    checkedPoints.add(point)
    val northPoint = getPointOrNull(point.x, point.y - 1)
    val eastPoint = getPointOrNull(point.x + 1, point.y)
    val southPoint = getPointOrNull(point.x, point.y + 1)
    val westPoint = getPointOrNull(point.x - 1, point.y)

    listOf(northPoint, eastPoint, southPoint, westPoint)
        .filter { it != null && it.height < 9 && !checkedPoints.contains(it) }
        .forEach { computeBasin(it!!, checkedPoints) }

    return checkedPoints
}

val biggestBasins = lowPoints
    .map { computeBasin(it, mutableSetOf()).size }
    .sorted()
    .takeLast(3)

println("combined basin size: ${biggestBasins.reduce { a, b -> a * b }}")
