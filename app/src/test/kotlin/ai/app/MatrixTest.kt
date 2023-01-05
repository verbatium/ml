package ai.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.MathContext
import java.math.RoundingMode
import ai.app.Matrix.Companion.matrixOf as m
import ai.app.Vector.Companion.vectorOf as v

class MatrixTest {
    @Test
    fun determinant_1x1Matrix() = assertEquals(d(1), m(v(1)).determinant())

    @Test
    fun determinant_2x2Matrix() = assertEquals(d(1 * 3 - 2 * 2), m(v(1, 2), v(2, 3)).determinant())

    @Test
    fun determinant_3x3Matrix() = assertEquals(d(2), m(v(1, 2, 3), v(1, 1, 1), v(1, 2, 1)).determinant())

    @Test
    fun determinant_notSquareMatrix() {
        assertThrows<IllegalArgumentException> { m(v(1, 2, 3), v(1, 1, 1)).determinant() }
        assertThrows<IllegalArgumentException> { m(v(1, 2), v(1, 1), v(2, 2)).determinant() }
        assertThrows<IllegalArgumentException> { m(v(1, 2), v(1)).determinant() }
    }

    @Test
    fun rank() {
        assertEquals(1, m(v(1)).rank())
        assertEquals(2, m(v(1, 2), v(3, 4)).rank())
        assertEquals(
            2, m(
                v(1, 2, 3),
                v(4, 5, 6),
                v(7, 8, 9)
            ).rank()
        )
        assertEquals(
            3, m(
                v(-1, -2, 3),
                v(4, 5, 6),
                v(7, 8, 9)
            ).rank()
        )
        assertEquals(
            3, m(
                v(1, 2, 3, 4),
                v(5, 6, 7, 8),
                v(9, 0, 1, 2),
                v(3, 4, 5, 6)
            ).rank()
        )

        assertEquals(
            2, m(
                v(1, 1, 1),
                v(1, 2, 0),
                v(0, -1, 1),
                v(3, 3, 3)
            ).rank()
        )
    }

    @Test
    fun rank_zeroMatrix() {
        assertEquals(0, m(v(0)).rank())
        assertEquals(0, m(v(0), v(0)).rank())
        assertEquals(0, m(v(0, 0)).rank())
        assertEquals(0, m(v(0, 0), v(0, 0)).rank())
    }

    @Test
    fun cofactor_excludeLastColumn() {
        val matrix = m(
            v(1, 2, 3),
            v(3, 4, 3)
        )
        assertEquals(m(v(1)), matrix.cofactor(listOf(1) to listOf(1, 2)))
        assertEquals(m(v(2)), matrix.cofactor(listOf(1) to listOf(0, 2)))
        assertEquals(m(v(3)), matrix.cofactor(listOf(0) to listOf(1, 2)))
        assertEquals(m(v(4)), matrix.cofactor(listOf(0) to listOf(0, 2)))
    }

    @Test
    fun listOfCofactors() {
        val matrix = m(
            v(1, 2, 3),
            v(4, 5, 6),
            v(7, 8, 9)
        )
        assertEquals(
            listOf(
                m(
                    v(5, 6),
                    v(8, 9)
                ),
                m(
                    v(4, 6),
                    v(7, 9)
                ),
                m(
                    v(4, 5),
                    v(7, 8)
                ),
                m(
                    v(2, 3),
                    v(8, 9)
                ),
                m(
                    v(1, 3),
                    v(7, 9)
                ),
                m(
                    v(1, 2),
                    v(7, 8)
                ),
                m(
                    v(2, 3),
                    v(5, 6)
                ),
                m(
                    v(1, 3),
                    v(4, 6)
                ),
                m(
                    v(1, 2),
                    v(4, 5)
                ),
            ), matrix.cofactors(2 to 2)
        )
    }

