package day2

import kotlin.test.Test
import kotlin.test.assertEquals

class Advent2Test {
    @Test
    fun `check add`() {
        val input = listOf("1", "0", "0", "0", "99")
        val testOutput = listOf("2", "0", "0", "0", "99")

        runCheck(input, testOutput)
    }

    @Test
    fun `check multiply`() {
        val input = listOf("2", "3", "0", "3", "99")
        val testOutput = listOf("2", "3", "0", "6", "99")

        runCheck(input, testOutput)
    }

    @Test
    fun `check multiply 2`() {
        val input = listOf("2", "4", "4", "5", "99", "0")
        val testOutput = listOf("2", "4", "4", "5", "99", "9801")

        runCheck(input, testOutput)
    }

    @Test
    fun `check complex`() {
        val input = listOf("1", "1", "1", "4", "99", "5", "6", "0", "99")
        val testOutput = listOf("30", "1", "1", "4", "2", "5", "6", "0", "99")

        runCheck(input, testOutput)
    }

    private fun runCheck(input: List<String>, referenceOutput: List<String>) {
        val runOutput = IntCodeComputer({""}, {_,_->}).runIntCode(input)

        assertEquals(referenceOutput.size, runOutput.size)

        referenceOutput.zip(runOutput)
            .forEachIndexed { i, (refVal, realVal) -> assertEquals(refVal, realVal, "Issue as pos $i") }
    }
}