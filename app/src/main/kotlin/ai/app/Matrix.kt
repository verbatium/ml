package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext

data class Matrix(val vectors: List<Vector>) {
    companion object {
        fun matrixOf(vararg args: Vector): Matrix {
            val a = args.asList()
            if (a.map { it.size }.toSet().size != 1) throw IllegalArgumentException("All vectors of matrix must have same dimension")
            return Matrix(a)
        }

        fun diagonal(vectorOf: Vector): Matrix =
            Matrix(vectorOf.mapIndexed { i, it -> Vector((0 until  vectorOf.size).map { j -> if (i == j) it else ZERO }) })

        fun identity(size: Int): Matrix = diagonal(Vector.scalar(1, size))
        fun scalar(value: Int, size: Int): Matrix = diagonal(Vector.scalar(value, size))
        fun scalar(value: BigDecimal, size: Int): Matrix = diagonal(Vector.scalar(value, size))
        fun vectorRow(row: Vector): Matrix = matrixOf(row)
        fun vectorColumn(vector: Vector): Matrix = vectorRow(vector).transpose()
    }

    override fun toString(): String = vectors.joinToString(",", "[", "]")
}

fun Matrix.columns() = (0 until  cols()).map { index -> Vector(vectors.map { v -> v[index] }) }
fun Matrix.transpose(): Matrix = Matrix(columns())
fun Matrix.rows(): Int = vectors.size
fun Matrix.cols(): Int = vectors.first().size
fun Matrix.size(): Pair<Int, Int> = rows() to cols()
fun Matrix.isSquare(): Boolean = rows() == cols()
fun Matrix.determinant(): BigDecimal {
    if (!this.isSquare()) throw IllegalArgumentException("Determinant can be calculated only for square matrix")
    if (vectors.size == 1) return vectors.first().first()
    return vectors[0].foldIndexed(ZERO) { index, total, item ->
        Matrix(vectors.slice(1 until vectors.size).map { Vector(it.filterIndexed { i, _ -> i != index }) })
            .determinant() * item * cellSign(index) + total
    }
}

fun Matrix.rank(): Int {
    val maxRank = vectors.size.coerceAtMost(vectors.first().size)
    return IntRange(0, maxRank - 1).toList().stream()
        .filter { !cofactors(it + 1 to it + 1).stream().anyMatch { m -> !m.determinant().isZero() } }
        .findFirst()
        .orElse(maxRank)
}

fun Matrix.cofactors(size: Pair<Int, Int>): List<Matrix> {
    return IntRange(0, rows() - 1).toList().permutations(rows() - size.first)
        .flatMap { row ->
            IntRange(0, cols() - 1).toList().permutations(cols() - size.second).map { col -> row to col }
        }
        .map(this::cofactor)
}

fun Matrix.cofactor(exclusions: Pair<List<Int>, List<Int>>): Matrix =
    Matrix(vectors.filterIndexed { index, _ -> !exclusions.first.contains(index) }
        .map { Vector(it.filterIndexed { index, _ -> !exclusions.second.contains(index) }) })

fun Matrix.isDiagonal(): Boolean = vectors
    .mapIndexed { i, vectors ->
        vectors
            .filterIndexed { j, _ -> i != j }
            .map { it.isZero() }
            .fold(true) { acc, b -> acc && b }
    }.fold(true) { acc, b -> acc && b }

fun Matrix.minor(i: Int, j: Int): BigDecimal = cofactor(i, j).determinant()
fun Matrix.cofactor(i: Int, j: Int): Matrix = cofactor(listOf(i) to listOf(j))
fun Matrix.adjugate(): Matrix =
    Matrix(vectors.mapIndexed { i, row -> Vector(List(row.size) { j -> minor(i, j) * cellSign(i) * cellSign(j) }) })

fun Matrix.inverse(mathContext: MathContext): Matrix = this.adjugate().divide(this.determinant(), mathContext).transpose()
fun Matrix.divide(divider: BigDecimal, mathContext: MathContext) = Matrix(vectors.map { it.divide(divider, mathContext) })
operator fun Matrix.times(other: BigDecimal): Matrix = Matrix(vectors.map { it * other })
operator fun Matrix.plus(other: Matrix): Matrix = Matrix(vectors.zip(other.vectors).map { it.first + it.second })
operator fun Matrix.minus(other: Matrix): Matrix = Matrix(vectors.zip(other.vectors).map { it.first - it.second })
operator fun Matrix.times(other: Matrix): Matrix {
    return Matrix(other.transpose()
        .vectors.map {
            Vector(vectors.map { o -> it * o })
        })
}

fun cellSign(index: Int): BigDecimal = (index % 2).compareTo(0.5).toBigDecimal().negate()
