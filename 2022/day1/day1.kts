#!/usr/bin/env kscript

val caloriesPerElf = mutableListOf<Int>()
var sum = 0
while (true) {
    val calories = readlnOrNull() ?: break
    if (calories.isEmpty()) {
        caloriesPerElf.add(sum)
        sum = 0
    } else {
        sum += calories.toInt()
    }
}
caloriesPerElf.add(sum)

println("max calories: ${caloriesPerElf.maxOrNull()}")

println("top 3 max calories: ${caloriesPerElf.sortedDescending()
    .take(3)
    .sum()}")
