package day7

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Advent7Test {
    @Test
    fun checkPermutations2() {
        val permutations = getPermutations(listOf(0, 1))

        assertEquals(permutations.size, 2)

        assertTrue(permutations.contains(listOf(0, 1)))
        assertTrue(permutations.contains(listOf(1, 0)))
    }

    @Test
    fun checkPermutations3() {
        val permutations = getPermutations(listOf(0, 1, 2))

        assertEquals(permutations.size, 6)

        assertTrue(permutations.contains(listOf(0, 1, 2)))
        assertTrue(permutations.contains(listOf(0, 2, 1)))
        assertTrue(permutations.contains(listOf(1, 0, 2)))
        assertTrue(permutations.contains(listOf(1, 2, 0)))
        assertTrue(permutations.contains(listOf(2, 0, 1)))
        assertTrue(permutations.contains(listOf(2, 1, 0)))
    }
}
