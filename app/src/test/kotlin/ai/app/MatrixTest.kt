package ai.app

import ai.app.Matrix.Companion.matrixOf
import ai.app.Vector.Companion.vectorOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.MathContext
import java.math.RoundingMode

class MatrixTest {
    @Test
    fun determinant_1x1Matrix() {
        val determinant = matrixOf(vectorOf(1)).determinant()
        assertEquals(d(1), determinant)
    }

    @Test
    fun determinant_2x2Matrix() {
        val determinant = matrixOf(vectorOf(1, 2), vectorOf(2, 3)).determinant()
        assertEquals(d(1 * 3 - 2 * 2), determinant)
    }

    @Test
    fun determinant_3x3Matrix() {
        val determinant = matrixOf(vectorOf(1, 2, 3), vectorOf(1, 1, 1), vectorOf(1, 2, 1)).determinant()
        assertEquals(d(2), determinant)
    }

    @Test
    fun determinant_notSquareMatrix() {
        assertThrows<IllegalArgumentException> { matrixOf(vectorOf(1, 2, 3), vectorOf(1, 1, 1)).determinant() }
        assertThrows<IllegalArgumentException> { matrixOf(vectorOf(1, 2), vectorOf(1, 1), vectorOf(2, 2)).determinant() }
        assertThrows<IllegalArgumentException> { matrixOf(vectorOf(1, 2), vectorOf(1)).determinant() }
    }

    @Test
    fun rank() {
        assertEquals(1, matrixOf(vectorOf(1)).rank())
        assertEquals(2, matrixOf(vectorOf(1, 2), vectorOf(3, 4)).rank())
        assertEquals(
            2, matrixOf(
                vectorOf(1, 2, 3),
                vectorOf(4, 5, 6),
                vectorOf(7, 8, 9)
            ).rank()
        )
        assertEquals(
            3, matrixOf(
                vectorOf(-1, -2, 3),
                vectorOf(4, 5, 6),
                vectorOf(7, 8, 9)
            ).rank()
        )
        assertEquals(
            3, matrixOf(
                vectorOf(1, 2, 3, 4),
                vectorOf(5, 6, 7, 8),
                vectorOf(9, 0, 1, 2),
                vectorOf(3, 4, 5, 6)
            ).rank()
        )

        assertEquals(
            2, matrixOf(
                vectorOf(1, 1, 1),
                vectorOf(1, 2, 0),
                vectorOf(0, -1, 1),
                vectorOf(3, 3, 3)
            ).rank()
        )
    }

    @Test
    fun rank_zeroMatrix() {
        assertEquals(0, matrixOf(vectorOf(0)).rank())
        assertEquals(0, matrixOf(vectorOf(0), vectorOf(0)).rank())
        assertEquals(0, matrixOf(vectorOf(0, 0)).rank())
        assertEquals(0, matrixOf(vectorOf(0, 0), vectorOf(0, 0)).rank())
    }

    @Test
    fun cofactor_excludeLastColumn() {
        val matrix = matrixOf(
            vectorOf(1, 2, 3),
            vectorOf(3, 4, 3)
        )
        assertEquals(matrixOf(vectorOf(1)), matrix.cofactor(listOf(1) to listOf(1, 2)))
        assertEquals(matrixOf(vectorOf(2)), matrix.cofactor(listOf(1) to listOf(0, 2)))
        assertEquals(matrixOf(vectorOf(3)), matrix.cofactor(listOf(0) to listOf(1, 2)))
        assertEquals(matrixOf(vectorOf(4)), matrix.cofactor(listOf(0) to listOf(0, 2)))
    }

    @Test
    fun listOfCofactors() {
        val matrix = matrixOf(
            vectorOf(1, 2, 3),
            vectorOf(4, 5, 6),
            vectorOf(7, 8, 9)
        )
        assertEquals(
            listOf(
                matrixOf(
                    vectorOf(5, 6),
                    vectorOf(8, 9)
                ),
                matrixOf(
                    vectorOf(4, 6),
                    vectorOf(7, 9)
                ),
                matrixOf(
                    vectorOf(4, 5),
                    vectorOf(7, 8)
                ),
                matrixOf(
                    vectorOf(2, 3),
                    vectorOf(8, 9)
                ),
                matrixOf(
                    vectorOf(1, 3),
                    vectorOf(7, 9)
                ),
                matrixOf(
                    vectorOf(1, 2),
                    vectorOf(7, 8)
                ),
                matrixOf(
                    vectorOf(2, 3),
                    vectorOf(5, 6)
                ),
                matrixOf(
                    vectorOf(1, 3),
                    vectorOf(4, 6)
                ),
                matrixOf(
                    vectorOf(1, 2),
                    vectorOf(4, 5)
                ),
            ), matrix.cofactors(2 to 2)
        )
    }

