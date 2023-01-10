package ai.app

import ai.app.Vector.Companion.vectorOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal.valueOf as d

class HammingTest {

    @Test
    fun distance() {
        assertEquals(d(2), Hamming().distance(vectorOf(1,2,3,4,5) to vectorOf(1,2,4,3,5)))
    }
}