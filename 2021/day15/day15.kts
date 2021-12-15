#!/usr/bin/env kscript

import java.util.PriorityQueue

val grid = mutableListOf<IntArray>()
while (true) {
    val line = readlnOrNull()?.map { it.digitToInt() }?.toIntArray() ?: break
    grid.add(line)
}

fun nextActions(position: Pair<Int, Int>, factor: Int): List<Pair<Int, Int>> {
    val (x, y) = position
    return listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
        .filterNot { (xa, ya) ->
            xa + x < 0 || ya + y < 0 || xa + x >= grid.size * factor || ya + y >= grid.size * factor
        }
}

fun g(nextGoal: Pair<Int, Int>): Int {
    val costBonus = Math.floorDiv(nextGoal.first, grid.size) + Math.floorDiv(nextGoal.second, grid.size)
    return (grid[nextGoal.second % grid.size][nextGoal.first % grid.size] + costBonus - 1) % 9 + 1
}

fun h(currentPosition: Pair<Int, Int>, nextPosition: Pair<Int, Int>) = 0
//    abs(currentPosition.first - nextPosition.first) + abs(currentPosition.second - nextPosition.second)

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

class BranchItem(val branchCost: Int, val currentPosition: Pair<Int, Int>, val action: Pair<Int, Int>)

fun aStar(start: Pair<Int, Int>, goal: Pair<Int, Int>, factor: Int): Pair<List<Pair<Int, Int>>, Int> {
    val queue = PriorityQueue<Pair<Int, Pair<Int, Int>>> { e1, e2 -> e1.first.compareTo(e2.first) }
    queue.add(Pair(0, start))
    val visited = mutableSetOf(start)
    val branch = mutableMapOf<Pair<Int, Int>, BranchItem>()
    var found = false

    while (!queue.isEmpty()) {
        val item = queue.poll()
        val currentPosition = item.second
        val currentCost = if (currentPosition == start) 0 else branch[currentPosition]?.branchCost ?: 0

        if (currentPosition == goal) {
            found = true
            break
        }
        for (nextAction in nextActions(currentPosition, factor)) {
            val nextPosition = currentPosition + nextAction
            val branchCost = currentCost + g(nextPosition)
            val queueCost = branchCost + h(currentPosition, nextPosition)

            if (!visited.contains(nextPosition)) {
                visited.add(nextPosition)
                branch[nextPosition] = BranchItem(branchCost, currentPosition, nextAction)
                queue.add(Pair(queueCost, nextPosition))
            }
        }
    }

    val path = mutableListOf<Pair<Int, Int>>()
    var pathCost = 0
    if (found) {
        var n = goal
        pathCost = branch[n]!!.branchCost
        while (branch[n]!!.currentPosition !== start) {
            path.add(branch[n]!!.action)
            n = branch[n]!!.currentPosition
        }
    } else {
        println("no path found")
    }
    return Pair(path, pathCost)
}

println("lowest risk: ${aStar(Pair(0, 0), Pair(grid.size - 1, grid.size - 1), 1).second}")
println("lowest risk large: ${aStar(Pair(0, 0), Pair(grid.size * 5 - 1, grid.size * 5 - 1), 5).second}")
