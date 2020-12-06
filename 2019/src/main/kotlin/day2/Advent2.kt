package day2

fun main() {
    val initialCode = loadCode("2/input.txt")
    val computer = IntCodeComputer({""}, {_, _ -> })

//    val result = runIntCode(initialCode)
//    println(result)


    outer@
    for (i in 0..99) {
        for (j in 0..99) {
            val adaptedCode = initialCode.toMutableList()
            adaptedCode[1] = i.toString()
            adaptedCode[2] = j.toString()
            val result = computer.runIntCode(adaptedCode)
            if (result[0] == "19690720") {
                println(result)
                println(100 * i + j)
                break@outer
            }
        }
    }
}
