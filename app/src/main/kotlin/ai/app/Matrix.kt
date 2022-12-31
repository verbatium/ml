package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ZERO

data class Matrix(val vectors: List<Vector>) {
    companion object {
        fun matrixOf(vararg args: Vector): Matrix {
            val a = args.asList()
            if (a.map { it.args.size }.toSet().size != 1) throw IllegalArgumentException("All vectors of matrix must have same dimension")
            return Matrix(a)
        }
    }

    override fun toString(): String = vectors.joinToString(",", "[", "]")
}

fun Matrix.columns() = IntRange(0, rows() - 1).map { index -> Vector(vectors.map { v -> v.args[index] }) }
fun Matrix.transpose(): Matrix = Matrix(columns())

fun Matrix.rows(): Int = vectors.size
fun Matrix.cols(): Int = vectors.first().args.size

fun Matrix.size(): Pair<Int, Int> = rows() to cols()
fun Matrix.isSquare(): Boolean = rows() == cols()
fun Matrix.determinant(): BigDecimal {
    if (!this.isSquare()) throw IllegalArgumentException("Determinant can be calculated only for square matrix")
    if (vectors.size == 1) return vectors.first().args.first()
    return vectors[0].args.foldIndexed(ZERO) { index, total, item ->
        Matrix(vectors.slice(1 until vectors.size).map { Vector(it.args.filterIndexed { i, _ -> i != index }) })
            .determinant() * item * cellSign(index) + total
    }
}

fun Matrix.rank(): Int {
    val maxRank = vectors.size.coerceAtMost(vectors.first().args.size)
    return IntRange(0, maxRank - 1).toList().stream()
        .filter { !minors(it + 1 to it + 1).stream().anyMatch { m -> !m.determinant().isZero() } }
        .findFirst()
        .orElse(maxRank)
}

fun Matrix.minors(size: Pair<Int, Int>): List<Matrix> {
    return IntRange(0, rows() - 1).toList().permutations(rows() - size.first)
        .flatMap { row ->
            IntRange(0, cols() - 1).toList().permutations(cols() - size.second).map { col -> row to col }
        }
        .map(::minor)
}

fun Matrix.minor(exclusions: Pair<List<Int>, List<Int>>): Matrix {
    return Matrix(vectors.filterIndexed { index, _ -> !exclusions.first.contains(index) }
        .map { Vector(it.args.filterIndexed { index, _ -> !exclusions.second.contains(index) }) })
}


operator fun Matrix.times(d: BigDecimal): Matrix = Matrix(vectors.map { it * d })
operator fun Matrix.plus(d: Matrix): Matrix = Matrix(vectors.zip(d.vectors).map { it.first + it.second })
operator fun Matrix.minus(d: Matrix): Matrix = Matrix(vectors.zip(d.vectors).map { it.first - it.second })
operator fun Matrix.times(d: Matrix): Matrix {
    val otherCols = d.columns()
    return Matrix(vectors.map { Vector(otherCols.map { o -> it * o }) })
}
fun cellSign(index: Int): BigDecimal = (index % 2).compareTo(0.5).toBigDecimal().negate()
