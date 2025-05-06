package io.github.u1tramarinet.easysettings.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class IntExtTest {
    @Test
    fun fromValueToPercent() {
        val value1 = 255
        val expected1 = 100
        val result1 = value1.fromValueToPercent(range)
        assertEquals(result1, expected1)

        val value2 = 1
        val expected2 = 0
        val result2 = value2.fromValueToPercent(range)
        assertEquals(result2, expected2)

        val value3 = 128
        val expected3 = 50
        val result3 = value3.fromValueToPercent(range)
        assertEquals(result3, expected3)
    }

    @Test
    fun fromPercentToValue() {
        val percent1 = 100
        val expected1 = 255
        val result1 = percent1.fromPercentToValue(range)
        assertEquals(result1, expected1)

        val percent2 = 0
        val expected2 = 1
        val result2 = percent2.fromPercentToValue(range)
        assertEquals(result2, expected2)

        val percent3 = 50
        val expected3 = 128
        val result3 = percent3.fromPercentToValue(range)
        assertEquals(result3, expected3)
    }

    companion object {
        private val range = 1..255
    }
}