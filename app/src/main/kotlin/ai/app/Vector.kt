package ai.app

import ai.app.Matrix.Companion.matrixOf
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext

data class Vector(val args: List<BigDecimal>) {
    companion object {
        fun vectorOf(vararg args: BigDecimal): Vector = Vector(args.asList())
        fun vectorOf(vararg args: Int): Vector = Vector(args.asList().map(Int::toBigDecimal).toList())
        fun isCoplanar(vararg vectors: Vector): Boolean = matrixOf(*vectors).rank() < 3
        fun scalar(value: Int, size: Int): Vector = scalar(value.toBigDecimal(), size)
        fun scalar(value: BigDecimal, size: Int): Vector = Vector((0 until size).map { value })
    }

    override fun toString(): String = args.joinToString(",", "[", "]")
}

fun Vector.vectorMultiply(other: Vector): Vector {
    return Vector(matrixOf(this, other)
        .cofactors(2 to 2)
        .map { it.determinant() }
        .mapIndexed {index, d -> d * cellSign(index) }
    )
}

fun Vector.projectionOn(other: Vector): BigDecimal = projectionOn(other, MathContext.DECIMAL128)
fun Vector.projectionOn(other: Vector, context: MathContext): BigDecimal = (this * other).divide(other.modulus(), context)
fun Vector.isOrthogonal(other: Vector): Boolean = (this * other).isZero()
fun Vector.cosA(other: Vector): BigDecimal = cosA(other, MathContext.DECIMAL128)
fun Vector.cosA(other: Vector, context: MathContext): BigDecimal = (this * other).divide((modulus2() * other.modulus2()).sqrt(context), context)
fun Vector.isCollinear(other: Vector): Boolean = this.isCollinear(other, MathContext.DECIMAL128)
fun Vector.isCollinear(other: Vector, context: MathContext): Boolean {
    return args.zip(other.args)
        .filter { it.first.compareTo(ZERO) != 0 && it.second.compareTo(ZERO) != 0 }
        .map { it -> it.second.divide(it.first, context) }
        .firstOrNull()
        ?.let { other == this * it }
        ?: false
}
fun Vector.cos(context: MathContext): Vector = Vector(args.map { it.divide(modulus(), context) })
fun Vector.cos(): Vector = this.cos(MathContext.DECIMAL128)
fun Vector.modulus(): BigDecimal = modulus2().sqrt(MathContext.DECIMAL128)
fun Vector.modulus2(): BigDecimal = args.map { it * it }.fold(ZERO, BigDecimal::add)
fun Vector.divide(k: BigDecimal, mathContext: MathContext) = Vector(args.map { it.divide(k, mathContext)})
operator fun Vector.plus(b: Vector) = Vector(args.zip(b.args).map { it.first + it.second })
operator fun Vector.minus(b: Vector) = Vector(args.zip(b.args).map { it.first - it.second })
operator fun Vector.times(k: BigDecimal) = Vector(args.map { it * k })
operator fun Vector.times(other: Vector): BigDecimal = args.zip(other.args).map { it.first * it.second }.fold(ZERO, BigDecimal::add)

fun BigDecimal.isZero(): Boolean = this.compareTo(ZERO) == 0