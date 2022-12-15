import java.nio.file.Files
import java.nio.file.Path

val registers = mutableMapOf<Char, Long>()

val commands = Files.readAllLines(Path.of("input.txt"))
    .map { it.split(' ') }

fun get(x: String, registers: Map<Char, Long>): Long {
    return if (x.matches(Regex("[a-z]"))) {
        registers[x.single()] ?: 0L
    } else {
        x.toLong()
    }
}

var index = 0
var lastSend= -1L
while (index >= 0 && index < commands.size) {
    val command = commands[index]
    when (command[0]) {
        "snd" -> lastSend = get(command[1], registers)
        "set" -> registers[command[1].single()] = get(command[2], registers)
        "add" -> registers[command[1].single()] = get(command[1], registers) + get(command[2], registers)
        "mul" -> registers[command[1].single()] = get(command[1], registers) * get(command[2], registers)
        "mod" -> registers[command[1].single()] = get(command[1], registers) % get(command[2], registers)
        "jgz" -> {
            if (get(command[1], registers) > 0) {
                index += get(command[2], registers).toInt()
                continue
            }
        }
        "rcv" -> {
            if (get(command[1], registers) > 0) {
                break
            }
        }
        else -> throw Error("Unknown command $command")
    }
    index++
}
println(lastSend)


class Program(id: Long) {
    private val registers = mutableMapOf(
        'p' to id
    )
    private val queue = mutableListOf<Long>()
    private var index = 0
    var sndCount = 0

    fun exec(pOther: Program): Boolean {
        if (index > commands.size) {
            return false
        }
        val command = commands[index]
        when (command[0]) {
            "snd" -> {
                pOther.queue.add(get(command[1], registers))
                sndCount++
            }
            "set" -> registers[command[1].single()] = get(command[2], registers)
            "add" -> registers[command[1].single()] = get(command[1], registers) + get(command[2], registers)
            "mul" -> registers[command[1].single()] = get(command[1], registers) * get(command[2], registers)
            "mod" -> registers[command[1].single()] = get(command[1], registers) % get(command[2], registers)
            "jgz" -> {
                if (get(command[1], registers) > 0) {
                    index += get(command[2], registers).toInt()
                    return true
                }
            }
            "rcv" -> {
                if (queue.isEmpty()) {
                    return false
                } else {
                    registers[command[1].single()] = queue.removeFirst()
                }
            }
            else -> throw Error("Unknown command $command")
        }
        index++
        return true
    }
}

val p0 = Program(0)
val p1 = Program(1)
while (true) {
    val p0Done = p0.exec(p1)
    val p1Done = p1.exec(p0)
    if (!p0Done && !p1Done) {
        break
    }
}
println(p1.sndCount)