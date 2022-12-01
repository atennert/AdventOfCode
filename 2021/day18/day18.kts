#!/usr/bin/env kscript

import kotlin.math.ceil

sealed interface SNNumber {
    val depth: Int

    val isGreater9: Boolean

    val magnitude: Long

    fun addLeft(value: Int)

    fun addRight(value: Int)
}

class SNPair(var left: SNNumber, var right: SNNumber) : SNNumber {
    override val depth
        get() = maxOf(1 + left.depth, 1 + right.depth)
    override val isGreater9
        get() = left.isGreater9 || right.isGreater9
    override val magnitude
        get() = left.magnitude * 3 + right.magnitude * 2

    override fun addLeft(value: Int) {
        this.left.addLeft(value)
    }

    override fun addRight(value: Int) {
        this.right.addRight(value)
    }

    fun replaceLeft(n: SNNumber) {
        left = n
    }

    fun replaceRight(n: SNNumber) {
        right = n
    }

    override fun toString() = "[$left,$right]"
}

class SNValue(var value: Int) : SNNumber {
    override val depth = 0
    override val isGreater9
        get() = value > 9
    override val magnitude: Long
        get() = value.toLong()

    override fun addLeft(value: Int) {
        this.value += value
    }

    override fun addRight(value: Int) {
        this.value += value
    }

    override fun toString() = value.toString()
}

operator fun SNNumber.plus(n2: SNNumber) = SNPair(this, n2)

fun String.splitSNumber(): Pair<String, String> {
    var pairCount = 0
    var first = ""
    for (c in this) {
        when (c) {
            '[' -> pairCount++
            ']' -> pairCount--
            ',' -> {
                if (pairCount == 0) {
                    break
                }
            }
        }
        first += c
    }
    val second = this.substring(first.length + 1)
    return Pair(first, second)
}

fun parseNumber(sNumber: String): SNNumber {
    if (sNumber.matches(Regex("\\d+"))) {
        return SNValue(sNumber.toInt())
    }
    val (first, second) = sNumber
        .replace(Regex("\\[(.*)]"), "$1")
        .splitSNumber()
    return SNPair(parseNumber(first), parseNumber(second))
}

fun SNNumber.explode(level: Int, left: SNNumber?, right: SNNumber?): SNNumber {
    if (this is SNPair) {
        if (level == 4) {
            left?.addRight((this.left as SNValue).value)
            right?.addLeft((this.right as SNValue).value)
            return SNValue(0)
        }
        if (this.left.depth == 4 - level) {
            this.replaceLeft(this.left.explode(level + 1, left, this.right))
        } else {
            this.replaceRight(this.right.explode(level + 1, this.left, right))
        }
    }
    return this
}

fun SNNumber.split(): SNNumber {
    return when (this) {
        is SNValue -> SNPair(SNValue(value / 2), SNValue(ceil(value / 2.0).toInt()))
        is SNPair -> if (left.isGreater9) {
            this.replaceLeft(left.split())
            this
        } else {
            this.replaceRight(right.split())
            this
        }
        else -> error("")
    }
}

fun SNNumber.addReduce(other: SNNumber): SNNumber {
    var newNumber: SNNumber = this + other
    while (newNumber.depth > 4 || newNumber.isGreater9) {
        while (newNumber.depth > 4) {
            newNumber = newNumber.explode(0, null, null)
        }
        if (newNumber.isGreater9) {
            newNumber = newNumber.split()
        }
    }
    return newNumber
}

val numbers = mutableListOf(readln())
var currentNumber = parseNumber(numbers[0])

while (true) {
    val line = readlnOrNull() ?: break
    val newNumber = parseNumber(line)
    numbers.add(line)

    currentNumber = currentNumber.addReduce(newNumber)
}

println("magnitude: ${currentNumber.magnitude}")

var maxMagnitude = 0L
numbers.forEachIndexed { i1, n1 ->
    numbers.forEachIndexed { i2, n2 ->
        if (i1 != i2) {
            val m = parseNumber(n1).addReduce(parseNumber(n2)).magnitude
            if (m > maxMagnitude) {
                maxMagnitude = m
            }
        }
    }
}
println("max magnitude: $maxMagnitude")
