import ai.utilities.AxisUtils
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.Font
import java.math.BigDecimal

@Composable
@Preview
fun App() {
    var buttonLabel by remember { mutableStateOf("Hello, World!") }
    val xAxis = Axis(BigDecimal(121), BigDecimal(345), 10)
    MaterialTheme {
        Column {
            Button(onClick = { buttonLabel = "Hello, Desktop!" }) {
                Text(buttonLabel)
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color.Gray)
                xAxis.draw(this)
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

data class Axis(
    private val min: BigDecimal,
    private val max: BigDecimal,
    private val ticks: Int
) {
    private val textPaint = org.jetbrains.skia.Paint().apply {
        isAntiAlias = true
        color = org.jetbrains.skia.Color.BLUE
    }

    private val font = Font().apply {
        size = 50f
    }

    private val width by lazy { (max - min).toFloat() }
    private val labels by lazy { AxisUtils.split(min, max, ticks) }

    fun draw(scope: DrawScope) {
        val k = scope.size.width / width
        scope.translate(0f, 0f) {
            this.drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(scope.size.width, 0f),
            )
            labels.forEach {
                val dx = (it - min).toFloat() * k
                translate(dx, 0f) {
                    this.drawLine(
                        color = Color.Black,
                        start = Offset(dx, -1f),
                        end = Offset(dx, 15f),
                    )

                    this.drawIntoCanvas { c ->
                        c.nativeCanvas.drawString(s = "$it", x = 0f, y = 55f, font, paint = textPaint)
                    }
                }
            }
        }
    }
}