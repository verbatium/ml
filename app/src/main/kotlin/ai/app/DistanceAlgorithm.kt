package ai.app

import java.math.BigDecimal

interface DistanceAlgorithm {
    fun distance(values: Pair<Vector, Vector>): BigDecimal
}
