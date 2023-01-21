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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos

@Composable
@Preview
fun App() {
    var buttonLabel by remember { mutableStateOf("Hello, World!") }
    val maxY = 10
    val minY = -10
    val minX = 0
    val maxX = 360
    val xAxis = XAxis(BigDecimal(minX), BigDecimal(maxX), 18, 15f)
    val yAxis = YAxis(BigDecimal(minY), BigDecimal(maxY), 10, 15f)
    val sin = IntRange(minX, maxX).map { Offset(it.toFloat(), (sin(PI * it / 180) * 10).toFloat()) }
    val cos = IntRange(minX, maxX).map { Offset(it.toFloat(), (cos(PI * it / 180) * 5).toFloat()) }
    val p = sin.zip(cos).map { Offset(it.first.x , it.first.y + it.second.y) }
    MaterialTheme {
        Column {
            Button(onClick = { buttonLabel = "Hello, Desktop!" }) {
                Text(buttonLabel)
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color.Gray)
                val chartWidth = size.width - yAxis.width
                val chartHeight = size.height - xAxis.height
                clipRect {
                    scale(1f, -1f, Offset(0f, (size.height - xAxis.height) / 2f)) {
                        translate(yAxis.width, 0f) {
                            drawRect(color = Color.White)
                            drawIntoCanvas { c ->
                                c.save()
                                c.scale(chartWidth / (maxX - minX), chartHeight / (maxY - minY))
                                c.translate((-minX).toFloat() , (-minY).toFloat())
                                c.drawPoints(PointMode.Polygon, sin, Paint())
                                c.drawPoints(PointMode.Polygon, cos, Paint().apply { color = Color.Red })
                                c.drawPoints(PointMode.Polygon, p, Paint().apply { color = Color.Green })
                                c.restore()
                            }
                            yAxis.draw(this, chartHeight)
                            xAxis.draw(this, chartWidth)
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

