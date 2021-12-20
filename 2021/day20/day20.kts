#!/usr/bin/env kscript

var enhancementAlgorithm = readln().map { if (it == '#') 1 else 0 }
readln() // empty line

var image = mutableListOf<MutableList<Int>>()
while (true) {
    val line = readlnOrNull() ?: break
    image.add(".$line.".map { if (it == '#') 1 else 0 }
        .toMutableList())
}
image.add(0, MutableList(image[0].size) { 0 })
image.add(MutableList(image[0].size) { 0 })

fun enhance(image: List<List<Int>>): List<List<Int>> {
    val newImage = image.map {
        val n = it.toMutableList()
        n.add(0, it[0])
        n.add(it[0])
        n
    }.toMutableList()
    newImage.add(0, MutableList(newImage[0].size) { newImage[0][0] })
    newImage.add(MutableList(newImage[0].size) { newImage[0][0] })

    for (i in 0 until newImage.size) {
        val line = newImage[i]
        val oldI = i - 1
        for (j in 0 until line.size) {
            val oldJ = j - 1
            newImage[i][j] = getValue(image, oldI, oldJ)
        }
    }
    return newImage
}

fun gv(image: List<List<Int>>, i: Int, j: Int): Int {
    return if (i < 0 || i >= image.size || j < 0 || j >= image[0].size) {
        image[0][0]
    } else {
        image[i][j]
    }
}

fun getValue(image: List<List<Int>>, oldI: Int, oldJ: Int): Int {
    return enhancementAlgorithm[listOf(
        gv(image, oldI - 1, oldJ - 1), gv(image, oldI - 1, oldJ), gv(image, oldI - 1, oldJ + 1),
        gv(image, oldI, oldJ - 1), gv(image, oldI, oldJ), gv(image, oldI, oldJ + 1),
        gv(image, oldI + 1, oldJ - 1), gv(image, oldI + 1, oldJ), gv(image, oldI + 1, oldJ + 1),
    ).reduce { acc, i -> acc.shl(1) + i }]
}

var newImage: List<List<Int>> = image
for (i in 1..2) {
    newImage = enhance(newImage)
}

println("lit count: ${newImage.sumOf { it.sum() }}")

for (i in 3..50) {
    newImage = enhance(newImage)
}

println("lit count 2: ${newImage.sumOf { it.sum() }}")
