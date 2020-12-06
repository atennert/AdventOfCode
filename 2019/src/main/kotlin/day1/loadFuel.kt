package day1

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

fun loadInts(inputFile: String): List<Int> {
    val inputStream = ClassLoader.getSystemResource(inputFile).openStream()
    val bufferedInput = BufferedReader(InputStreamReader(inputStream))
    val ints = bufferedInput.lines()
        .map(Integer::parseInt)
        .collect(Collectors.toList())
    bufferedInput.close()
    return ints
}