package ai.app

import java.math.BigDecimal.ONE

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