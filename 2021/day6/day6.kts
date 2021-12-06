#!/usr/bin/env kscript

var lanternFish: MutableMap<Int, Long> = readln()
    .split(",")
    .map(String::toInt)
    .fold(mutableMapOf()) { acc, cycle ->
        acc[cycle] = (acc[cycle] ?: 0) + 1L
        acc
    }

fun computePopulation(initialFish: Map<Int, Long>, days: Int): Map<Int, Long> {
    var lanternFish = initialFish.toMutableMap()
    for (day in 1..days) {
        val fishToAdd = lanternFish[0] ?: 0
        lanternFish[7] = fishToAdd + (lanternFish[7] ?: 0)
        lanternFish.remove(0)
        lanternFish = lanternFish.mapKeys { it.key - 1 }.toMutableMap()
        lanternFish[8] = fishToAdd
    }
    return lanternFish
}

println("fish: ${computePopulation(lanternFish, 80).values.sum()}")
println("fish: ${computePopulation(lanternFish, 256).values.sum()}")
