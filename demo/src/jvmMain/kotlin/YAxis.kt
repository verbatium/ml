import java.math.BigDecimal


class YAxis(min: BigDecimal, max: BigDecimal, ticks: Int, ticksLength: Float) :
    Axis(min, max, ticks, ticksLength, 0, 1) {
    val width = maxWidth + tickLength
}