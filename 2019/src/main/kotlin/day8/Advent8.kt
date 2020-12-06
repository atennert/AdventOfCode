package day8

import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val imageWidth = 25
    val imageHeight = 6
    val inputStream = ClassLoader.getSystemResourceAsStream("8/input.txt")
    val reader = BufferedReader(InputStreamReader(inputStream))
    val inputData = reader.readText().trim()

    val imageSize = imageWidth * imageHeight
    val images: List<String> = inputData
        .chunked(imageSize)

    val maxZeroImg = images.minBy { it.count { c -> c == '0' } }!!
    println(maxZeroImg)

    val numberOf1 = maxZeroImg.count { it == '1' }
    val numberOf2 = maxZeroImg.count { it == '2' }

    println("part 1 result: ${numberOf1 * numberOf2}\n")


    List(imageSize) { i ->
        images.map { it[i] }
            .reduceRight { new, old ->
                when (new) {
                    '2' -> old
                    else -> new
                }
            }
    }
        .joinToString("")
        .replace('0', ' ')
        .chunked(imageWidth)
        .forEach(::println)
}