package day9

import java.nio.file.Files
import java.nio.file.Path

var score = 0
var level = 0
var insideTrash = false
var ignoreNext = false
var trash = 0

Files.readString(Path.of("input.txt"))
    .toCharArray()
    .forEach {
        if (insideTrash && it != '!' && !ignoreNext && it != '>') {
            trash++
        }
        when {
            ignoreNext -> ignoreNext = false
            it == '!' -> ignoreNext = true
            it == '>' -> insideTrash = false
            insideTrash -> {/* noop */}
            it == '{' -> {
                level++
                score += level
            }
            it == '}' -> level--
            it == '<' -> insideTrash = true
            else -> {/* noop */}
        }
    }

println(score)
println(trash)