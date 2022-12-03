#!/usr/bin/env kscript

fun getItemPriority(item: Char): Int {
    return if (item.isUpperCase()) {
        item.code - 38
    } else {
        item.code - 96
    }
}

var priorities1 = 0
lateinit var sameItems: Set<Char>
var counter = 0
var priorities2 = 0

while (true) {
    val backpack = readlnOrNull() ?: break
    val halfSize = backpack.length / 2
    val first = backpack.take(halfSize).toSet()
    val second = backpack.drop(halfSize).toSet()

    priorities1 += getItemPriority(first.intersect(second).first())

    sameItems = if (counter == 0) {
        backpack.toSet()
    } else {
        backpack.toSet().intersect(sameItems)
    }
    if (counter == 2) {
        priorities2 += getItemPriority(sameItems.first())
    }
    counter = (counter + 1) % 3
}

println(priorities1)
println(priorities2)
