package ai.app

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.MathContext.DECIMAL128

class MinkowskiTest {
    @Test
    fun distance_euclidean() {
        assertEquals(BigDecimal.valueOf(5), Minkowski(2, DECIMAL128).distance(Vector.vectorOf(1, 2) to Vector.vectorOf(4, 6)).stripTrailingZeros())
        assertEquals(BigDecimal.valueOf(5), Minkowski(2, DECIMAL128).distance(Vector.vectorOf(4, 6) to Vector.vectorOf(1, 2)).stripTrailingZeros())
    }

    @Test
    fun distance_manhattan() {
        assertEquals(BigDecimal.valueOf(7), Minkowski(1, DECIMAL128).distance(Vector.vectorOf(1, 2) to Vector.vectorOf(4, 6)).stripTrailingZeros())
        assertEquals(BigDecimal.valueOf(7), Minkowski(1, DECIMAL128).distance(Vector.vectorOf(4, 6) to Vector.vectorOf(1, 2)).stripTrailingZeros())
    }
}