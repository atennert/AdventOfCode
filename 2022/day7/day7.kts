#!/usr/bin/env kscript

class File(val size: Int)

class Directory(val name: String) {
    var files = mutableListOf<File>()
    var directories = mutableListOf<Directory>()

    fun getSizes(): List<Int> {
        val dirSums = directories.map { it.getSizes() }
        return dirSums.flatten().plus(dirSums.sumOf { it.last() } + files.sumOf { it.size })
    }
}

fun handleCD(path: MutableList<Directory>, line: String) {
    val cdParts = line.split(' ')
    when (cdParts[2]) {
        "/" -> {
            path.clear()
            path.add(root)
        }
        ".." -> path.removeLast()
        else -> path.add(path.last().directories.first { it.name == cdParts[2] })
    }
}

fun handleDir(path: MutableList<Directory>, line: String) {
    val dirParts = line.split(' ')
    path.last().directories.add(Directory(dirParts[1]))
}

fun handleFile(path: MutableList<Directory>, line: String) {
    val fileParts = line.split(' ')
    path.last().files.add(File(fileParts[0].toInt()))
}

val root = Directory("/")
val path = mutableListOf(root)

while (true) {
    val line = readlnOrNull() ?: break
    when {
        line.startsWith("$ cd") -> handleCD(path, line)
        line.startsWith("dir") -> handleDir(path, line)
        line.startsWith("$ ls") -> {}
        else -> handleFile(path, line)
    }
}

val sizes = root.getSizes()
println(sizes.filter { it <= 100000 }.sum())

val available = 70000000 - sizes.last()
val required = 30000000 - available
println(sizes.filter { it >= required }.minOrNull())