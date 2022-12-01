package day5

import day2.IntCodeComputer
import kotlin.test.Test
import kotlin.test.assertEquals

class Advent5Test {
    @Test
    fun `check 1`() {
        val input = listOf("1002","4","3","4","33")

        val output = IntCodeComputer({""}, {_,_->}).runIntCode(input)

        assertEquals("99", output.last())
    }

    @Test
    fun `check 2`() {
        val input = listOf("1101","100","-1","4","0")

        val output = IntCodeComputer({""}, {_,_->}).runIntCode(input)

        assertEquals("99", output.last())
    }
}
