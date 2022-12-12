#!/usr/bin/env kscript

val map = mutableListOf<List<Int>>()
lateinit var start: Pair<Int, Int>
lateinit var goal: Pair<Int, Int>

fun String.findStartAndGoal(): String {
    val s1 = findAnyOf(setOf("S"))?.let { (index, _) ->
        start = Pair(index, map.size)
        replace('S', 'a')
    } ?: this
    val s2 = s1.findAnyOf(setOf("E"))?.let { (index, _) ->
        goal = Pair(index, map.size)
        s1.replace('E', 'z')
    } ?: s1
    return s2
}

fun getActions(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    return listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        .filter { (x, _) -> pos.first + x >= 0 && pos.first + x < map.first().size }
        .filter { (_, y) -> pos.second + y >= 0 && pos.second + y < map.size }
}

fun getActions1(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    return getActions(pos)
        .filter { (x, y) -> map[pos.second][pos.first] + 1 >= map[pos.second + y][pos.first + x] }
}

fun getActions2(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
    return getActions(pos)
        .filter { (x, y) -> map[pos.second][pos.first] - 1 <= map[pos.second + y][pos.first + x] }
}

fun h1(nextState: Pair<Int, Int>, goal: Pair<Int, Int>): Int {
    return map[goal.second][goal.first] - map[nextState.second][nextState.first]
}

fun h2(nextState: Pair<Int, Int>): Int {
    return map[nextState.second][nextState.first]
}

fun aStar(
    start: Pair<Int, Int>,
    goal: Pair<Int, Int>,
    getActions: (Pair<Int, Int>) -> List<Pair<Int, Int>>,
    h: (Pair<Int, Int>, Pair<Int, Int>) -> Int,
    check: (Pair<Int, Int>) -> Boolean
): Int? {
    val queue = mutableListOf(Pair(0, start))
    val visited = mutableSetOf(start)
    val branch = mutableMapOf<Pair<Int, Int>, Int>()
    lateinit var targetState: Pair<Int, Int>

    while (queue.size > 0) {
        val currentState = queue.removeFirst().second
        val currentCost = branch[currentState] ?: 0

        if (check(currentState)) {
            targetState = currentState
            break
        }

        for (action in getActions(currentState)) {
            val nextState = Pair(currentState.first + action.first, currentState.second + action.second)
            val branchCost = currentCost + 1
            val queueCost = branchCost + h(nextState, goal)

            if (!visited.contains(nextState) || (branch[nextState] ?: -1) > branchCost) {
                visited.add(nextState)
                branch[nextState] = branchCost
                queue.removeIf { (_, state) -> state == nextState }
                queue.add(Pair(queueCost, nextState))
                queue.sortBy { it.first }
            }
        }
    }
    return branch[targetState]
}

while (true) {
    val line = readlnOrNull()?.findStartAndGoal() ?: break
    map.add(line.toCharArray().map { it.code - 'a'.code })
}

println(aStar(start, goal, { getActions1(it) }, { a, b -> h1(a, b) }, { it == goal }))
println(aStar(goal, Pair(0,0), { getActions2(it) }, { a, _ -> h2(a) }, { h2(it) == 0 }))