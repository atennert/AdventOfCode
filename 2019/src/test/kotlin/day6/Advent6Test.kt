package day6

import kotlin.test.Test
import kotlin.test.assertEquals

class Advent6Test {
    @Test
    fun `check map validation`() {
        val testMap = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L")

        val mapHandler = MapHandler(testMap)

        assertEquals(42, mapHandler.getNumberOfOrbits())
    }

    @Test
    fun simple1() {
        val testMap = listOf("COM)A")

        val mapHandler = MapHandler(testMap)

        assertEquals(1, mapHandler.getNumberOfOrbits())
    }

    @Test
    fun simple2() {
        val testMap = listOf("COM)A", "COM)B")

        val mapHandler = MapHandler(testMap)

        assertEquals(2, mapHandler.getNumberOfOrbits())
    }

    @Test
    fun simple3() {
        val testMap = listOf("COM)A", "A)B")

        val mapHandler = MapHandler(testMap)

        assertEquals(3, mapHandler.getNumberOfOrbits())
    }

    @Test
    fun simple4() {
        val testMap = listOf("COM)A", "A)B", "A)C")

        val mapHandler = MapHandler(testMap)

        assertEquals(5, mapHandler.getNumberOfOrbits())
    }

    @Test
    fun jumpTest() {
        val testMap = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN")

        val mapHandler = MapHandler(testMap)

        assertEquals(4, mapHandler.getJumpDistance("YOU", "SAN"))
    }
}