#!/usr/bin/env kscript

var heightMap = mutableListOf<List<Int>>()
while (true) {
    val line = readlnOrNull() ?: break
    heightMap.add(line.map { it.digitToInt() })
}

fun isLowPoint(heightMap: List<List<Int>>, x: Int, y: Int): Boolean {
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
        if (isLowPoint(heightMap, x, y)) {
            lowPoints.add(Point(x, y, heightMap[y][x]))
        }
    }
}

println("low point sum: ${lowPoints.sumOf { it.height + 1 }}")

fun getPointOrNull(heightMap: List<List<Int>>, x: Int, y: Int): Point? {
    return heightMap.getOrNull(y)?.getOrNull(x)?.let { Point(x, y, it) }
}

fun computeBasin(heightMap: List<List<Int>>, point: Point): Int {
    return computeBasin1(heightMap, point, mutableSetOf()).size
}

fun computeBasin1(
    heightMap: List<List<Int>>, point: Point, checkedPoints: MutableSet<Point>
): Set<Point> {
    checkedPoints.add(point)
    val x = point.x
    val y = point.y
    val northPoint = getPointOrNull(heightMap, x, y - 1)
    val eastPoint = getPointOrNull(heightMap, x + 1, y)
    val southPoint = getPointOrNull(heightMap, x, y + 1)
    val westPoint = getPointOrNull(heightMap, x - 1, y)
    for (p in listOf(northPoint, eastPoint, southPoint, westPoint)) {
        if (p != null && p.height < 9 && !checkedPoints.contains(p)) {
            computeBasin1(heightMap, p, checkedPoints)
        }
    }
    return checkedPoints
}

val biggestBasins = mutableListOf<Int>()
for (point in lowPoints) {
    biggestBasins.add(computeBasin(heightMap, point))
}
biggestBasins.sort()
biggestBasins.reverse()

println("combined basin size: ${biggestBasins[0] * biggestBasins[1] * biggestBasins[2]}")
