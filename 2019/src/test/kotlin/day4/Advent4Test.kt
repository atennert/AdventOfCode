package day4

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Advent4Test {
    @Test
    fun check1() {
        assertTrue(isAMatch(112233))
    }

    @Test
    fun check2() {
        assertFalse(isAMatch(123444))
    }

    @Test
    fun check3() {
        assertTrue(isAMatch(111122))
    }

    @Test
    fun check4() {
        assertFalse(isAMatch(223450))
    }

    @Test
    fun check5() {
        assertFalse(isAMatch(123789))
    }
}
