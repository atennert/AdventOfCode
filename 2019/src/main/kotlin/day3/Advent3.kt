package day3

import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.InvalidParameterException
import kotlin.math.abs
import kotlin.streams.toList

const val DIR_RIGHT = "R"
const val DIR_LEFT = "L"
const val DIR_UP = "U"
const val DIR_DOWN = "D"

typealias Point = Triple<Int, Int, Int>

class Advent3(commands: List<List<String>>) {
    private val courses = commands.map(this::plotCourse)

    private val intersections = calculateIntersections(courses)

    private val closestIntersection1 = calculateClosestIntersection1(intersections)

    private val closestIntersection2 = calculateClosestIntersection2(intersections)

    private fun plotCourse(commands: List<String>): List<Point> {
        var plotPoint = Triple(0, 0, 0)
        return commands.fold(mutableListOf()) { plotList, command ->
            val commandPlot = interpretCommand(plotPoint, command)
            plotList.addAll(commandPlot)
            plotPoint = commandPlot.last()
            plotList
        }
    }

    private fun interpretCommand(startPoint: Point, command: String): List<Point> {
        val (direction, distanceString) = command.partition { it.isLetter() }
        val distance = Integer.valueOf(distanceString)
        val plotChange = getPointChange(direction)
        var currentPoint = startPoint

        val commandCoursePlot = mutableListOf<Point>()
        repeat(distance) {
            currentPoint = addPoints(currentPoint, plotChange)
            commandCoursePlot.add(currentPoint)
        }
        return commandCoursePlot
    }

    private fun addPoints(a: Point, b: Point): Point {
        return Triple(
            a.first + b.first,
            a.second + b.second,
            a.third + b.third
        )
    }

    private fun getPointChange(direction: String): Point {
        return when (direction) {
            DIR_UP -> Triple(0, 1, 1)
            DIR_DOWN -> Triple(0, -1, 1)
            DIR_LEFT -> Triple(-1, 0, 1)
            DIR_RIGHT -> Triple(1, 0, 1)
            else -> throw InvalidParameterException("unknown direction $direction")
        }
    }

    private fun calculateIntersections(courses: List<List<Point>>): List<Point> {
        return courses.reduce { c1, c2 ->
            val intersectList = mutableListOf<Point>()
            outer@
            for (p1 in c1) {
                for (p2 in c2) {
                    if (p1.first == p2.first && p1.second == p2.second) {
                        intersectList.add(Point(p1.first, p1.second, p1.third + p2.third))
                        continue@outer
                    }
                }
            }
            intersectList
        }
    }

    private fun calculateClosestIntersection1(intersections: List<Point>): Point {
        return intersections.minWith(Comparator { p1, p2 -> manhattanDistance(p1) - manhattanDistance(p2) })!!
    }

    private fun calculateClosestIntersection2(intersections: List<Point>): Point {
        return intersections.minWith(Comparator { p1, p2 -> p1.third - p2.third })!!
    }

    fun getDistanceToClosestIntersection(): Int {
        return manhattanDistance(closestIntersection1)
    }

    fun getTotalStepsToClosestIntersection(): Int {
        return closestIntersection2.third
    }

    private fun manhattanDistance(position: Point): Int {
        return abs(position.first) + abs(position.second)
    }
}

fun loadData(inputFile: String): List<List<String>> {
    val inputStream = ClassLoader.getSystemResource(inputFile).openStream()
    val bufferedInput = BufferedReader(InputStreamReader(inputStream))
    val commands = bufferedInput.lines()
        .map(String::trim)
        .map { it.split(',') }
        .toList()
    bufferedInput.close()
    return commands
}

fun main() {
    val advent = Advent3(loadData("3/input.txt"))
    println("distance: ${advent.getDistanceToClosestIntersection()}")
    println("distance: ${advent.getTotalStepsToClosestIntersection()}")
}
