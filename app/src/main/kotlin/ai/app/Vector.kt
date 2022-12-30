package ai.app

import java.math.BigDecimal
import java.math.MathContext

data class Vector(val args: List<BigDecimal>) {
    companion object {
        fun vectorOf(vararg args: BigDecimal): Vector = Vector(args.asList())
        fun vectorOf(vararg args: Int): Vector = Vector(args.asList().map(Int::toBigDecimal).toList())
    }
}

fun Vector.isOrthogonal(other: Vector): Boolean = (this * other).compareTo(BigDecimal.ZERO) == 0

fun Vector.isCollinear(other: Vector): Boolean = this.isCollinear(other, MathContext.DECIMAL128)
fun Vector.isCollinear(other: Vector, context: MathContext): Boolean {
    return args.zip(other.args)
        .filter { it.first.compareTo(BigDecimal.ZERO) != 0 && it.second.compareTo(BigDecimal.ZERO) != 0 }
        .map { it -> it.second.divide(it.first, context) }
        .firstOrNull()
        ?.let { other == this * it }
        ?: false
}


fun Vector.cos(context: MathContext): Vector = Vector(args.map { it.divide(modulus(), context) })
fun Vector.cos(): Vector = this.cos(MathContext.DECIMAL128)
fun Vector.modulus(): BigDecimal = args.map { it * it }.fold(BigDecimal.ZERO, BigDecimal::add).sqrt(MathContext.DECIMAL128)
operator fun Vector.plus(b: Vector) = Vector(args.zip(b.args).map { it.first + it.second })
operator fun Vector.minus(b: Vector) = Vector(args.zip(b.args).map { it.first - it.second })
operator fun Vector.times(k: BigDecimal) = Vector(args.map { it * k })
operator fun Vector.times(other: Vector): BigDecimal = args.zip(other.args)
    .map { it.first * it.second }
    .fold(BigDecimal.ZERO, BigDecimal::add)