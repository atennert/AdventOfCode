import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.max

operator fun <T> List<T>.component6(): T = get(5)
operator fun <T> List<T>.component7(): T = get(6)

fun evaluate(cond: String, a: Int, b: Int) = when (cond) {
    ">" -> a > b
    "<" -> a < b
    ">=" -> a >= b
    "<=" -> a <= b
    "==" -> a == b
    "!=" -> a != b
    else -> throw Error("Unknown operator $cond")
}

val registers = mutableMapOf<String, Int>()
var highestValueEver = Int.MIN_VALUE

Files.lines(Path.of("input.txt"))
    .forEach { line ->
        val (register, incDec, amount, _, condRegister, cond, value) = line.split(' ')
        if (!evaluate(cond, registers.getOrDefault(condRegister, 0), value.toInt())) {
            return@forEach
        }
        registers[register] = if (incDec == "inc") {
            registers.getOrDefault(register, 0) + amount.toInt()
        } else { // dec
            registers.getOrDefault(register, 0) - amount.toInt()
        }
        highestValueEver = max(highestValueEver, registers.getOrDefault(register, 0))
    }

println(registers.values.maxOrNull())
println(highestValueEver)
