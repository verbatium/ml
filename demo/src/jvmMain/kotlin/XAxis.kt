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

data class XAxis(
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

    private val width by lazy { (max - min).toFloat() }
    private val labels by lazy { AxisUtils.split(min, max, ticks) }
    private val labelDimensions by lazy { labels.map { TextLine.make("$it", font) } }
    private val maxHeight = labelDimensions.maxOf { it.height }
    val maxWidth = labelDimensions.maxOf { it.width }
    val height = maxHeight + tickLength

    fun draw(scope: DrawScope) {
        val k = scope.size.width / width
        scope.apply {
            this.drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(scope.size.width, 0f),
                strokeWidth = 2f
            )

            labels.forEach {
                val dx = (it - min).toFloat() * k
                translate(dx, -tickLength) {
                    this.drawLine(
                        color = Color.Black,
                        start = Offset(0f, 0f),
                        end = Offset(0f, tickLength),
                        strokeWidth = 3f
                    )
                    this.drawIntoCanvas { c ->
                        val textLine = TextLine.make("$it", font)
                        c.save()
                        c.scale(1f, -1f)
                        c.nativeCanvas.drawString(s = "$it", x = -textLine.width / 2f, y = maxHeight, font, paint = textPaint)
                        c.restore()
                    }
                }
            }
        }
    }
}