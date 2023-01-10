package ai.app

import java.math.BigDecimal
import java.math.BigInteger

fun <T> List<T>.permutations(length: Int): List<List<T>> {
    if (length == 0) return listOf(listOf())
    return IntRange(0, size - length)
        .flatMap { idx ->
            this.drop(idx + 1)
                .permutations(length - 1)
                .map { listOf(this[idx]) + it }
        }
}

fun <T : Number> List<T>.vector(): Vector = Vector(this.map { x ->
    when (x) {
        is Double -> x.toBigDecimal()
        is Int -> x.toBigDecimal()
        is Long -> x.toBigDecimal()
        is BigDecimal -> x
        is BigInteger -> x.toBigDecimal()
        else -> throw IllegalArgumentException("unsupported type ${x.javaClass}")
    }
})

fun <T : Number> List<List<T>>.matrix(): Matrix = Matrix(this.map { it.vector() })