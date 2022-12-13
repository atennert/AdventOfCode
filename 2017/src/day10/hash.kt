package day10

fun run(input: List<Int>, times: Int): List<Int> {
    var position = 0
    var skipSize = 0
    var numbers = List(256) { it }

    for (i in 1 .. times) {
        input.forEach {
            numbers = numbers.drop(position) + numbers.take(position)
            numbers = numbers.take(it).reversed() + numbers.drop(it)
            numbers = numbers.drop(numbers.size - position) + numbers.take(numbers.size - position)
            position = (position + it + skipSize) % numbers.size
            skipSize++
        }
    }
    return numbers
}

fun computeHash(input: String, times: Int): String {
    val converted = input.toCharArray().map { it.code } + listOf(17, 31, 73, 47, 23)
    val sparseHash = run(converted, times)
    val denseHash = sparseHash.chunked(16)
        .map { it.reduce { acc, i -> acc xor i } }
        .joinToString("") { it.toString(16).padStart(2, '0') }
    return denseHash
}
