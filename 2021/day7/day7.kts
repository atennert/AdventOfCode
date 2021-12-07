#!/usr/bin/env kscript

import kotlin.math.abs

val positions = readln()
    .split(",")
    .map(String::toLong)

val minPosition = positions.minOrNull() ?: error("")
val maxPosition = positions.maxOrNull() ?: error("")

var leastFuelPosition1 = maxPosition
var leastFuel1 = positions.sum()
var leastFuelPosition2 = maxPosition
var leastFuel2 = Long.MAX_VALUE
for (i in minPosition .. maxPosition) {
    val fuel1 = positions.sumOf { abs(it - i) }
    if (fuel1 < leastFuel1) {
        leastFuel1 = fuel1
        leastFuelPosition1 = i
    }
    val fuel2 = positions.sumOf { (0..abs(it - i)).sum() }
    if (fuel2 < leastFuel2) {
        leastFuel2 = fuel2
        leastFuelPosition2 = i
    }
}

println("pos: $leastFuelPosition1 - fuel: $leastFuel1")
println("pos: $leastFuelPosition2 - fuel: $leastFuel2")
