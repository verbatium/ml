package ai.app

import ai.app.Vector.Companion.vectorOf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TWO

class VectorTest {
    @Test
    fun testSum() {
        val vectorA = vectorOf(ONE, TWO)
        val vectorB = vectorOf(d(4), d(8))
        assertEquals(vectorOf(d(5), d(10)), vectorA + vectorB)
    }

    @Test
    fun testSum_resultIsShortestVector() {
        val vectorA = vectorOf(ONE, ONE)
        val vectorB = vectorOf(ONE, ONE, TWO)
        assertEquals(vectorOf(TWO, TWO), vectorB + vectorA)
    }

    @Test
    fun testSubtract() {
        val vectorA = vectorOf(ONE, TWO)
        val vectorB = vectorOf(d(4), d(8))
        assertEquals(vectorOf(d(-3), d(-6)), vectorA - vectorB)
    }

    @Test
    fun testMultiply() {
        val vectorA = vectorOf(ONE, ONE)
        val vectorB = vectorOf(TWO, TWO)
        assertEquals(vectorB, vectorA * TWO)
    }

    @Test
    fun module() {
        assertEquals(d(6), vectorOf(d(2), d(4), d(4)).modulus())
    }

    @Test
    fun cos() {
        val expected = vectorOf(
            d("0.3333333333333333333333333333333333"),
            d("0.6666666666666666666666666666666667"),
            d("0.6666666666666666666666666666666667")
        )
        assertEquals(expected, vectorOf(d(2), d(4), d(4)).cos())
    }

    @Test
    fun isOrthogonal() {
        assertTrue(vectorOf(d(1), d(2), d(0)).isOrthogonal(vectorOf(d(2), d(-1), d(10))))
        assertTrue((vectorOf(d(2), d(3), d(1))).isOrthogonal(vectorOf(d(3), d(1), d(-9))))
        assertFalse((vectorOf(d(1), d(2), d(0))).isOrthogonal(vectorOf(d(1), d(2), d(0))))
    }

    @Test
    fun isCollinear() {
        //Два вектора a и b коллинеарны, если существует число n такое, что a = n · b
        val a = vectorOf(d(1), d(2), d(3))
        val b = vectorOf(d(4), d(8), d(12))
        val c = vectorOf(d(5), d(10), d(12))
        assertTrue(a.isCollinear(b), "a is Collinear to b")
        assertFalse(a.isCollinear(c), "a is not Collinear to c")
        assertFalse(b.isCollinear(c), "b is not Collinear to c")
        assertTrue(vectorOf(d(0), d(3)).isCollinear(vectorOf(d(0), d(6))))
        assertFalse(vectorOf(0, 1, 2).isCollinear(vectorOf(1, 0, 1)))
    }

    @Test
    fun isCoplanar_2x2AlwaysCoplanar() {
        assertTrue(Vector.isCoplanar(vectorOf(1, 2), vectorOf(2, 3)))

    }

    @Test
    fun isCoplanar_3x3CoplanarIfDeterminantIsZero() {
        assertTrue(Vector.isCoplanar(vectorOf(1, 1, 1), vectorOf(1, 3, 1), vectorOf(2, 2, 2)))
        assertFalse(Vector.isCoplanar(vectorOf(1, 2, 3), vectorOf(1, 1, 1), vectorOf(1, 2, 1)))

        assertEquals(d(0), vectorOf(1,1,1) * vectorOf(1,3,1).vectorMultiply(vectorOf(2,2,2)))
        assertEquals(d(2), vectorOf(1,2,3) * vectorOf(1,1,1).vectorMultiply(vectorOf(1,2,1)))
    }

    @Test
    fun isCoplanar_3x4CoplanarIfRankIsLessThan3() {
        //Вектора компланарны если среди них не более двух линейно независимых векторов.
        assertTrue(
            Vector.isCoplanar(
                vectorOf(1, 1, 1),
                vectorOf(1, 2, 0),
                vectorOf(0, -1, 1),
                vectorOf(3, 3, 3),
            )
        )
    }

    @Test
    fun cosA() {
        assertEquals(d(0.96), vectorOf(3, 4).cosA(vectorOf(4, 3)))
        assertEquals(d(0.8), vectorOf(7, 1).cosA(vectorOf(5, 5)))
    }

    @Test
    fun projection() {
        assertEquals(d(2.2), vectorOf(1, 2).projectionOn(vectorOf(3, 4)))
    }

    @Test
    fun vectorMultiply() {
        assertEquals(vectorOf(-7, 8, -3), vectorOf(1, 2, 3).vectorMultiply(vectorOf(2, 1, -2)))
        assertEquals(vectorOf(0, -5, -5), vectorOf(-1, 2, -2).vectorMultiply(vectorOf(2, 1, -1)))
    }
}

fun d(value: Int): BigDecimal {
    return value.toBigDecimal()
}

fun d(value: String): BigDecimal {
    return value.toBigDecimal()
}

fun d(value: Double): BigDecimal {
    return value.toBigDecimal()
}