    @Test
    fun transpose_square() {
        val matrix = m(
            v(1, 2),
            v(3, 4)
        )
        val result = matrix.transpose()
        val expected = m(
            v(1, 3),
            v(2, 4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun transpose_horizontal() = assertEquals(m(v(1), v(2), v(3)), m(v(1, 2, 3)).transpose())

    @Test
    fun columns() {
        assertEquals(listOf(v(1, 3), v(2, 4)), m(v(1, 2), v(3, 4)).columns())
        assertEquals(listOf(v(1), v(2), v(3)), m(v(1, 2, 3)).columns())
    }

    @Test
    fun multiplyByScalar() {
        val matrix = m(
            v(1, 2),
            v(3, 4)
        )
        val result = matrix * d(2)
        val expected = m(
            v(2, 4),
            v(6, 8)
        )
        assertEquals(expected, result)
    }

    @Test
    fun add() {
        val a = m(
            v(4, 2),
            v(9, 0)
        )
        val b = m(
            v(3, 1),
            v(-3, 4)
        )
        val result = a + b
        val expected = m(
            v(7, 3),
            v(6, 4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun minus() {
        val a = m(
            v(4, 2),
            v(9, 0)
        )
        val b = m(
            v(3, 1),
            v(-3, 4)
        )
        val result = a - b
        val expected = m(
            v(1, 1),
            v(12, -4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun multiplyMatrix() {
        val a = m(
            v(4, 2),
            v(9, 0)
        )
        val b = m(
            v(3, 1),
            v(-3, 4)
        )
        val result = a * b
        val expected = m(
            v(6, 27),
            v(12, 9)
        )
        assertEquals(expected, result)
    }

    @Test
    fun multiplyRowByColumn() {
        val expected2 = Matrix.matrixOf(Vector.vectorOf(4, 6), Vector.vectorOf(6, 9))
        val a = Matrix.matrixOf(Vector.vectorOf(2), Vector.vectorOf(3))
        val b = Matrix.matrixOf(Vector.vectorOf(2, 3))
        assertEquals(expected2, a * b)
    }

    @Test
    fun multiply1x3x3x1() {
        val m1 = Matrix.matrixOf(Vector.vectorOf(1, 40, 210))
        val m2 = Matrix.matrixOf(Vector.vectorOf(1), Vector.vectorOf(40), Vector.vectorOf(210))
        assertEquals(Matrix.scalar(d(45701), 1), m1 * m2)
    }

    @Test
    fun multiply3x1x1x3() {
        val expected2 = Matrix.matrixOf(
            Vector.vectorOf(1, 1000, 1000),
            Vector.vectorOf(1000, 1000000, 1000000),
            Vector.vectorOf(1000, 1000000, 1000000)
        )
        val a = Matrix.matrixOf(Vector.vectorOf(1), Vector.vectorOf(1000), Vector.vectorOf(1000))
        val b = Matrix.matrixOf(Vector.vectorOf(1, 1000, 1000))
        assertEquals(expected2, a * b)
    }

    @Test
    fun minor() {
        val matrix = m(
            v(1, 2, 4),
            v(2, 3, 5),
            v(6, 7, 8),
        )
        assertEquals(d(-1), matrix.minor(2, 2))
    }

    @Test
    fun adjugate() {
        val matrix = m(
            v(2, 4, 1),
            v(0, 2, 1),
            v(2, 1, 1),
        )
        val expected = m(
            v(1, 2, -4),
            v(-3, 0, 6),
            v(2, -2, 4),
        )
        val result = matrix.adjugate()
        assertEquals(expected, result)
    }

    @Test
    fun invertibleMatrix() {
        val mathContext = MathContext(2, RoundingMode.HALF_UP)
        val matrix = m(
            v(2, 4, 1),
            v(0, 2, 1),
            v(2, 1, 1),
        )
        val expected = m(
            v(d(1).divide(d(6), mathContext), d(-0.5), d(1.0).divide(d(3), mathContext)),
            v(d(1.0).divide(d(3), mathContext), d(0), d(-1.0).divide(d(3), mathContext)),
            v(d(-2.0).divide(d(3), mathContext), d(1), d(2.0).divide(d(3), mathContext)),
        )
        val result = matrix.inverse(mathContext)
        assertEquals(expected, result)
    }

    @Test
    fun cofactor() {
        val matrix = m(
            v(1, 2, 4),
            v(2, 3, 5),
            v(6, 7, 8),
        )
        assertEquals(m(v(1, 2), v(2, 3)), matrix.cofactor(2, 2))
    }

    @Test
    fun diagonalMatrix_check() = assertTrue(m(v(1, 0), v(0, 1)).isDiagonal())

    @Test
    fun diagonalMatrix() = assertEquals(m(v(1, 0), v(0, 2)), Matrix.diagonal(v(1, 2)))

    @Test
    fun identity() = assertEquals(m(v(1, 0), v(0, 1)), Matrix.identity(2))

    @Test
    fun scalar() {
        assertEquals(m(v(3, 0), v(0, 3)), Matrix.scalar(3, 2))
        assertEquals(m(v(2, 0), v(0, 2)), Matrix.scalar(d(2), 2))
    }

    @Test
    fun vectorRow() = assertEquals(m(v(1, 2, 3)), Matrix.vectorRow(v(1, 2, 3)))

    @Test
    fun vectorColumn() = assertEquals(m(v(1), v(2), v(3)), Matrix.vectorColumn(v(1, 2, 3)))
}