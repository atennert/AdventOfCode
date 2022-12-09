import java.nio.file.Files
import java.nio.file.Path

class Program(val name: String) {
    var weight: Int = 0
    val subPrograms = mutableSetOf<Program>()
    var parent: Program? = null

    fun towerWeight(): Int = this.weight + this.subPrograms.sumOf { it.towerWeight() }

    fun getUnbalanced(): MutableMap<Int, MutableSet<Program>>? {
        for (p in this.subPrograms) {
            val unbalanced = p.getUnbalanced()
            if (unbalanced != null) {
                return unbalanced
            }
        }
        val weights = this.subPrograms.fold(mutableMapOf<Int, MutableSet<Program>>()) { m, p ->
            val programs = m.getOrDefault(p.towerWeight(), mutableSetOf())
            programs.add(p)
            m[p.towerWeight()] = programs
            m
        }
        return if (weights.size <= 1) {
            null
        } else {
            weights
        }
    }
}

fun getOrCreateProgram(name: String): Program {
    val program = programs.getOrDefault(name, Program(name))
    programs[program.name] = program
    return program
}

val programs = mutableMapOf<String, Program>()

Files.lines(Path.of("input.txt"))
    .forEach {
        val content = it.split(" ")
        val program = getOrCreateProgram(content[0])
        program.weight = content[1].drop(1).dropLast(1).toInt()

        for (i in 3 until content.size) {
            val subProgram = getOrCreateProgram(content[i].replace(",", ""))
            subProgram.parent = program
            program.subPrograms.add(subProgram)
        }
    }

var rootProgram: Program? = programs.values.first()
while (rootProgram?.parent != null) {
    rootProgram = rootProgram?.parent
}

println(rootProgram?.name)

val unbalanced = rootProgram?.getUnbalanced()!!
val bad = unbalanced.values.first { it.size == 1 }
val good = unbalanced.values.first { it.size > 1 }
val targetWeight = good.first().towerWeight() - (bad.first().subPrograms.size * (bad.first().subPrograms.firstOrNull()?.towerWeight() ?: 0))

println(targetWeight)
