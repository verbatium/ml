package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext

data class Matrix(private val vectors: List<Vector>) : List<Vector> {
    companion object {
        fun matrixOf(vararg args: Vector): Matrix {
            val a = args.asList()
            if (a.map { it.size }.toSet().size != 1) throw IllegalArgumentException("All vectors of matrix must have same dimension")
            return Matrix(a)
        }

        fun diagonal(vectorOf: Vector): Matrix =
            Matrix(vectorOf.mapIndexed { i, it -> Vector((0 until vectorOf.size).map { j -> if (i == j) it else ZERO }) })

        fun identity(size: Int): Matrix = diagonal(Vector.scalar(1, size))
        fun scalar(value: Int, size: Int): Matrix = diagonal(Vector.scalar(value, size))
        fun scalar(value: BigDecimal, size: Int): Matrix = diagonal(Vector.scalar(value, size))
        fun vectorRow(row: Vector): Matrix = matrixOf(row)
        fun vectorColumn(vector: Vector): Matrix = vectorRow(vector).transpose()
    }

    override val size: Int
        get() = vectors.size

    override fun get(index: Int): Vector = vectors.get(index)
    override fun isEmpty(): Boolean = vectors.isEmpty()
    override fun iterator(): Iterator<Vector> = vectors.iterator()
    override fun listIterator(): ListIterator<Vector> = vectors.listIterator()
    override fun listIterator(index: Int): ListIterator<Vector> = vectors.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<Vector> = vectors.subList(fromIndex, toIndex)
    override fun lastIndexOf(element: Vector): Int = lastIndexOf(element)
    override fun indexOf(element: Vector): Int = vectors.indexOf(element)
    override fun containsAll(elements: Collection<Vector>): Boolean = vectors.containsAll(elements)
    override fun contains(element: Vector): Boolean = vectors.contains(element)
    override fun toString(): String = joinToString(",", "[", "]")
}

fun Matrix.columns() = (0 until cols()).map { index -> Vector(map { v -> v[index] }) }
fun Matrix.transpose(): Matrix = Matrix(columns())
fun Matrix.rows(): Int = size
fun Matrix.cols(): Int = first().size
fun Matrix.size(): Pair<Int, Int> = rows() to cols()
fun Matrix.isSquare(): Boolean = rows() == cols()
fun Matrix.determinant(): BigDecimal {
    if (!this.isSquare()) throw IllegalArgumentException("Determinant can be calculated only for square matrix")
    if (size == 1) return first().first()
    return first().foldIndexed(ZERO) { index, total, item ->
        Matrix(slice(1 until size).map { Vector(it.filterIndexed { i, _ -> i != index }) })
            .determinant() * item * cellSign(index) + total
    }
}

fun Matrix.rank(): Int {
    val maxRank = size.coerceAtMost(first().size)
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
    Matrix(filterIndexed { index, _ -> !exclusions.first.contains(index) }
        .map { Vector(it.filterIndexed { index, _ -> !exclusions.second.contains(index) }) })

fun Matrix.isDiagonal(): Boolean = mapIndexed { i, vectors ->
    vectors
        .filterIndexed { j, _ -> i != j }
        .map { it.isZero() }
        .fold(true) { acc, b -> acc && b }
}.fold(true) { acc, b -> acc && b }

fun Matrix.minor(i: Int, j: Int): BigDecimal = cofactor(i, j).determinant()
fun Matrix.cofactor(i: Int, j: Int): Matrix = cofactor(listOf(i) to listOf(j))
fun Matrix.adjugate(): Matrix =
    Matrix(mapIndexed { i, row -> Vector(List(row.size) { j -> minor(i, j) * cellSign(i) * cellSign(j) }) })

fun Matrix.inverse(mathContext: MathContext): Matrix = this.adjugate().divide(this.determinant(), mathContext).transpose()
fun Matrix.divide(divider: BigDecimal, mathContext: MathContext) = Matrix(map { it.divide(divider, mathContext) })
operator fun Matrix.times(other: BigDecimal): Matrix = Matrix(map { it * other })
operator fun Matrix.plus(other: Matrix): Matrix = Matrix(zip(other).map { it.first + it.second })
operator fun Matrix.minus(other: Matrix): Matrix = Matrix(zip(other).map { it.first - it.second })
operator fun Matrix.times(other: Matrix): Matrix {
    return Matrix(other.transpose()
        .map {
            Vector(map { o -> it * o })
        })
}

fun cellSign(index: Int): BigDecimal = (index % 2).compareTo(0.5).toBigDecimal().negate()
