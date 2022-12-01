package day1

import day1.getRequiredFuelForModule
import day1.getRequiredFuelForModules
import org.junit.Test
import kotlin.test.assertEquals

class `Advent1-1Test` {
    @Test
    fun `check example 1`() {
        val fuelAmount = getRequiredFuelForModule(12)
        assertEquals(2, fuelAmount)
    }

    @Test
    fun `check example 2`() {
        val fuelAmount = getRequiredFuelForModule(14)
        assertEquals(2, fuelAmount)
    }

    @Test
    fun `check example 3`() {
        val fuelAmount = getRequiredFuelForModule(1969)
        assertEquals(654, fuelAmount)
    }

    @Test
    fun `check fuel sum`() {
        val fuelAmount = getRequiredFuelForModules(listOf(12, 14, 1969))
        assertEquals(658, fuelAmount)
    }
}