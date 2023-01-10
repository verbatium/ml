package ai.app

import java.math.BigDecimal
import java.math.MathContext

class Euclidean(private val mathContext: MathContext) : DistanceAlgorithm {
    override fun distance(values: Pair<Vector, Vector>): BigDecimal =
        (values.first - values.second).map { it.pow(2) }
            .sumOf { it }
            .sqrt(mathContext)
}