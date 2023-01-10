package ai.app

import java.math.BigDecimal
import java.math.MathContext

fun BigDecimal.nthRoot(n: Int, mathContext: MathContext): BigDecimal {
    require(this >= BigDecimal.ZERO) { "nth root can only be calculated for positive numbers" }
    if (this == BigDecimal.ZERO) return this
    if (n == 0) return BigDecimal.ONE
    if (n == 1) return this
    if (n == 2) return sqrt(mathContext)

    var xPrev: BigDecimal = this
    var x: BigDecimal = this.divide(BigDecimal(n), mathContext)
    val e = BigDecimal.valueOf(.1).movePointLeft(mathContext.precision)
    while (x.subtract(xPrev).abs() > e) {
        xPrev = x
        x = BigDecimal.valueOf(n - 1.0)
            .multiply(x)
            .add(this.divide(x.pow(n - 1), mathContext))
            .divide(BigDecimal(n), mathContext)
    }
    return x
}