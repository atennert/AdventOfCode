#!/usr/bin/env kscript

import kotlin.math.max

operator fun List<String>.component2(): String = getOrElse(1) { "" }

fun findClosingIndex(s: String): Int {
    var open = 0
    for (ci in s.indices) {
        when (s[ci]) {
            '[' -> open += 1
            ']' -> open -= 1
            else -> {}
        }
        if (open == 0) {
            return ci
        }
    }
    return -1
}

fun parse(s: String): List<Any> {
    val l = mutableListOf<Any>()
    var sTemp = s
    while (sTemp.isNotEmpty()) {
        if (sTemp.startsWith('[')) {
            val sLength = findClosingIndex(sTemp) + 1
            l.add(parse(sTemp.drop(1).take(sLength - 2)))
            sTemp = sTemp.drop(sLength)
            if (sTemp.startsWith(',')) {
                sTemp = sTemp.drop(1)
            }
        } else {
            val (head, tail) = sTemp.split(',', limit = 2)
            l.add(head.toInt())
            sTemp = tail
        }
    }
    return l
}

fun getOrderedCount(): Int {
    var ordered = 0
    for (i in 0 until lines.size step 2) {
        ordered += if (comparator.compare(lines[i], lines[i + 1]) < 0) i / 2 + 1 else 0
    }
    return ordered
}

val comparator = object : Comparator<Any> {
    override fun compare(left: Any, right: Any): Int {
        when {
            left is List<*> && right is List<*> -> {
                for (i in 0 until max(left.size, right.size)) {
                    when {
                        i >= left.size -> return -1
                        i >= right.size -> return 1
                        else -> {
                            val result = compare(left[i]!!, right[i]!!)
                            if (result != 0) {
                                return result
                            }
                        }
                    }
                }
                return 0
            }

            left is List<*> -> return compare(left, listOf(right))
            right is List<*> -> return compare(listOf(left), right)
            else -> return left as Int - right as Int
        }
    }
}

val lines = mutableListOf<Any>()

do {
    lines.add(parse(readln().drop(1).dropLast(1)))
    lines.add(parse(readln().drop(1).dropLast(1)))
} while (readlnOrNull() != null)

println(getOrderedCount())

val a = listOf(listOf(2))
val b = listOf(listOf(6))
lines.add(a)
lines.add(b)
lines.sortWith(comparator)
println((lines.indexOf(a) + 1) * (lines.indexOf(b) + 1))
