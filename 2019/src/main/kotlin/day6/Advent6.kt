package day6

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class MapHandler(private val mapList: List<String>) {
    private val thingMap = decodeMap()

    class Thing(val id: String) {
        val orbiters = mutableSetOf<String>()
    }

    private fun decodeMap(): Map<String, Thing> {
        val thingMap = mutableMapOf<String, Thing>()
        for (relation in mapList) {
            val orbitals = relation.split(')')
            val orbiteeId = orbitals[0]
            val orbiterId = orbitals[1]

            if (!thingMap.containsKey(orbiteeId)) {
                thingMap[orbiteeId] = Thing(orbiteeId)
            }
            if (!thingMap.containsKey(orbiterId)) {
                thingMap[orbiterId] = Thing(orbiterId)
            }

            thingMap.getValue(orbiteeId).orbiters.add(orbiterId)
        }
        return thingMap
    }

    fun getNumberOfOrbits(): Int {
        val rootThing = thingMap.getValue("COM")

        return getTotalOrbits(0, rootThing)
    }

    private fun getTotalOrbits(currentCount: Int, thing: Thing): Int {
        val newCount = currentCount + 1
        return currentCount + thing.orbiters
            .map { thingMap.getValue(it) }
            .map { getTotalOrbits(newCount, it) }
            .foldRight(0) { a, b -> a + b }
    }

    fun getJumpDistance(origin: String, target: String): Int {
        val originThing = thingMap.values.single { it.orbiters.contains(origin) }

        val resultDistance = searchBelow(originThing, 0, target)
        if (resultDistance != null) {
            return resultDistance
        }

        return searchUp(originThing, 0, target)
    }

    private fun searchBelow(thing: Thing, distance: Int, target: String): Int? {
        if (thing.orbiters.contains(target)) {
            return distance
        }

        val newDistance = distance + 1
        for (subThingId in thing.orbiters) {
            val resultDistance = searchBelow(thingMap.getValue(subThingId), newDistance, target)
            if (resultDistance != null) {
                return resultDistance
            }
        }
        return null
    }

    private fun searchUp(thing: Thing, distance: Int, target: String): Int {
        val currentDistance = distance + 1
        val upThing = thingMap.values.single { it.orbiters.contains(thing.id) }

        val resultDistance = searchBelow(upThing, currentDistance, target)
        if (resultDistance != null) {
            return resultDistance
        }

        return searchUp(upThing, currentDistance, target)
    }
}

fun load(input: String): List<String> {
    val inputStream = ClassLoader.getSystemResourceAsStream(input)
    val bufferedInput = BufferedReader(InputStreamReader(inputStream))
    return bufferedInput.lines()
        .collect(Collectors.toList())
}

fun main() {
    val mapHandler = MapHandler(load("6/input.txt"))
    println(mapHandler.getNumberOfOrbits())
    println(mapHandler.getJumpDistance("YOU", "SAN"))
}
