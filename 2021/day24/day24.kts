#!/usr/bin/env kscript

import java.nio.file.Files
import java.nio.file.Paths

data class Command(val type: String, val a: String, val b: String?)

class ALU(val readInput: () -> Long) {
    var w = 0L
        private set
    var x = 0L
        private set
    var y = 0L
        private set
    var z = 0L
        private set
    var memory: List<Long>
        get() = listOf(w,x,y,z)
        set(mem) {
            w = mem[0]
            x = mem[1]
            y = mem[2]
            z = mem[3]
        }

    private fun read(m: String): Long {
        return when(m) {
            "w" -> w
            "x" -> x
            "y" -> y
            "z" -> z
            else -> m.toLong()
        }
    }

    private fun write(a: String, value: Long) {
        when(a) {
            "w" -> w = value
            "x" -> x = value
            "y" -> y = value
            "z" -> z = value
            else -> error("unknown target: $a")
        }
    }

    fun executeCommand(command: Command) {
        when (command.type) {
            "inp" -> write(command.a, readInput())
            "add" -> write(command.a, read(command.a) + read(command.b!!))
            "mul" -> write(command.a, read(command.a) * read(command.b!!))
            "div" -> write(command.a, read(command.a) / read(command.b!!))
            "mod" -> write(command.a, read(command.a) % read(command.b!!))
            "eql" -> write(command.a, if (read(command.a) == read(command.b!!)) 1 else 0)
            else -> error("unknown command: ${command.type}")
        }
    }
}

val commands = mutableListOf<Command>()
val inputIndexes = mutableListOf<Int>()
var count = 0
Files.readAllLines(Paths.get("input.txt"))
    .forEachIndexed { i, line: String ->
        val (type, a, b) = line.split(' ').plus(null)
        if (type == "inp") {
            inputIndexes.add(i)
        }
        commands.add(Command(type!!, a!!, b))
    }
//while (true) {
//    val (type, a, b) = readlnOrNull()?.split(' ')?.plus(null) ?: break
//    if (type == "inp") {
//        inputIndexes.add(count)
//    }
//    commands.add(Command(type!!, a!!, b))
//    count++
//}

//val input = LongArray(14) { 9L }
val input = longArrayOf(9,9,9,8,9,5,8,5,4,6,1,5,9,3)
val memories = mutableListOf<List<Long>>()
var idx = -1
val alu = ALU { input[idx] }

fun decrease(): Int {
    var idx = input.size - 1
    while (true) {
        input[idx] = input[idx] - 1
        if (input[idx] == 0L) {
            input[idx] = 9
            idx--
        } else {
            break
        }
    }
    return idx
}

var drop = 0
count = 0
while (true) {
    if (count == 0) {
        println(input.joinToString(""))
    }
    count = (count + 1) % 1000000
    for (command in commands.drop(inputIndexes[drop])) {
        if (command.type == "inp") {
            memories.add(alu.memory)
            idx++
        }
        alu.executeCommand(command)
    }
    if (alu.z != 0L) {
        drop = decrease()
        for (i in 1..(14-drop)) {
            memories.removeLast()
            idx--
        }
        alu.memory = memories.lastOrNull()!!
    } else {
        break
    }
}
println("${alu.z} -> ${input.joinToString("")}")