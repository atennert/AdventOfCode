import java.nio.file.Files
import java.nio.file.Path

val mostCommon = Files.lines(Path.of("input.txt"))
    .map(String::toCharArray)
    .map { it.map(Char::digitToInt)
        .map { i -> if (i == 0) -1 else i } }
    .reduce { a, b -> a.zip(b) { x, y -> x + y } }
    .get()

var gammaRate = 0
var epsilonRate = 0
mostCommon.forEach { i ->
    gammaRate = gammaRate.shl(1)
    epsilonRate = epsilonRate.shl(1)
    if (i > 0) {
        gammaRate = gammaRate.or(1)
    } else {
        epsilonRate = epsilonRate.or(1)
    }
}

println(gammaRate * epsilonRate)