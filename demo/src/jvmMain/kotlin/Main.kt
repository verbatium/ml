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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.math.BigDecimal

@Composable
@Preview
fun App() {
    var buttonLabel by remember { mutableStateOf("Hello, World!") }
    val xAxis = XAxis(BigDecimal(121), BigDecimal(345), 20, 15f)
    val yAxis = YAxis(BigDecimal(-20), BigDecimal(20), 10, 15f)
    MaterialTheme {
        Column {
            Button(onClick = { buttonLabel = "Hello, Desktop!" }) {
                Text(buttonLabel)
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color.Gray)
                clipRect {
                    scale(1f, -1f, Offset(0f, (size.height - xAxis.height) / 2f)) {
                        yAxis.draw(this, size.height - xAxis.height)
                        translate(yAxis.width, 0f) {
                            xAxis.draw(this, size.width - yAxis.width)
                            drawRect(color = Color.White)
                        }
                    }
                }

            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

