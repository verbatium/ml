package ai.app

import java.math.BigDecimal

data class KNearestNeighbors(val k: Int, val classes: Int, val inputs: Matrix, val outputs: Vector) {
}

fun KNearestNeighbors.compute(input: Vector): BigDecimal {
    return inputs
        .map { neighbor -> (input - neighbor).modulus() } // Find distance
        .zip(outputs)                                     // Join Category
        .sortedBy { it.first }                            // Find nearest
        .take(k)                                          // Take first K elements
        .groupingBy { it.second }.eachCount()             // Sum Count by Category
        .entries.maxBy { it.value }.key                   // Take First Category
}