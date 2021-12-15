#!/usr/bin/env kscript

val grid = mutableListOf<IntArray>()
while (true) {
    val line = readlnOrNull()?.map { it.digitToInt() }?.toIntArray() ?: break
    grid.add(line)
}

fun getActions(position: Pair<Int, Int>, factor: Int): List<Pair<Int, Int>> {
    val (x, y) = position
    return listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
        .filterNot { (xa, ya) ->
            xa + x < 0 || ya + y < 0 || xa + x >= grid.size * factor || ya + y >= grid.size * factor
        }
}

fun getActionCost(nextPosition: Pair<Int, Int>, nextAction: Pair<Int, Int>): Int {
    val tileBonus = nextPosition.first / grid.size + nextPosition.second / grid.size
    return (grid[nextPosition.second % grid.size][nextPosition.first % grid.size] + tileBonus - 1) % 9 + 1
}

fun h(nextPosition: Pair<Int, Int>, goal: Pair<Int, Int>) = 0
//    abs(nextPosition.first - goal.first) + abs(nextPosition.second - goal.second)

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(this.first + other.first, this.second + other.second)

class BranchItem(val branchCost: Int, val currentPosition: Pair<Int, Int>, val action: Pair<Int, Int>)

fun aStar(
    start: Pair<Int, Int>,
    goal: Pair<Int, Int>,
    h: (Pair<Int, Int>, Pair<Int, Int>) -> Int,
    getActionCost: (Pair<Int, Int>, Pair<Int, Int>) -> Int,
    getActions: (Pair<Int, Int>) -> List<Pair<Int, Int>>
): Pair<List<Pair<Int, Int>>, Int> {
    val queue = mutableListOf<Pair<Int, Pair<Int, Int>>>()
    queue.add(Pair(0, start))
    val visited = mutableSetOf(start)
    val branch = mutableMapOf<Pair<Int, Int>, BranchItem>()
    var found = false

    while (queue.isNotEmpty()) {
        val currentPosition = queue.removeFirst().second
        val currentCost = branch[currentPosition]?.branchCost ?: 0 // g

        if (currentPosition == goal) {
            found = true
            break
        }
        for (nextAction in getActions(currentPosition)) {
            val nextPosition = currentPosition + nextAction
            val branchCost = currentCost + getActionCost(nextPosition, nextAction)
            val queueCost = branchCost + h(nextPosition, goal)

            if (!visited.contains(nextPosition)) {
                visited.add(nextPosition)
                branch[nextPosition] = BranchItem(branchCost, currentPosition, nextAction)
                queue.add(Pair(queueCost, nextPosition))
                queue.sortBy { it.first }
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

val start = Pair(0, 0)
val goal1 = Pair(grid.size - 1, grid.size - 1)
val goal2 = Pair(grid.size * 5 - 1, grid.size * 5 - 1)

println("lowest risk: ${aStar(start, goal1, ::h, ::getActionCost) { getActions(it, 1) }.second}")
println("lowest risk large: ${aStar(start, goal2, ::h, ::getActionCost) { getActions(it, 5)}.second}")
