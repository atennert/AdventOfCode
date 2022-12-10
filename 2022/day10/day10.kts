#!/usr/bin/env kscript

fun runCycle() {
    cycle++
    if (cycle % 40 == 20) {
        signalStrength += x * cycle
    }
    val pixelX = (cycle - 1) % 40
    pixels.add(x - 1 <= pixelX && pixelX <= x + 1)
}

var x = 1
var cycle = 0
var signalStrength = 0
var pixels = mutableListOf<Boolean>()

while (true) {
    val instruction = readlnOrNull() ?: break
    runCycle()
    if (instruction.startsWith("addx")) {
        runCycle()
        x += instruction.split(' ')[1].toInt()
    }
}

println(signalStrength)
pixels.map { if (it) '#' else ' ' }
    .joinToString("")
    .chunked(40)
    .forEach { println(it) }