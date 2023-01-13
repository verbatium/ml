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
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.Font
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

@Composable
@Preview
fun App() {
    var buttonLabel by remember { mutableStateOf("Hello, World!") }
    val xAxis = Axis(ZERO, BigDecimal.TEN, 10)
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
    fun draw(scope: DrawScope) {
        val k = scope.size.width / width
        scope.translate(0f, scope.size.height / 2f) {
            this.drawIntoCanvas { c -> c.nativeCanvas.drawString(s = "0", x = 0f, y = -10f, font, paint = textPaint) }
            this.drawLine(
                color = Color.Black,
                start = Offset(0f, 0f),
                end = Offset(scope.size.width, 0f),
            )
            IntRange(0, ticks)
                .map { it.toFloat() * k }.forEach {
                    this.drawLine(
                        color = Color.Black,
                        start = Offset(it, -1f),
                        end = Offset(it, 15f),
                    )
                }
        }
    }
}