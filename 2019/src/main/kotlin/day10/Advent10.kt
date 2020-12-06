package day10

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.*

fun getAsteroidPositions(map: String): List<Pair<Int, Int>> {
    return map.trim()
        .lines()
        .map(String::toCharArray)
        .foldIndexed(mutableListOf()) { y, acc, chars ->
            chars.forEachIndexed { x, c ->
                if (c == '#') {
                    acc.add(Pair(x, y))
                }
            }
            acc
        }
}

class VisibleAsteroid(val x: Int, val y: Int, val xDiff: Float, val yDiff: Float) {
    val rel = xDiff / yDiff
//      0 +
//   - +   + +
//- 0         + 0
//   - -   + -
//      0 -
    val thing = Triple(rel, xDiff.sign, yDiff.sign)
    val hyp = sqrt(xDiff.pow(2) + yDiff.pow(2))
    val s = asin(xDiff / hyp)
    val c = acos(yDiff / hyp)
    val angle = when {
        s >= 0 -> c
        c > 90 -> 180 + abs(s)
        else -> 360 - abs(s)
    }

    fun hasSameDirection(asteroid: VisibleAsteroid): Boolean {
        return xDiff.sign == asteroid.xDiff.sign &&
                yDiff.sign == asteroid.yDiff.sign
    }

    fun hasSameAngle(asteroid: VisibleAsteroid): Boolean {
        return thing == asteroid.thing
    }

    fun isCloser(asteroid: VisibleAsteroid): Boolean {
        return hyp <= asteroid.hyp
    }

    fun hasSameAngleAndDirection(asteroid: VisibleAsteroid): Boolean {
        return hasSameAngle(asteroid) && hasSameDirection(asteroid)
    }
}

fun getVisibleAsteroids(input: String): Pair<List<VisibleAsteroid>, List<VisibleAsteroid>> {
    val asteroids = getAsteroidPositions(input)
    return asteroids.fold(Pair(emptyList(), emptyList())) { visibleAsteroids, baseA ->
        val newAsteroids = asteroids.filterNot { it == baseA }
            .fold(Pair(mutableListOf<VisibleAsteroid>(), mutableListOf<VisibleAsteroid>())) { (l1, l2), a ->
                val xDiff = baseA.first - a.first
                val yDiff = baseA.second - a.second
                val newAsteroid = VisibleAsteroid(a.first, a.second, xDiff.toFloat(), yDiff.toFloat())

                var addAsteroid = true
                for (checkA in l1.toList()) {
                    if (newAsteroid.hasSameAngleAndDirection(checkA)) {
                        if (newAsteroid.isCloser(checkA)) {
                            l1.remove(checkA)
                        } else {
                            addAsteroid = false
                        }
                    }
                }
                if (addAsteroid) {
                    l1.add(newAsteroid)
                }
                l2.add(newAsteroid)
                Pair(l1, l2)
            }
        if (newAsteroids.first.size > visibleAsteroids.first.size) {
            newAsteroids
        } else {
            visibleAsteroids
        }
    }
}

fun main() {
    val inputStream = ClassLoader.getSystemResourceAsStream("10/input.txt")
    val reader = BufferedReader(InputStreamReader(inputStream))

    val map = reader.readText()

    val visibleAsteroids = getVisibleAsteroids(map)

    println("visible asteroids: ${visibleAsteroids.first.size}")

    val allAsteroids = visibleAsteroids.second
        .fold(mutableMapOf<Float, MutableList<VisibleAsteroid>>()) { buckets, a ->
            if (!buckets.containsKey(a.angle)) {
                buckets[a.angle] = mutableListOf()
            }
            buckets[a.angle]!!.add(a)
            buckets
        }
        .onEach { (_, bucket) -> bucket.sortBy { it.hyp } }

    val angles = allAsteroids.keys.toMutableList()
    angles.sortBy { it }
    var asteroid: VisibleAsteroid? = null
    var i = 0
    var max = 200
    while (i < max) {
        val angle = angles[i.rem(angles.size)]
        val bucket = allAsteroids[angle]!!
        i++
        if (bucket.isEmpty()) {
            max++
            continue
        }
        asteroid = bucket.removeAt(0)
    }

    if (asteroid != null) {
        println(asteroid.x * 100 + asteroid.y)
    }
}
