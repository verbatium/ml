package ai.app

import java.math.BigDecimal
import java.math.MathContext

data class KNearestNeighbors(val k: Int, val classes: Int, val inputs: Matrix, val outputs: Vector) {
    val distanceAlgorithm: DistanceAlgorithm = Euclidean(MathContext.DECIMAL128)
}

fun KNearestNeighbors.compute(input: Vector): BigDecimal {
    return inputs
        .map { input to it }                  // Get input and known value
        .map(distanceAlgorithm::distance)     // Find distance between them
        .zip(outputs)                         // Join Category of knownValue
        .sortedBy { it.first }                // Find nearest
        .take(k)                              // Take first K elements
        .groupingBy { it.second }.eachCount() // Sum Count by Category
        .entries.maxBy { it.value }.key       // Take First Category
}