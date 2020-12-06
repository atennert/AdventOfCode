package day1

import kotlin.test.Test
import kotlin.test.assertEquals

class `Advent1-2Test` {
    // 14 => 2
    // 1969 -> 654 -> 216 -> 70 -> 21 -> 5 => 966
    // 100756 -> 33583 -> 11192 -> 3728 -> 1240 -> 411 -> 135 -> 43 -> 12 -> 2 = 50346
    // (x / 3)v - 2
    @Test
    fun `check fuel simple`() {
        val accumulatedFuel = getAccumulatedFuels(14).sum()
        assertEquals(2, accumulatedFuel)
    }
    @Test
    fun `check fuel complex 1`() {
        val accumulatedFuel = getAccumulatedFuels(1969).sum()
        assertEquals(966, accumulatedFuel)
    }
    @Test
    fun `check fuel complex 2`() {
        val accumulatedFuel = getAccumulatedFuels(100756).sum()
        assertEquals(50346, accumulatedFuel)
    }
}