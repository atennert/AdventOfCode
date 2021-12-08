#!/usr/bin/env kscript

fun String.containsAllOf(check: String): Boolean {
    for (c in check) {
        if (!this.contains(c)) {
            return false
        }
    }
    return true
}

fun analyseLayouts(layouts: List<String>): Map<String, Char> {
    val one = layouts.find { it.length == 2 }!!
    val four = layouts.find { it.length == 4 }!!
    val seven = layouts.find { it.length == 3 }!!
    val eight = layouts.find { it.length == 7 }!!
    val twoThreeFive = layouts.filter { it.length == 5 }
    val zeroSixNine = layouts.filter { it.length == 6 }
    val three = twoThreeFive.find { it.containsAllOf(one) }!!
    val nine = zeroSixNine.find { it.containsAllOf(three) }!!
    val five = twoThreeFive.find { it != three && nine.containsAllOf(it) }!!
    val two = twoThreeFive.find { it != three && it != five }!!
    val six = zeroSixNine.find { it != nine && it.containsAllOf(five) }!!
    val zero = zeroSixNine.find { it != six && it != nine }!!
    return mapOf(one to '1', two to '2', three to '3', four to '4', five to '5', six to '6', seven to '7', eight to '8', nine to '9', zero to '0')
}

var sum1 = 0
var sum2 = 0
while (true) {
    val (layout, actual) = readlnOrNull()?.split(Regex(" \\| ")) ?: break
    val layouts = analyseLayouts(layout.split(' ').map { it.toCharArray().sorted().joinToString("") })
    val actuals = actual.split(' ').map { it.toCharArray().sorted().joinToString("") }

    sum1 += actuals.count { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }

    sum2 += actuals.map { layouts[it]!! }.joinToString("").toInt()
}

println(sum1)
println(sum2)
