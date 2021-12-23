#!/usr/bin/env kscript

import kotlin.math.abs

val layout = mapOf(
    'A' to setOf(Pair(2, 5), Pair(5, 5), Pair(4, 7), Pair(3, 9)),
    'B' to setOf(Pair(4, 5), Pair(3, 7), Pair(5, 7), Pair(5, 9)),
    'C' to setOf(Pair(5, 3), Pair(3, 5), Pair(2, 9), Pair(4, 9)),
    'D' to setOf(Pair(2, 3), Pair(3, 3), Pair(4, 3), Pair(2, 7)),
    '.' to setOf(Pair(1, 1), Pair(1, 2), Pair(1, 4), Pair(1, 6), Pair(1, 8), Pair(1, 10), Pair(1, 11))
)

val hallWayPositions = setOf(Pair(1, 1), Pair(1, 2), Pair(1, 4), Pair(1, 6), Pair(1, 8), Pair(1, 10), Pair(1, 11))
val roomPositions = mapOf(
    'A' to setOf(Pair(2, 3), Pair(3, 3), Pair(4, 3), Pair(5, 3)),
    'B' to setOf(Pair(2, 5), Pair(3, 5), Pair(4, 5), Pair(5, 5)),
    'C' to setOf(Pair(2, 7), Pair(3, 7), Pair(4, 7), Pair(5, 7)),
    'D' to setOf(Pair(2, 9), Pair(3, 9), Pair(4, 9), Pair(5, 9))
)
val roomNumbers = mapOf('A' to 3, 'B' to 5, 'C' to 7, 'D' to 9)

data class Move(val item: Char, val currentPos: Pair<Int, Int>, val nextPos: Pair<Int, Int>)

fun targetRoomAvailable(item: Char, layout: Map<Char, Set<Pair<Int, Int>>>): Boolean {
    for (value in layout.filterKeys { it != '.' && it != item }.values) {
        for (v in value) {
            if (roomPositions[item]!!.contains(v)) {
                return false
            }
        }
    }
    return true
}
assert(targetRoomAvailable('A', mapOf()))
assert(targetRoomAvailable('A', mapOf('A' to setOf(Pair(2,3)))))
assert(targetRoomAvailable('A', mapOf('.' to setOf(Pair(2,3)))))
assert(!targetRoomAvailable('A', mapOf('B' to setOf(Pair(2,3)))))

fun isHome(item: Char, pos: Pair<Int, Int>, layout: Map<Char, Set<Pair<Int, Int>>>): Boolean {
    return roomNumbers[item] == pos.second &&
            layout.filterKeys { it != item && it != '.' }
                .values.firstOrNull {
                    it.contains(Pair(2, pos.second)) || it.contains(Pair(3, pos.second)) ||
                            it.contains(Pair(4, pos.second)) || it.contains(Pair(5, pos.second))
                } == null
}
assert(isHome('A', Pair(2,3), mapOf()))
assert(isHome('A', Pair(2,3), mapOf('A' to setOf(Pair(3,3)))))
assert(isHome('A', Pair(2,3), mapOf('.' to setOf(Pair(2,3)))))
assert(!isHome('A', Pair(2,3), mapOf('B' to setOf(Pair(3,3)))))
assert(!isHome('A', Pair(2,5), mapOf('B' to setOf(Pair(3,3)))))

fun wayToHomeIsFree(item: Char, pos: Pair<Int, Int>, layout: Map<Char, Set<Pair<Int, Int>>>): Boolean {
    val roomX = roomPositions[item]!!.first().second
    val (min, max) = if (pos.second < roomX) Pair(pos.second + 1, roomX) else Pair(roomX, pos.second)
    for (x in min until max) {
        if (hallWayPositions.contains(Pair(1, x)) && !layout['.']!!.contains(Pair(1, x))) {
            return false
        }
    }
    for (y in 2 until pos.first) {
        if (!layout['.']!!.contains(Pair(y, pos.second))) {
            return false
        }
    }
    return true
}
assert(wayToHomeIsFree('A', Pair(1,11), mapOf('.' to setOf(Pair(1, 4), Pair(1, 6), Pair(1, 8), Pair(1, 10)))))
assert(wayToHomeIsFree('A', Pair(5,5), mapOf('.' to setOf(Pair(1, 4), Pair(2, 5), Pair(3, 5), Pair(4, 5)))))
assert(wayToHomeIsFree('A', Pair(5,5), mapOf('A' to setOf(Pair(5,3), Pair(4,3), Pair(3,3)),'.' to setOf(Pair(1, 4), Pair(2, 5), Pair(3, 5), Pair(4, 5)))))
assert(!wayToHomeIsFree('A', Pair(5,5), mapOf('.' to setOf(Pair(1, 4), Pair(2, 5), Pair(3, 5)))))
assert(!wayToHomeIsFree('A', Pair(1,11), mapOf('.' to setOf(Pair(1, 6), Pair(1, 8), Pair(1, 10)))))

