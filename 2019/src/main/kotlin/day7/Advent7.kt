package day7

import day2.IntCodeComputer
import day2.loadCode
import java.util.concurrent.ArrayBlockingQueue

fun main() {
    val code = loadCode("7/input.txt")
    val results = mutableListOf<Int>()

//    runPart1(code, results)
    runPart2(code, results)

    println("-> ${results.max()}")
}

private fun runPart1(
    code: List<String>,
    results: MutableList<Int>
) {
    val amplifierParameters = listOf(0, 1, 2, 3, 4)

    for (params in getPermutations(amplifierParameters)) {
        val inputOutputList = mutableListOf("0")
        params.forEach {
            val inputList = mutableListOf(it.toString(), inputOutputList.removeAt(0))
            val computer = IntCodeComputer(
                { inputList.removeAt(0) },
                { out, _ -> inputOutputList.add(out) }
            )
            computer.runIntCode(code)
        }

        println(inputOutputList.last())
        results.add(inputOutputList.last().toInt())
    }
}

private fun runPart2(
    code: List<String>,
    results: MutableList<Int>
) {
    val amplifierParameters = listOf(5, 6, 7, 8, 9)

    for (params in getPermutations(amplifierParameters)) {
        val ioLists = List<ArrayBlockingQueue<String>>(params.size) { ArrayBlockingQueue(2) }
        params.forEachIndexed { i, param -> ioLists[i].add(param.toString()) }
        ioLists[0].add("0")
        var result = "0"

        val computer = List(params.size) {
            Thread {
                IntCodeComputer(
                    { ioLists[it].take() },
                    { out, _ ->
                        if (it+1 == params.size) {
                            result = out
                        }
                        ioLists[(it + 1).rem(params.size)].add(out)
                    }
                ).runIntCode(code)
            }
        }

        computer.forEach { it.start() }
        computer.last().join()

        println(result)
        results.add(result.toInt())
    }
}

/**
 * Heap's algorithm
 * @see https://en.wikipedia.org/wiki/Heap%27s_algorithm
 */
fun getPermutations(input: List<Int>): List<List<Int>> {
    val tempList = input.toMutableList()
    val stack = MutableList(input.size) { 0 }

    val permutationList = mutableListOf<List<Int>>()
    permutationList.add(input)

    var i = 0
    while (i < input.size) {
        if (stack[i] < i) {
            if (i.isEven()) {
                tempList.swap(0, i)
            } else {
                tempList.swap(stack[i], i)
            }
            permutationList.add(tempList.toList())

            stack[i] += 1
            i = 0
        } else {
            stack[i] = 0
            i++
        }
    }

    return permutationList
}

fun Int.isEven(): Boolean = this.and(0x01) == 0

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val value1 = this[index1]
    this[index1] = this[index2]
    this[index2] = value1
}
