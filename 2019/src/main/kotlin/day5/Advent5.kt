package day5

import day2.IntCodeComputer
import day2.loadCode

fun inputFcn(): String {
    print("Please input command value: ")
    return readLine()!!
}

fun outputFcn(parameter: String, position: Int) {
    println("position: $position; value: $parameter")
}

fun main() {
    val computer = IntCodeComputer(::inputFcn, ::outputFcn)
    computer.runIntCode(loadCode("5/input.txt"))
}
