package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.MathContext

data class LinearRegression(val yVector: Vector, val xVectors: List<Vector>) {
    companion object {
        fun regression(yVector: Vector, vararg xVectors: Vector): LinearRegression {
            if (xVectors.first().toSet().first().minus(ONE).isZero()) {
                return LinearRegression(yVector, xVectors.toList())
            }
            return LinearRegression(yVector, listOf(Vector.scalar(ONE, yVector.size), *xVectors))
        }
    }
}

fun LinearRegression.size(): Int = xVectors.map { it.size }.first()

fun LinearRegression.solve(mathContext: MathContext): Vector {
    val xYSquaresSum = Matrix.vectorRow(
        Vector(xVectors
            .map { Vector(it.zip(yVector).map { it.first * it.second }) }
            .map { it.sum() })
    ).transpose()

    return (Matrix(xVectors).transpose()
        .map { Matrix.vectorColumn(it) * Matrix.vectorRow(it) }
        .fold(Matrix.scalar(BigDecimal.ZERO, xVectors.size)) { acc, m -> acc + m }.inverse(mathContext) * xYSquaresSum)
        .first()
}

fun LinearRegression.f(vectorOf: Vector, mathContext: MathContext): BigDecimal {
    if (vectorOf.size < xVectors.size) return f(Vector(listOf(ONE) + vectorOf), mathContext)
    return solve(mathContext)
        .zip(vectorOf)
        .map {
            it.first * it.second
        }.fold(BigDecimal.ZERO, BigDecimal::add)
}

fun LinearRegression.meanSquaredError(mathContext: MathContext): BigDecimal {
    return Matrix(xVectors).transpose()
        .map { f(it, mathContext) }
        .zip(yVector)
        .map {
            val v = (it.first - it.second).pow(2)
            println("${it.first}-${it.second})^2 = ${v}")
            v
        }
        .fold(BigDecimal.ZERO, BigDecimal::add)
        .divide(yVector.size.toBigDecimal(), mathContext)
}