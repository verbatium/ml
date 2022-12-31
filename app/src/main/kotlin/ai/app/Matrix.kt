package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class Matrix(val vectors: List<Vector>) {
    companion object {
        fun matrixOf(vararg args: Vector): Matrix = Matrix(args.asList())
    }

    override fun toString(): String {
        return vectors.joinToString(",", "[", "]")
    }

    fun size(): Pair<Int, Int> {
        return vectors.size to vectors.map { it.args.size }.toSortedSet().first()
    }
}

fun Matrix.determinant(): BigDecimal {
    if (vectors.map { it.args.size }.toSet().firstOrNull()?.let { it != vectors.size } == true)
        throw IllegalArgumentException("Determinant can be calculated only for square matrix")
    if (vectors.size == 1) return vectors.first().args.first()
    return vectors[0].args.foldIndexed(ZERO) { index, total, item ->
        Matrix(vectors.slice(1 until vectors.size).map { Vector(it.args.filterIndexed { i, _ -> i != index }) })
            .determinant() * item * (index % 2).compareTo(0.5).toBigDecimal().negate() + total
    }
}