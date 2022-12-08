import java.nio.file.Files
import java.nio.file.Path

println(Files.lines(Path.of("input.txt"))
    .map { it.split(" ") }
    .map { Pair(it.size == it.toSet().size, !hasAnagram(it)) }
    .reduce(
        Pair(0, 0),
        { (p1, p2), (e1, e2) -> Pair(p1 + if (e1) 1 else 0, p2 + if (e2) 1 else 0)},
        { (p11, p12), (p21, p22) ->
            println("$p11 $p12 $p21 $p22")
            Pair(p11 + p21, p12 + p22)
        }
    ))

fun hasAnagram(it: List<String>): Boolean {
    for (i in 0 until it.size - 1) {
        val chars1 = it[i].toCharArray().sorted()
        for (word in it.drop(i + 1)) {
            if (chars1 == word.toCharArray().sorted()) {
                return true
            }
        }
    }
    return false
}