package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.MathContext

data class LinearRegression(val yVector: Vector, val xVectors: List<Vector>) {
    companion object {
        fun regression(yVector: Vector, vararg xVectors: Vector): LinearRegression {
            if (xVectors.first().args.toSet().first().minus(ONE).isZero()) {
                return LinearRegression(yVector, xVectors.toList())
            }
            return LinearRegression(yVector, listOf(Vector.scalar(ONE, yVector.args.size), *xVectors))
        }
    }
}

fun LinearRegression.size(): Int = xVectors.map { it.args.size }.first()

fun LinearRegression.solve(mathContext: MathContext): Vector {
    val xYSquaresSum = Matrix.vectorRow(
        Vector(xVectors
            .map { Vector(it.args.zip(yVector.args).map { it.first * it.second }) }
            .map { it.sum() })
    ).transpose()

    return (Matrix(xVectors).transpose().vectors
        .map { Matrix.vectorColumn(it) * Matrix.vectorRow(it) }
        .fold(Matrix.scalar(BigDecimal.ZERO, xVectors.size)) { acc, m -> acc + m }.inverse(mathContext) * xYSquaresSum)
        .vectors.first()
}

fun LinearRegression.f(vectorOf: Vector, mathContext: MathContext): BigDecimal {
    if (vectorOf.args.size < xVectors.size) return f(Vector(listOf(ONE) + vectorOf.args), mathContext)
    return solve(mathContext).args
        .zip(vectorOf.args)
        .map {
            it.first * it.second
        }.fold(BigDecimal.ZERO, BigDecimal::add)
}

fun LinearRegression.meanSquaredError(mathContext: MathContext): BigDecimal {
    return Matrix(xVectors).transpose().vectors
        .map { f(it, mathContext) }
        .zip(yVector.args)
        .map {
            val v = (it.first - it.second).pow(2)
            println("${it.first}-${it.second})^2 = ${v}")
            v
        }
        .fold(BigDecimal.ZERO, BigDecimal::add)
        .divide(yVector.args.size.toBigDecimal(), mathContext)
}