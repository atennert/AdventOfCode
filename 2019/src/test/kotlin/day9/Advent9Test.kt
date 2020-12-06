package day9

import day2.IntCodeComputer
import kotlin.test.Test
import kotlin.test.assertEquals

class Advent9Test {
    @Test
    fun check1() {
        val input = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99".split(',')

        val output = mutableListOf<String>()

        IntCodeComputer(
            {""},
            {out, _ -> output.add(out)}
        ).runIntCode(input)

        assertEquals(input, output)
    }

    @Test
    fun check2() {
        val input = "1102,34915192,34915192,7,4,7,99,0".split(',')

        val output = mutableListOf<String>()

        IntCodeComputer(
            {""},
            {out, _ -> output.add(out)}
        ).runIntCode(input)

        println(output)
    }

    @Test
    fun check3() {
        val input = "104,1125899906842624,99".split(',')

        val output = mutableListOf<String>()

        IntCodeComputer(
            {""},
            {out, _ -> output.add(out)}
        ).runIntCode(input)

        assertEquals(input.subList(1,2), output)
    }
}