    @Test
    fun transpose() {
        val matrix = matrixOf(
            vectorOf(1, 2),
            vectorOf(3, 4)
        )
        val result = matrix.transpose()
        val expected = matrixOf(
            vectorOf(1, 3),
            vectorOf(2, 4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun multiplyByScalar() {
        val matrix = matrixOf(
            vectorOf(1, 2),
            vectorOf(3, 4)
        )
        val result = matrix * d(2)
        val expected = matrixOf(
            vectorOf(2, 4),
            vectorOf(6, 8)
        )
        assertEquals(expected, result)
    }

    @Test
    fun add() {
        val a = matrixOf(
            vectorOf(4, 2),
            vectorOf(9, 0)
        )
        val b = matrixOf(
            vectorOf(3, 1),
            vectorOf(-3, 4)
        )
        val result = a + b
        val expected = matrixOf(
            vectorOf(7, 3),
            vectorOf(6, 4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun minus() {
        val a = matrixOf(
            vectorOf(4, 2),
            vectorOf(9, 0)
        )
        val b = matrixOf(
            vectorOf(3, 1),
            vectorOf(-3, 4)
        )
        val result = a - b
        val expected = matrixOf(
            vectorOf(1, 1),
            vectorOf(12, -4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun multiplyMatrix() {
        val a = matrixOf(
            vectorOf(4, 2),
            vectorOf(9, 0)
        )
        val b = matrixOf(
            vectorOf(3, 1),
            vectorOf(-3, 4)
        )
        val result = a * b
        val expected = matrixOf(
            vectorOf(6, 12),
            vectorOf(27, 9)
        )
        assertEquals(expected, result)
    }

    @Test
    fun minor() {
        val matrix = matrixOf(
            vectorOf(1, 2, 4),
            vectorOf(2, 3, 5),
            vectorOf(6, 7, 8),
        )
        assertEquals(d(-1), matrix.minor(2, 2))
    }

    @Test
    fun adjugate() {
        val matrix = matrixOf(
            vectorOf(2, 4, 1),
            vectorOf(0, 2, 1),
            vectorOf(2, 1, 1),
        )
        val expected = matrixOf(
            vectorOf(1, 2, -4),
            vectorOf(-3, 0, 6),
            vectorOf(2, -2, 4),
        )
        val result = matrix.adjugate()
        assertEquals(expected, result)
    }

    @Test
    fun invertibleMatrix() {
        val mathContext = MathContext(2, RoundingMode.HALF_UP)
        val matrix = matrixOf(
            vectorOf(2, 4, 1),
            vectorOf(0, 2, 1),
            vectorOf(2, 1, 1),
        )
        val expected = matrixOf(
            vectorOf(d(1).divide(d(6), mathContext), d(-0.5), d(1.0).divide(d(3), mathContext)),
            vectorOf(d(1.0).divide(d(3), mathContext), d(0), d(-1.0).divide(d(3), mathContext)),
            vectorOf(d(-2.0).divide(d(3), mathContext), d(1), d(2.0).divide(d(3), mathContext)),
        )
        val result = matrix.inverse(mathContext)
        assertEquals(expected, result)
    }

    @Test
    fun cofactor() {
        val matrix = matrixOf(
            vectorOf(1, 2, 4),
            vectorOf(2, 3, 5),
            vectorOf(6, 7, 8),
        )
        assertEquals(matrixOf(vectorOf(1, 2), vectorOf(2, 3)), matrix.cofactor(2, 2))
    }

    @Test
    fun diagonalMatrix_check() {
        assertTrue(matrixOf(vectorOf(1, 0), vectorOf(0, 1)).isDiagonal())
    }

    @Test
    fun diagonalMatrix() {
        assertEquals(matrixOf(vectorOf(1, 0), vectorOf(0, 2)), Matrix.diagonal(vectorOf(1, 2)))
    }

    @Test
    fun identity() {
        assertEquals(matrixOf(vectorOf(1, 0), vectorOf(0, 1)), Matrix.identity(2))
    }

    @Test
    fun scalar() {
        assertEquals(matrixOf(vectorOf(3, 0), vectorOf(0, 3)), Matrix.scalar(3, 2))
        assertEquals(matrixOf(vectorOf(2, 0), vectorOf(0, 2)), Matrix.scalar(d(2), 2))
    }
}