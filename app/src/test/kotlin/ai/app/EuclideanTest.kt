package ai.app

import ai.app.Vector.Companion.vectorOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.MathContext
import java.math.BigDecimal.valueOf as d

class EuclideanTest {

    @Test
    fun distance() {
        assertEquals(d(5), Euclidean(MathContext.DECIMAL128).distance(vectorOf(1, 2) to vectorOf(4, 6)))
        assertEquals(d(5), Euclidean(MathContext.DECIMAL128).distance(vectorOf(4, 6) to vectorOf(1, 2)))
    }
}