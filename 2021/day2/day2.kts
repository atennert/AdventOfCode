#!/usr/bin/env kscript

var horizontal = 0
var vertical = 0
var vertical2 = 0
var aim = 0
while (true) {
    val input = readlnOrNull()?.split(" ") ?: break
    val value = input[1].toInt()
    when (input[0]) {
        "forward" -> {
            horizontal += value
            vertical2 += aim * value
        }
        "up" -> {
            vertical -= value
            aim -= value
        }
        "down" -> {
            vertical += value
            aim += value
        }
    }
}
println(horizontal * vertical)
println(horizontal * vertical2)