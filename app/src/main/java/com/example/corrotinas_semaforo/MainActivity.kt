package com.example.corrotinas_semaforo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.corrotinas_semaforo.ui.theme.CorrotinassemaforoTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CorrotinassemaforoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SemaforoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class ModoSemaforo {
    AUTOMATICO,
    AMARELO_PISCANTE
}

enum class LuzSemaforo {
    VERMELHO,
    AMARELO,
    VERDE,
    APAGADO
}

@Composable
fun SemaforoScreen(modifier: Modifier = Modifier) {
    var modoAtual by remember { mutableStateOf(ModoSemaforo.AUTOMATICO) }
    var luzAtual by remember { mutableStateOf(LuzSemaforo.VERMELHO) }

    // Sempre que o modo muda, esta corrotina reinicia a lógica do semáforo.
    LaunchedEffect(modoAtual) {
        when (modoAtual) {
            ModoSemaforo.AUTOMATICO -> {
                while (true) {
                    luzAtual = LuzSemaforo.VERMELHO
                    delay(3_000)

                    luzAtual = LuzSemaforo.VERDE
                    delay(3_000)

                    luzAtual = LuzSemaforo.AMARELO
                    delay(1_500)
                }
            }

            ModoSemaforo.AMARELO_PISCANTE -> {
                while (true) {
                    luzAtual = LuzSemaforo.AMARELO
                    delay(500)

                    luzAtual = LuzSemaforo.APAGADO
                    delay(500)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Simulador de Semáforo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Semaforo(luzAtual = luzAtual)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    modoAtual = if (modoAtual == ModoSemaforo.AUTOMATICO) {
                        ModoSemaforo.AMARELO_PISCANTE
                    } else {
                        ModoSemaforo.AUTOMATICO
                    }
                }
            ) {
                Text("Amarelo piscante")
            }
        }
    }
}

@Composable
fun Semaforo(luzAtual: LuzSemaforo) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2E2E2E))
            .padding(horizontal = 28.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Luz(color = Color.Red, ligada = luzAtual == LuzSemaforo.VERMELHO)
        Luz(color = Color.Yellow, ligada = luzAtual == LuzSemaforo.AMARELO)
        Luz(color = Color.Green, ligada = luzAtual == LuzSemaforo.VERDE)
    }
}

@Composable
fun Luz(color: Color, ligada: Boolean) {
    val corFinal = if (ligada) color else color.copy(alpha = 0.20f)

    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
            .background(corFinal)
    )
}

@Preview(showBackground = true)
@Composable
fun SemaforoScreenPreview() {
    CorrotinassemaforoTheme {
        SemaforoScreen()
    }
}
