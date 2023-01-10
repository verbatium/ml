package ai.app

import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO

class Hamming() : DistanceAlgorithm {
    override fun distance(values: Pair<Vector, Vector>): BigDecimal =
        values.first.zip(values.second)
            .map { if (it.first.compareTo(it.second) == 0) ZERO else ONE }
            .sumOf { it }
}

