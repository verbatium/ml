import ai.utilities.AxisUtils
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine
import java.math.BigDecimal

open class Axis(
    private val min: BigDecimal,
    private val max: BigDecimal,
    private val ticks: Int,
    protected val tickLength: Float = 55f,
    private val kX: Int,
    private val kY: Int
) {
    protected val textPaint = org.jetbrains.skia.Paint().apply {
        isAntiAlias = true
        color = org.jetbrains.skia.Color.BLACK
    }

    protected val font = Font().apply {
        size = 30f
    }

    protected val scalaLength by lazy { (max - min).toFloat() }
    protected val labels by lazy { AxisUtils.split(min, max, ticks) }
    private val labelDimensions by lazy { labels.map { TextLine.make("$it", font) } }
    protected val maxWidth = labelDimensions.maxOf { it.width }
    protected val maxHeight = labelDimensions.maxOf { it.height }

    fun draw(scope: DrawScope, length: Float) {
        val k = length / scalaLength
        scope.apply {
            this.drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(length * kX, length * kY),
                strokeWidth = 2f
            )

            labels.forEach {
                val offset = (it - min).toFloat() * k
                translate(offset * kX -tickLength * kY, offset * kY -tickLength * kX) {
                    this.drawLine(
                        color = Color.Black,
                        start = Offset(0f, 0f),
                        end = Offset(tickLength * kY, tickLength * kX),
                        strokeWidth = 3f
                    )
                    this.drawIntoCanvas { c ->
                        val textLine = TextLine.make("$it", font)
                        c.save()
                        c.scale(1f, -1f)
                        val x = -textLine.width / 2f * kX -maxWidth * kY
                        val y = maxHeight * kX + textLine.height / 2f * kY
                        c.nativeCanvas.drawString(s = "$it", x = x, y = y, font, paint = textPaint)
                        c.restore()
                    }
                }
            }
        }
    }
}