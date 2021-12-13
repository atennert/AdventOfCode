#!/usr/bin/env kscript

class Dot(val x: Int, val y: Int) {
    override fun equals(other: Any?) = other is Dot && other.x == x && other.y == y
    override fun hashCode() = 31 * x + y
    fun needsFolding(fold: Fold) = (if (fold.axis == 'x') x else y) > fold.index
    fun fold(fold: Fold) = if (fold.axis == 'x') Dot(fold.index * 2 - x, y) else Dot(x, fold.index * 2 - y)
}

class Fold(val axis: Char, val index: Int)

var dots = mutableSetOf<Dot>()
while (true) {
    val line = readln()
    if (line.isEmpty()) {
        break
    }
    val (x, y) = line.split(',').map(String::toInt)
    dots.add(Dot(x, y))
}

val folds = mutableListOf<Fold>()
while (true) {
    val (axis, index) = readlnOrNull()?.replace("fold along ", "")
        ?.split('=') ?: break

    folds.add(Fold(axis[0], index.toInt()))
}

fun fold(fold: Fold) {
    dots = dots.map { if (it.needsFolding(fold)) it.fold(fold) else it}.toMutableSet()
}

fold(folds.removeFirst())

println("dot count: ${dots.size}")

folds.forEach(this::fold)

val maxX = dots.maxOf(Dot::x)
val maxY = dots.maxOf(Dot::y)

println("Code:")
for (y in 0 .. maxY) {
    var line = ""
    for (x in 0 .. maxX) {
        line += if (dots.contains(Dot(x, y))) '#' else ' '
    }
    println(line)
}