fun wayToSpotIsFree(pos: Pair<Int, Int>, spot: Pair<Int, Int>, layout: Map<Char, Set<Pair<Int, Int>>>): Boolean {
    val (min, max) = if (pos.second < spot.second) Pair(pos.second + 1, spot.second) else Pair(spot.second, pos.second)
    for (x in min until max) {
        if (hallWayPositions.contains(Pair(1, x)) && !layout['.']!!.contains(Pair(1, x))) {
            return false
        }
    }
    for (y in 2 until pos.first) {
        if (!layout['.']!!.contains(Pair(y, pos.second))) {
            return false
        }
    }
    return true
}
assert(wayToSpotIsFree(Pair(5,5), Pair(1,1), mapOf('.' to setOf(Pair(1,1), Pair(1,2), Pair(1, 4), Pair(2, 5), Pair(3, 5), Pair(4, 5)))))
assert(!wayToSpotIsFree(Pair(5,5), Pair(1,1), mapOf('.' to setOf(Pair(1,1), Pair(1,2), Pair(2, 5), Pair(3, 5)))))
assert(!wayToSpotIsFree(Pair(5,5), Pair(1,1), mapOf('.' to setOf(Pair(1,1), Pair(2, 5), Pair(3, 5), Pair(4, 5)))))

fun freeRoomPos(item: Char, layout: Map<Char, Set<Pair<Int, Int>>>): Pair<Int, Int> {
    return Pair(
        layout[item]!!.sortedBy { it.first }
            .firstOrNull { it.second == roomNumbers[item]!! }
            ?.first?.minus(1) ?: 5,
        roomNumbers[item]!!
    )
}
assert(freeRoomPos('A', mapOf('A' to setOf())) == Pair(5,3))
assert(freeRoomPos('A', mapOf('A' to setOf(Pair(5,3)))) == Pair(4,3))
assert(freeRoomPos('A', mapOf('A' to setOf(Pair(5,3), Pair(4,3)))) == Pair(3,3))
assert(freeRoomPos('A', mapOf('A' to setOf(Pair(5,3), Pair(4,3), Pair(3,3)))) == Pair(2,3))

fun getActions(layout: Map<Char, Set<Pair<Int, Int>>>): Set<Move> {
    val moves = mutableSetOf<Move>()
    for (key in layout.keys.filterNot { it == '.' }) {
        for (pos in layout[key]!!) {
            if (hallWayPositions.contains(pos)) {
                if (targetRoomAvailable(key, layout) && wayToHomeIsFree(key, pos, layout)) {
                    moves.add(Move(key, pos, freeRoomPos(key, layout)))
                }
            } else if (!isHome(key, pos, layout)) {
                if (targetRoomAvailable(key, layout) && wayToHomeIsFree(key, pos, layout)) {
                    moves.add(Move(key, pos, freeRoomPos(key, layout)))
                } else {
                    hallWayPositions.filter { layout['.']!!.contains(it) } // hallway spot is free
                        .filter { wayToSpotIsFree(pos, it, layout) }
                        .forEach {
                            moves.add(Move(key, pos, it))
                        }
                }
            }
        }
    }
    return moves
}
assert(getActions(mapOf('D' to setOf(Pair(1,6)), '.' to setOf(Pair(1,8), Pair(2,9), Pair(3,9), Pair(4,9), Pair(5,9)))) == setOf(Move('D', Pair(1,6), Pair(5,9))))
assert(getActions(mapOf('D' to setOf(Pair(1,6), Pair(5,9)), '.' to setOf(Pair(1,8), Pair(2,9), Pair(3,9), Pair(4,9)))) == setOf(Move('D', Pair(1,6), Pair(4,9))))
assert(getActions(mapOf('D' to setOf(Pair(1,6)), 'A' to setOf(Pair(1,8)), '.' to setOf(Pair(2,9), Pair(3,9), Pair(4,9), Pair(5,9), Pair(2,3), Pair(3,3), Pair(4,3), Pair(5,3),Pair(1,4)))) == setOf<Move>())
assert(getActions(mapOf('D' to setOf(Pair(2,7)), '.' to setOf(Pair(1,8), Pair(2,7), Pair(2,9), Pair(3,9), Pair(4,9), Pair(5,9)))) == setOf(Move('D', Pair(2,7), Pair(5,9))))

