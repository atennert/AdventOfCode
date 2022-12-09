#!/usr/bin/env kscript

fun simulateHead(knots: MutableList<Pair<Int, Int>>, direction: String) {
    knots[0] = when (direction) {
        "U" -> Pair(knots[0].first, knots[0].second + 1)
        "L" -> Pair(knots[0].first - 1, knots[0].second)
        "R" -> Pair(knots[0].first + 1, knots[0].second)
        "D" -> Pair(knots[0].first, knots[0].second - 1)
        else -> throw Error("Unknown direction $direction")
    }
}

fun simulateTail(knots: MutableList<Pair<Int, Int>>, k: Int) {
    knots[k] = when (knots[k]) {
        Pair(knots[k-1].first - 1, knots[k-1].second - 2),
        Pair(knots[k-1].first,     knots[k-1].second - 2),
        Pair(knots[k-1].first + 1, knots[k-1].second - 2) -> Pair(knots[k-1].first, knots[k-1].second - 1)

        Pair(knots[k-1].first + 2, knots[k-1].second + 1),
        Pair(knots[k-1].first + 2, knots[k-1].second),
        Pair(knots[k-1].first + 2, knots[k-1].second - 1) -> Pair(knots[k-1].first + 1, knots[k-1].second)

        Pair(knots[k-1].first - 2, knots[k-1].second + 1),
        Pair(knots[k-1].first - 2, knots[k-1].second),
        Pair(knots[k-1].first - 2, knots[k-1].second - 1) -> Pair(knots[k-1].first - 1, knots[k-1].second)

        Pair(knots[k-1].first - 1, knots[k-1].second + 2),
        Pair(knots[k-1].first,     knots[k-1].second + 2),
        Pair(knots[k-1].first + 1, knots[k-1].second + 2) -> Pair(knots[k-1].first, knots[k-1].second + 1)

        Pair(knots[k-1].first + 2, knots[k-1].second + 2) -> Pair(knots[k-1].first + 1, knots[k-1].second + 1)
        Pair(knots[k-1].first + 2, knots[k-1].second - 2) -> Pair(knots[k-1].first + 1, knots[k-1].second - 1)
        Pair(knots[k-1].first - 2, knots[k-1].second - 2) -> Pair(knots[k-1].first - 1, knots[k-1].second - 1)
        Pair(knots[k-1].first - 2, knots[k-1].second + 2) -> Pair(knots[k-1].first - 1, knots[k-1].second + 1)
        else -> knots[k]
    }
}

val tailPositions1 = mutableSetOf(Pair(0, 0))
val tailPositions2 = mutableSetOf(Pair(0, 0))
val twoKnots = MutableList(2) { Pair(0, 0) }
val tenKnots = MutableList(10) { Pair(0, 0) }

while (true) {
    val (direction, steps) = readlnOrNull()?.split(' ') ?: break

    for (i in 1 .. steps.toInt()) {
        simulateHead(twoKnots, direction)
        for (k in 1 until twoKnots.size) {
            simulateTail(twoKnots, k)
        }
        tailPositions1.add(twoKnots[1])

        simulateHead(tenKnots, direction)
        for (k in 1 until tenKnots.size) {
            simulateTail(tenKnots, k)
        }
        tailPositions2.add(tenKnots[9])
    }
}

println(tailPositions1.size)
println(tailPositions2.size)
