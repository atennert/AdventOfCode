package day16

import java.nio.file.Files
import java.nio.file.Path

interface Command {
    fun exec()
}

class Spin(s: String) : Command {
    private val n = s.drop(1).toInt()
    override fun exec() {
        position = (position.takeLast(n) + position.dropLast(n)).toMutableList()
    }
}

class Exchange(s: String) : Command {
    private val p1: Int
    private val p2: Int

    init {
        val x = s.drop(1).split('/').map { it.toInt() }
        p1 = x[0]
        p2 = x[1]
    }

    override fun exec() {
        val e1 = position[p1]
        position[p1] = position[p2]
        position[p2] = e1
    }
}

class Swap(s: String) : Command {
    private val n1: Char
    private val n2: Char

    init {
        val x = s.drop(1).split('/').map { it.single() }
        n1 = x[0]
        n2 = x[1]
    }

    override fun exec() {
        val p1 = position.indexOf(n1)
        val p2 = position.indexOf(n2)
        position[p1] = n2
        position[p2] = n1
    }
}

var position = "abcdefghijklmnop".toCharArray().toMutableList()

val moves: List<Command> = Files.readString(Path.of("input.txt"))
    .split(',')
    .map {
        when (it[0]) {
            's' -> Spin(it)
            'x' -> Exchange(it)
            'p' -> Swap(it)
            else -> throw Error("WHAAAAAT???")
        }
    }

moves.forEach { it.exec() }
println(position.joinToString(""))

var i = 1
var target = 1000000000
while (i < target) {
    moves.forEach { it.exec() }
    i++
    if (position.joinToString("") == "abcdefghijklmnop") {
        target = target % i + i
    }
}
println(position.joinToString(""))
