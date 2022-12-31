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
}

