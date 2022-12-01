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

val commands = mutableListOf<MutableList<Command>>()
Files.readAllLines(Paths.get("input.txt"))
    .forEach { line: String ->
        val (type, a, b) = line.split(' ').plus(null)
        if (type == "inp") {
            commands.add(mutableListOf())
        }
        commands.last().add(Command(type!!, a!!, b))
    }
//while (true) {
//    val (type, a, b) = readlnOrNull()?.split(' ')?.plus(null) ?: break
//    if (type == "inp") {
//        inputIndexes.add(count)
//    }
//    commands.add(Command(type!!, a!!, b))
//    count++
//}

val init = longArrayOf(9,9,9,8,8,9,9,9,9,9,9,9,9,9)

fun runAlu(memory: List<Long>, cmdIndex: Int): String? {
    for (i in (1..init[cmdIndex]).reversed()) {
        when (cmdIndex) {
            0 -> println(i)
            1 -> println(" $i")
            2 -> println("  $i")
            3 -> println("   $i")
            4 -> println("    $i")
            5 -> println("     $i")
        }
        val alu = ALU { i }
        alu.memory = memory.toList()
        for (c in commands[cmdIndex]) {
            alu.executeCommand(c)
        }
        if (cmdIndex < 13) {
            val result = runAlu(alu.memory, cmdIndex + 1)
            if (result != null) {
                return "$i$result"
            }
        } else if (alu.z == 0L) {
            return "$i"
        }
    }
    return null
}

println(runAlu(listOf(0,0,0,0), 0))