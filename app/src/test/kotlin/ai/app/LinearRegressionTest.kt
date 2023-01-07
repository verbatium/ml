package ai.app

import ai.app.Vector.Companion.vectorOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.MathContext
import java.math.RoundingMode.HALF_UP

class LinearRegressionTest {
    @Test
    fun size() {
        val xValues = vectorOf(0, 2, 5, 7, 9, 0, 3, 4, 5)
        val yValues = vectorOf(5, 9, 5, 15, 20, 25, 30, 35, 30)
        val regression = LinearRegression.regression(yValues, xValues)
        assertEquals(9, regression.size())
    }

    @Test
    fun meanSquaredError() {
        val mathContext = MathContext(5, HALF_UP)
        val xValues = vectorOf(0, 2, 5, 7, 9, 0, 3, 4, 5)
        val yValues = vectorOf(5, 9, 5, 15, 20, 25, 30, 35, 30)
        val regression = LinearRegression.regression(yValues, xValues)
        assertEquals(d(114.28), regression.meanSquaredError(mathContext))
    }

    @Test
    fun f() {
        val mathContext = MathContext(5, HALF_UP)
        val xValues = vectorOf(0, 2, 5, 7, 9, 0, 3, 4, 5)
        val yValues = vectorOf(5, 9, 5, 15, 20, 25, 30, 35, 30)
        val regression = LinearRegression.regression(yValues, xValues)
        assertEquals(d(21.533244), regression.f(vectorOf(d(9)), mathContext))
    }

    @Test
    fun solve1() {
        val mathContext = MathContext.DECIMAL128
        val vectorX2 = vectorOf(
            3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 5, 5, 5, 5, 5, 6, 6, 5, 5, 5, 5, 5, 5, 5, 6, 6, 7, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7
        )
        val vectorY = vectorOf(
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11
        )

        assertEquals(
            vectorOf(d("-0.43925233644859813084112149532710366"), d("1.60747663551401869158878504672897786")),
            LinearRegression.regression(vectorY, vectorX2).solve(mathContext)
        )
        assertEquals(
            d("1.543925233644859813084112149532710"),
            LinearRegression.regression(vectorY, vectorX2).meanSquaredError(mathContext)
        )
    }

    @Test
    fun solve_2xVectors() {
        val mathContext = MathContext(5, HALF_UP)

        val vectorX2 = vectorOf(-1, 0, 1, 2, 3, -1, 0, 1, 2, 3)
        val vectorX3 = vectorOf(2, 0, 2, 2, 2, 0, 2, 0, 0, 0)
        val vectorY = vectorOf(2, -2, 2, 2, 2, -4, 2, -3, -2, 1)

        assertEquals(
            vectorOf(d("-2.50"), d("0.50"), d("2.0")),
            LinearRegression.regression(vectorY, vectorX2, vectorX3)
                .solve(mathContext)
        )
    }
}

private fun LinearRegression.b(mathContext: MathContext): List<BigDecimal> {
    val m = mean()
    val y = yVector.mean()
    return w(mathContext)
        .zip(m)
        .map { y - it.first * it.second }
}

private fun LinearRegression.w(mathContext: MathContext): List<BigDecimal> {
    val r = r(mathContext)
    val stdev = yVector.standardDeviation(mathContext)
    return standardDeviation(mathContext)
        .map { r * stdev.divide(it, mathContext) }
}

private fun LinearRegression.r(mathContext: MathContext): BigDecimal {
    return product().minus(yVector.mean() * mean().fold(ONE, BigDecimal::multiply))
        .divide(
            yVector.standardDeviation(mathContext) * standardDeviation(mathContext)
                .fold(ONE, BigDecimal::multiply), mathContext
        )
}

private fun LinearRegression.standardDeviation(mathContext: MathContext): List<BigDecimal> {
    return xVectors.map { it.standardDeviation(mathContext) }
}

private fun LinearRegression.mean(): List<BigDecimal> {
    return xVectors.map { it.mean() }
}

private fun LinearRegression.product(): BigDecimal {
    return xVectors.fold(Vector.scalar(ONE, size()), Vector::hadamardProduct).hadamardProduct(yVector).mean()
}

private fun LinearRegression.r2(mathContext: MathContext): BigDecimal {
    return r(mathContext).pow(2)
}