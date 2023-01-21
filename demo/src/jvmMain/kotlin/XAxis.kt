import java.math.BigDecimal

class XAxis(min: BigDecimal, max: BigDecimal, ticks: Int, ticksLength: Float) :
    Axis(min, max, ticks, ticksLength, 1, 0) {
    val height = maxHeight + tickLength
}