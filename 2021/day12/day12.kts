#!/usr/bin/env kscript

val caveMap = mutableMapOf<String, MutableSet<String>>()
while (true) {
    val (cave1, cave2) = readlnOrNull()?.split('-') ?: break
    if (!caveMap.containsKey(cave1)) {
        caveMap[cave1] = mutableSetOf()
    }
    if (!caveMap.containsKey(cave2)) {
        caveMap[cave2] = mutableSetOf()
    }
    if (cave1 != "end" && cave2 != "start") {
        caveMap[cave1]?.add(cave2)
    }
    if (cave1 != "start" && cave2 != "end") {
        caveMap[cave2]?.add(cave1)
    }
}

fun String.isLowerCase() = this[0].isLowerCase()

fun <T> List<T>.hasDouble() = this.find { e -> this.filter { it == e }.size > 1 } != null

fun visitCaves(visitedCaves: List<String>, ignoreCondition: (String, List<String>) -> Boolean): List<List<String>> {
    if (visitedCaves.last() == "end") {
        return listOf(visitedCaves)
    }
    return caveMap[visitedCaves.last()]
        ?.filterNot { ignoreCondition(it, visitedCaves) }
        ?.flatMap { visitCaves(visitedCaves + it, ignoreCondition) } ?: listOf()
}

println("routes: ${caveMap["start"]
    ?.flatMap { visitCaves(listOf(it)) { s, vc ->
        s.isLowerCase() && vc.contains(s)
    } }?.size}")
println("more routes: ${caveMap["start"]
    ?.flatMap { visitCaves(listOf(it)) { s, vc ->
        val lowerVc = vc.filter { s1 -> s1.isLowerCase() }
        s.isLowerCase() && lowerVc.contains(s) && lowerVc.hasDouble()
    } }?.size}")
