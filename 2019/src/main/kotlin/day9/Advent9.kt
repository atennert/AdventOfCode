package day9

import day2.IntCodeComputer
import day2.loadCode

fun main() {
    val code = loadCode("9/input.txt")

    // Part 1
    IntCodeComputer(
        {"1"},
        {out, pos -> println ("$pos :: $out")}
    ).runIntCode(code)

    // Part 2
    IntCodeComputer(
        {"2"},
        {out, pos -> println ("$pos :: $out")}
    ).runIntCode(code)
}