import java.nio.file.Files
import java.nio.file.Path

var horizontal = 0
var vertical = 0
var vertical2 = 0
var aim = 0
Files.lines(Path.of("input.txt"))
    .forEach {
        val input = it.split(" ")
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