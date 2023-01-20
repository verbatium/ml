package ai.utilities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal.TEN
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf as d

class AxisUtilsTest {
    @Test
    fun split() {
        assertEquals(listOf(0L, 20, 40, 60, 80, 100).map(Long::toBigDecimal), AxisUtils.split(ZERO, d(100), 5))
        assertEquals(listOf(0L, 50, 100).map(Long::toBigDecimal), AxisUtils.split(d(0), d(100), 2))
        assertEquals(listOf(10L, 12, 14, 16, 18, 20).map(Long::toBigDecimal), AxisUtils.split(TEN, d(20), 4))
        assertEquals(listOf(0L, 20, 40, 60, 80, 100).map(Long::toBigDecimal), AxisUtils.split(TEN, d(90), 5))
        assertEquals(listOf(100L, 120, 140, 160, 180, 200).map(Long::toBigDecimal), AxisUtils.split(d(114), d(191), 5))
        assertEquals(listOf(0.00, 0.2, 0.40, 0.60, 0.80, 1.00).map(Double::toBigDecimal), AxisUtils.split(d(0.14), d(0.91), 5))
        assertEquals(listOf(1.00, 1.2, 1.40, 1.60, 1.80, 2.00).map(Double::toBigDecimal), AxisUtils.split(d(1.14), d(1.91), 5))
    }
}