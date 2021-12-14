#!/usr/bin/env kscript

val template = readln()
readln()
val formulas = mutableMapOf<Pair<Char, Char>, Char>()
while (true) {
    val (polymer, result) = readlnOrNull()?.split(" -> ") ?: break
    formulas[Pair(polymer[0], polymer[1])] = result[0]
}

// Part 1: construct string solution

var polymer1 = template
for (c in 1..10) {
    var newPolymer = ""
    for (i in 0 until (polymer1.length - 1)) {
        newPolymer += polymer1[i] + formulas[Pair(polymer1[i], polymer1[i+1])].toString()
    }
    newPolymer += polymer1.last()
    polymer1 = newPolymer
}

fun minMaxDiff(polymer: String): Long {
    val elementCounts = polymer.fold(mutableMapOf<Char, Long>()) { acc, c ->
        acc[c] = acc.getOrDefault(c, 0L) + 1L
        acc
    }
    return elementCounts.maxOf { it.value } - elementCounts.minOf { it.value }
}

println("Min-Max-Diff: ${minMaxDiff(polymer1)}")

// Part 2 fast solution

var pairs = mutableMapOf<Pair<Char, Char>, Long>()
for (i in 0 until (template.length - 1)) {
    pairs[Pair(template[i], template[i + 1])] = pairs.getOrDefault(Pair(template[i], template[i + 1]), 0) + 1
}

for (c in 1..40) {
    val newPairs = mutableMapOf<Pair<Char, Char>, Long>()
    for ((e1, e3) in pairs.keys) {
        val e2 = formulas[Pair(e1, e3)]!!
        val count = pairs[Pair(e1, e3)]!!
        newPairs[Pair(e1, e2)] = newPairs.getOrDefault(Pair(e1, e2), 0) + count
        newPairs[Pair(e2, e3)] = newPairs.getOrDefault(Pair(e2, e3), 0) + count
    }
    pairs = newPairs
}
val letters = mutableMapOf<Char, Long>()
pairs.forEach { (a, _), count ->
    letters[a] = letters.getOrDefault(a, 0) + count
}
letters[template.last()] = letters[template.last()]!! + 1

println("Min-Max-Diff 2: ${letters.maxOf { it.value } - letters.minOf { it.value }}")
