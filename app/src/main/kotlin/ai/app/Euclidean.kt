package ai.app

import java.math.BigDecimal

class Euclidean: DistanceAlgorithm {
    override fun distance(values: Pair<Vector, Vector>): BigDecimal = (values.first - values.second).modulus()
}