package ai.app

import ai.app.Matrix.Companion.matrixOf
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext

data class Vector(val args: List<BigDecimal>) {
    companion object {
        fun vectorOf(vararg args: BigDecimal): Vector = Vector(args.asList())
        fun vectorOf(vararg args: Int): Vector = Vector(args.asList().map(Int::toBigDecimal).toList())

        fun isCoplanar(vararg vectors : Vector) : Boolean {
            val matrix = matrixOf(*vectors)
            if (matrix.size() == 2 to 2) return true
            if(matrix.size() == 3 to 3) {
                return matrix.determinant().isZero()
            }
            return false
        }
    }

    override fun toString(): String {
        return args.joinToString(",", "[", "]")
    }
}


fun Vector.isOrthogonal(other: Vector): Boolean = (this * other).isZero()

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
fun Vector.modulus(): BigDecimal = args.map { it * it }.fold(ZERO, BigDecimal::add).sqrt(MathContext.DECIMAL128)
operator fun Vector.plus(b: Vector) = Vector(args.zip(b.args).map { it.first + it.second })
operator fun Vector.minus(b: Vector) = Vector(args.zip(b.args).map { it.first - it.second })
operator fun Vector.times(k: BigDecimal) = Vector(args.map { it * k })
operator fun Vector.times(other: Vector): BigDecimal = args.zip(other.args)
    .map { it.first * it.second }
    .fold(ZERO, BigDecimal::add)

fun BigDecimal.isZero(): Boolean = this.compareTo(ZERO) == 0