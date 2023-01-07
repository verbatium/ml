package ai.app

import ai.app.Matrix.Companion.matrixOf
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext

data class Vector(private val args: List<BigDecimal>) : List<BigDecimal> {
    companion object {
        fun vectorOf(vararg args: BigDecimal): Vector = Vector(args.asList())
        fun vectorOf(vararg args: Int): Vector = Vector(args.asList().map(Int::toBigDecimal).toList())
        fun isCoplanar(vararg vectors: Vector): Boolean = matrixOf(*vectors).rank() < 3
        fun scalar(value: Int, size: Int): Vector = scalar(value.toBigDecimal(), size)
        fun scalar(value: BigDecimal, size: Int): Vector = Vector((0 until size).map { value })
    }

    override val size: Int
        get() = args.size

    override fun get(index: Int): BigDecimal = args.get(index)
    override fun isEmpty(): Boolean = args.isEmpty()
    override fun iterator(): Iterator<BigDecimal> = args.iterator()
    override fun listIterator(): ListIterator<BigDecimal> = args.listIterator()
    override fun listIterator(index: Int): ListIterator<BigDecimal> = args.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<BigDecimal> = args.subList(fromIndex, toIndex)
    override fun lastIndexOf(element: BigDecimal): Int = args.lastIndexOf(element)
    override fun indexOf(element: BigDecimal): Int = args.indexOf(element)
    override fun containsAll(elements: Collection<BigDecimal>): Boolean = args.containsAll(elements)
    override fun contains(element: BigDecimal): Boolean = args.contains(element)

    override fun toString(): String = joinToString(",", "[", "]")
}

fun Vector.vectorMultiply(other: Vector): Vector {
    return Vector(matrixOf(this, other)
        .cofactors(2 to 2)
        .map { it.determinant() }
        .mapIndexed {index, d -> d * cellSign(index) }
    )
}
fun Vector.hadamardProduct(vectorOf: Vector): Vector = Vector(zip(vectorOf).map { it.first * it.second })
fun Vector.projectionOn(other: Vector): BigDecimal = projectionOn(other, MathContext.DECIMAL128)
fun Vector.projectionOn(other: Vector, context: MathContext): BigDecimal = (this * other).divide(other.modulus(), context)
fun Vector.isOrthogonal(other: Vector): Boolean = (this * other).isZero()
fun Vector.cosA(other: Vector): BigDecimal = cosA(other, MathContext.DECIMAL128)
fun Vector.cosA(other: Vector, context: MathContext): BigDecimal = (this * other).divide((modulus2() * other.modulus2()).sqrt(context), context)
fun Vector.isCollinear(other: Vector): Boolean = this.isCollinear(other, MathContext.DECIMAL128)
fun Vector.isCollinear(other: Vector, context: MathContext): Boolean {
    return zip(other)
        .filter { it.first.compareTo(ZERO) != 0 && it.second.compareTo(ZERO) != 0 }
        .map {  it.second.divide(it.first, context) }
        .firstOrNull()
        ?.let { other == this * it }
        ?: false
}
fun Vector.cos(context: MathContext): Vector = Vector(map { it.divide(modulus(), context) })
fun Vector.cos(): Vector = this.cos(MathContext.DECIMAL128)
fun Vector.modulus(): BigDecimal = modulus2().sqrt(MathContext.DECIMAL128)
fun Vector.modulus2(): BigDecimal = map { it * it }.fold(ZERO, BigDecimal::add)
fun Vector.divide(k: BigDecimal, mathContext: MathContext) = Vector(map { it.divide(k, mathContext) })
fun Vector.sum(): BigDecimal = fold(ZERO, BigDecimal::add)
fun Vector.mean(): BigDecimal = mean(MathContext.DECIMAL128)
fun Vector.mean(mathContext: MathContext): BigDecimal = fold(ZERO, BigDecimal::add).divide(size.toBigDecimal(), mathContext)
fun Vector.standardDeviation(): BigDecimal = standardDeviation(MathContext.DECIMAL128)
fun Vector.standardDeviation(mathContext: MathContext): BigDecimal =
    fold(ZERO to ZERO) { acc, pair -> acc.first + pair to acc.second + pair.pow(2) }
    .let { it.second.multiply(size.toBigDecimal()) - it.first.pow(2) }
    .divide(size.toBigDecimal().pow(2), mathContext)
    .sqrt(mathContext)
operator fun Vector.plus(b: Vector) = Vector(zip(b).map { it.first + it.second })
operator fun Vector.minus(b: Vector) = Vector(zip(b).map { it.first - it.second })
operator fun Vector.times(k: BigDecimal) = Vector(map { it * k })
operator fun Vector.times(other: Vector): BigDecimal = zip(other).map { it.first * it.second }.fold(ZERO, BigDecimal::add)

fun BigDecimal.isZero(): Boolean = this.compareTo(ZERO) == 0