package ai.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ManhattanTest {
    @Test
    fun distance() {
        assertEquals(BigDecimal.valueOf(7), Manhattan().distance(Vector.vectorOf(1, 2) to Vector.vectorOf(4, 6)))
        assertEquals(BigDecimal.valueOf(7), Manhattan().distance(Vector.vectorOf(4, 6) to Vector.vectorOf(1, 2)))
    }
}