package day11

import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign
import kotlin.test.assertEquals

fun Triple<Int, Int, Int>.simplify(): Triple<Int, Int, Int> {
    return when {
        second.sign != first.sign && first.sign != 0 && second.sign != 0 -> {
            if (abs(first) < abs(second)) {
                Triple(0, second + first, third - first)
            } else {
                Triple(first + second, 0, third + second)
            }
        }
        second.sign != third.sign && second.sign != 0 && third.sign != 0 -> {
            if (abs(second) < abs(third)) {
                Triple(first + second, 0, third + second)
            } else {
                Triple(first - third, second + third, 0)
            }
        }
        first.sign == third.sign && first.sign != 0 -> {
            if (abs(first) < abs(third)) {
                Triple(0, second + first, third - first)
            } else {
                Triple(first - third, second + third, 0)
            }
        }
        else -> {
            Triple(1, 2, 3)
        }
    }
}

fun Triple<Int, Int, Int>.distance() = simplify().run { abs(first) + abs(second) + abs(third) }

assertEquals(Triple(0, 11, 0), Triple(1, 10, 1).simplify(), "1 from (1,10,1)")
assertEquals(Triple(1, 11, 0), Triple(2, 10, 1).simplify(), "2 from (2,10,1)")
assertEquals(Triple(0, 11, 1), Triple(1, 10, 2).simplify(), "3 from (1,10,2)")

assertEquals(Triple(0, -11, 0), Triple(-1, -10, -1).simplify(), "4 from (-1,-10,-1)")
assertEquals(Triple(-1, -11, 0), Triple(-2, -10, -1).simplify(), "5 from (-2,-10,-1)")
assertEquals(Triple(0, -11, -1), Triple(-1, -10, -2).simplify(), "6 from (-1,-10,-2)")

assertEquals(Triple(0, 0, -11), Triple(1, -1, -10).simplify(), "7 from (1,-1,10)")
assertEquals(Triple(1, 0, -11), Triple(2, -1, -10).simplify(), "8 from (2,-1,10)")
assertEquals(Triple(0, -1, -11), Triple(1, -2, -10).simplify(), "9 from (1,-2,10)")

assertEquals(Triple(0, 0, 11), Triple(-1, 1, 10).simplify(), "10 from (-1,1,10)")
assertEquals(Triple(-1, 0, 11), Triple(-2, 1, 10).simplify(), "11 from (-2,1,10)")
assertEquals(Triple(0, 1, 11), Triple(-1, 2, 10).simplify(), "12 from (-1,2,10)")

assertEquals(Triple(11, 0, 0), Triple(10, 1, -1).simplify(), "13 from (10,1,-1)")
assertEquals(Triple(11, 1, 0), Triple(10, 2, -1).simplify(), "14 from (10,2,-1)")
assertEquals(Triple(11, 0, -1), Triple(10, 1, -2).simplify(), "15 from (10,1,-2)")

assertEquals(Triple(-11, 0, 0), Triple(-10, -1, 1).simplify(), "16 from (10,-1,1)")
assertEquals(Triple(-11, -1, 0), Triple(-10, -2, 1).simplify(), "17 from (10,-2,1)")
assertEquals(Triple(-11, 0, 1), Triple(-10, -1, 2).simplify(), "18 from (10,-1,2)")

var position = Triple(0, 0, 0)
var maxDistance = 0

Files.readString(Path.of("input.txt"))
    .split(',')
    .asSequence()
    .forEach {
        when (it) {
            "nw" -> position = Triple(position.first + 1, position.second, position.third)
            "n"  -> position = Triple(position.first, position.second + 1, position.third)
            "ne" -> position = Triple(position.first, position.second, position.third + 1)
            "se" -> position = Triple(position.first - 1, position.second, position.third)
            "s"  -> position = Triple(position.first, position.second - 1, position.third)
            "sw" -> position = Triple(position.first, position.second, position.third - 1)
            else -> {}
        }
        maxDistance = max(maxDistance, position.distance())
    }

println(position.distance())
println(maxDistance)