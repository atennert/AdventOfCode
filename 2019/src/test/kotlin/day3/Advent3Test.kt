package day3

import kotlin.test.Test
import kotlin.test.assertEquals

class Advent3Test {
    @Test
    fun `find intersection 1-1`() {
        val commands1 = listOf("R8","U5","L5","D3")
        val commands2 = listOf("U7","R6","D4","L4")

        val advent3 = Advent3(listOf(commands1, commands2))
        val distance = advent3.getDistanceToClosestIntersection()
        assertEquals(6, distance)
    }

    @Test
    fun `find intersection 1-2`() {
        val commands1 = listOf("R75","D30","R83","U83","L12","D49","R71","U7","L72")
        val commands2 = listOf("U62","R66","U55","R34","D71","R55","D58","R83")

        val advent3 = Advent3(listOf(commands1, commands2))

        val distance = advent3.getDistanceToClosestIntersection()
        assertEquals(159, distance)
    }

    @Test
    fun `find intersection 1-3`() {
        val commands1 = listOf("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51")
        val commands2 = listOf("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7")

        val advent3 = Advent3(listOf(commands1, commands2))
        val distance = advent3.getDistanceToClosestIntersection()
        assertEquals(135, distance)
    }

    // Part 2
    @Test
    fun `find intersection 2-1`() {
        val commands1 = listOf("R8","U5","L5","D3")
        val commands2 = listOf("U7","R6","D4","L4")

        val advent3 = Advent3(listOf(commands1, commands2))
        val totalSteps = advent3.getTotalStepsToClosestIntersection()
        assertEquals(30, totalSteps)
    }

    @Test
    fun `find intersection 2-2`() {
        val commands1 = listOf("R75","D30","R83","U83","L12","D49","R71","U7","L72")
        val commands2 = listOf("U62","R66","U55","R34","D71","R55","D58","R83")

        val advent3 = Advent3(listOf(commands1, commands2))

        val totalSteps = advent3.getTotalStepsToClosestIntersection()
        assertEquals(610, totalSteps)
    }

    @Test
    fun `find intersection 2-3`() {
        val commands1 = listOf("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51")
        val commands2 = listOf("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7")

        val advent3 = Advent3(listOf(commands1, commands2))
        val totalSteps = advent3.getTotalStepsToClosestIntersection()
        assertEquals(410, totalSteps)
    }
}