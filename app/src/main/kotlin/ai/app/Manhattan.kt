package ai.app

import java.math.BigDecimal

class Manhattan : DistanceAlgorithm {
    override fun distance(values: Pair<Vector, Vector>): BigDecimal =
        (values.first - values.second)
            .map { it.abs() }
            .sumOf { it }
}