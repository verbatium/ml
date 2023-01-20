package ai.utilities

import java.math.BigDecimal
import java.math.RoundingMode

class AxisUtils {
    companion object {
        fun split(start: BigDecimal, end: BigDecimal, parts: Int): List<BigDecimal> {
            val width = end - start
            val part = steps(width / parts.toBigDecimal())
            val newStart = start.divide(part).setScale(0, RoundingMode.DOWN) * part
            val calcParts = end.subtract(newStart).divide(part).setScale(0, RoundingMode.UP)
            return IntRange(0, calcParts.toInt()).map {newStart + it.toBigDecimal() * part }
        }

        private fun steps(value: BigDecimal) : BigDecimal {
            val factor = value.scale() - value.precision() + 1
            val scaled = value.movePointRight(factor).setScale(1, RoundingMode.HALF_EVEN)
            return when {
                scaled >= BigDecimal(5) -> BigDecimal(5).movePointLeft(factor)
                scaled >= BigDecimal(2.5) -> BigDecimal(2.5).movePointLeft(factor)
                scaled >= BigDecimal(2) -> BigDecimal(2).movePointLeft(factor)
                scaled >= BigDecimal(1.5) -> BigDecimal(2).movePointLeft(factor)
                else -> BigDecimal(1).movePointLeft(factor)
            }
        }
    }
}