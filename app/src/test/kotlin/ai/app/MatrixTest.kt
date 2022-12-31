package ai.app

import ai.app.Matrix.Companion.matrixOf
import ai.app.Vector.Companion.vectorOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    fun minor() {
        val matrix = matrixOf(
            vectorOf(1, 2),
            vectorOf(3, 4)
        )
        assertEquals(matrixOf(vectorOf(1)), matrix.minor(listOf(1) to listOf(1)))
        assertEquals(matrixOf(vectorOf(2)), matrix.minor(listOf(1) to listOf(0)))
        assertEquals(matrixOf(vectorOf(3)), matrix.minor(listOf(0) to listOf(1)))
        assertEquals(matrixOf(vectorOf(4)), matrix.minor(listOf(0) to listOf(0)))
    }

    @Test
    fun minors() {
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
            ), matrix.minors(2 to 2)
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
}
