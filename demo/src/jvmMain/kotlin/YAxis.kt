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

data class YAxis(
    private val min: BigDecimal,
    private val max: BigDecimal,
    private val ticks: Int,
    private val tickLength: Float = 55f
) {
    private val textPaint = org.jetbrains.skia.Paint().apply {
        isAntiAlias = true
        color = org.jetbrains.skia.Color.BLACK
    }

    private val font = Font().apply {
        size = 30f
    }

    private val height by lazy { (max - min).toFloat() }
    private val labels by lazy { AxisUtils.split(min, max, ticks) }
    private val labelDimensions by lazy { labels.map { TextLine.make("$it", font) } }
    private val maxWidth = labelDimensions.maxOf { it.width }
    val width = maxWidth + tickLength

    fun draw(scope: DrawScope, length: Float) {
        val k = length / height
        scope.apply {
            this.drawLine(
                color = Color.Black,
                start = Offset(width, 0f),
                end = Offset( width, length),
                strokeWidth = 2f
            )

            labels.forEach {
                val dy = (it - min).toFloat() * k
                translate( maxWidth, dy) {
                    this.drawLine(
                        color = Color.Black,
                        start = Offset(0f, 0f),
                        end = Offset(tickLength, 0f),
                        strokeWidth = 3f
                    )
                    this.drawIntoCanvas { c ->
                        val textLine = TextLine.make("$it", font)
                        c.save()
                        c.scale(1f, -1f)
                        c.nativeCanvas.drawString(s = "$it", x =-maxWidth, y = textLine.height / 2f, font, paint = textPaint)
                        c.restore()
                    }
                }
            }
        }
    }
}