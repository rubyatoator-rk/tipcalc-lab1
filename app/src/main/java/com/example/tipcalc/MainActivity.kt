package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalc.ui.theme.TipCalcTheme
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalcScreen()
                }
            }
        }
    }
}

@Composable
fun TipCalcScreen() {
    // State variables
    var billText by rememberSaveable { mutableStateOf("") }

    // Exercise 1.3: Initial tip is your R = 16% (0.16f)
    var tipPercent by rememberSaveable { mutableFloatStateOf(0.16f) }

    // Exercise 1.1: Default people is your P = 5
    var people by rememberSaveable { mutableIntStateOf(5) }

    // Derived calculations
    val bill = billText.toDoubleOrNull() ?: 0.0
    val tip = bill * tipPercent
    val total = bill + tip
    val perPerson = if (people > 0) total / people else 0.0

    // Exercise 1.2: Round up by your Delta = 0.20
    val delta = 0.20
    val perPersonRounded = ceil(perPerson / delta) * delta

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(), // Keeps keyboard from covering the card
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tip Calculator",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = billText,
            onValueChange = { billText = it },
            label = { Text("Bill amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Tip: ${(tipPercent * 100).toInt()}%")

        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..0.30f,
            steps = 29 // Gives 1% steps so 16% is reachable
        )

        // People Stepper (Exercise 1.1)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("People: $people", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { if (people > 1) people-- }) { Text("−") }
            Button(onClick = { if (people < 20) people++ }) { Text("+") }
        }

        Spacer(Modifier.weight(1f)) // Pushes the card to the bottom

        Card(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tip: $${"%.2f".format(tip)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Total: $${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Per person: $${"%.2f".format(perPerson)}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Rounded per person: $${"%.2f".format(perPersonRounded)}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        // Clear Button (Exercise 1.3)
        Button(
            onClick = {
                billText = ""
                tipPercent = 0.16f // Resets to your R (16%)
                people = 5        // Resets to your P (5)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TipCalcPreview() {
    TipCalcTheme {
        TipCalcScreen()
    }
}