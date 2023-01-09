package ai.app

import java.math.BigDecimal

fun <T> List<T>.permutations(length: Int): List<List<T>> {
    if (length == 0) return listOf(listOf())
    return IntRange(0, size - length)
        .flatMap { idx ->
            this.drop(idx + 1)
                .permutations(length - 1)
                .map { listOf(this[idx]) + it }
        }
}

fun List<BigDecimal>.vector(): Vector = Vector(this)
fun List<List<BigDecimal>>.matrix(): Matrix = Matrix(this.map { it.vector() })