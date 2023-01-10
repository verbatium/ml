package ai.app

import java.math.BigDecimal
import java.math.MathContext

class Minkowski(private val p: Int, private val mathContext: MathContext) : DistanceAlgorithm {
    override fun distance(values: Pair<Vector, Vector>): BigDecimal =
        (values.first - values.second)
            .map { it.abs().pow(p) }
            .sumOf { it }
            .nthRoot(p, mathContext)
}