fun applyAction(layout: Map<Char, Set<Pair<Int, Int>>>, move: Move): Map<Char, Set<Pair<Int, Int>>> {
    val newLayout = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()
    for ((key, values) in layout) {
        newLayout[key] = values.map { Pair(it.first, it.second) }.toMutableSet()
    }
    newLayout[move.item]!!.remove(move.currentPos)
    newLayout[move.item]!!.add(move.nextPos)
    newLayout['.']!!.remove(move.nextPos)
    newLayout['.']!!.add(move.currentPos)
    return newLayout
}
assert(applyAction(mapOf('.' to setOf(Pair(5,3),Pair(4,3)), 'A' to setOf(Pair(3,5), Pair(4,5))), Move('A', Pair(3,5), Pair(5,3))) == mapOf('.' to setOf(Pair(3,5),Pair(4,3)), 'A' to setOf(Pair(5,3), Pair(4,5))))

fun getActionCost(move: Move) =
    (abs(move.currentPos.second - move.nextPos.second) + move.currentPos.first + move.nextPos.first - 2) *
            when (move.item) {
                'A' -> 1
                'B' -> 10
                'C' -> 100
                'D' -> 1000
                else -> error("unknown item: ${move.item}")
            }
assert(getActionCost(Move('A', Pair(3,5), Pair(5,3))) == 8)
assert(getActionCost(Move('A', Pair(1,11), Pair(5,3))) == 12)
assert(getActionCost(Move('B', Pair(3,5), Pair(5,3))) == 80)
assert(getActionCost(Move('C', Pair(3,5), Pair(5,3))) == 800)
assert(getActionCost(Move('D', Pair(3,5), Pair(5,3))) == 8000)

fun h(item: Char, nextPosition: Pair<Int, Int>) = 0

class BranchItem(val branchCost: Int, val state: Map<Char, Set<Pair<Int, Int>>>, val action: Move)

fun aStar(
    start: Map<Char, Set<Pair<Int, Int>>>,
    goal: Map<Char, Set<Pair<Int, Int>>>,
    h: (Char, Pair<Int, Int>) -> Int,
    getActionCost: (move: Move) -> Int,
    getActions: (Map<Char, Set<Pair<Int, Int>>>) -> Set<Move>
): Pair<List<Move>,Int> {
    val queue = mutableListOf<Pair<Int, Map<Char, Set<Pair<Int, Int>>>>>()
    queue.add(Pair(0, start))
    val visited = mutableSetOf(start)
    val branch = mutableMapOf<Map<Char, Set<Pair<Int, Int>>>, BranchItem>()
    var found = false

    while (queue.isNotEmpty()) {
        val currentState: Map<Char, Set<Pair<Int, Int>>> = queue.removeFirst().second
        val currentCost = branch[currentState]?.branchCost ?: 0 // g
        println(currentCost)

        if (currentState == goal) {
            found = true
            break
        }
        for (nextAction in getActions(currentState)) {
            val nextState = applyAction(currentState, nextAction)
            val branchCost = currentCost + getActionCost(nextAction)
            val queueCost = branchCost + h(nextAction.item, nextAction.nextPos)

            if (!visited.contains(nextState) || branch[nextState]!!.branchCost > branchCost) {
                visited.add(nextState)
                branch[nextState] = BranchItem(branchCost, currentState, nextAction)
                queue.removeIf { it.second == nextState }
                queue.add(Pair(queueCost, nextState))
                queue.sortBy { it.first }
            }
        }
    }

    var pathCost = 0
    val path = mutableListOf<Move>()
    if (found) {
        var n = goal
        pathCost = branch[n]!!.branchCost
        while (branch[n]!!.state !== start) {
            path.add(branch[n]!!.action)
            n = branch[n]!!.state
        }

    } else {
        println("no path found")
    }
    return Pair(path, pathCost)
}

println(aStar(layout, roomPositions.plus(
    '.' to setOf(Pair(1, 1), Pair(1, 2), Pair(1, 4), Pair(1, 6), Pair(1, 8), Pair(1, 10), Pair(1, 11)
    )), ::h, ::getActionCost, ::getActions))
