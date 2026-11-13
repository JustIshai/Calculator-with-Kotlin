package com.example.kotlincalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var resetDisplay by remember { mutableStateOf(false) }

    fun formatNumber(number: Double): String {
        return if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }

    fun pressDigit(digit: String) {
        if (resetDisplay) {
            display = "0"
            resetDisplay = false
        }

        if (digit == "." && display.contains(".")) return

        display = if (display == "0" && digit != ".") {
            digit
        } else {
            display + digit
        }
    }

    fun chooseOperator(symbol: String) {
        firstNumber = display.toDoubleOrNull()
        operator = symbol
        resetDisplay = true
    }

    fun calculate() {
        val first = firstNumber ?: return
        val second = display.toDoubleOrNull() ?: return

        val result = when (operator) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> {
                if (second == 0.0) {
                    display = "Cannot divide by zero"
                    firstNumber = null
                    operator = null
                    resetDisplay = true
                    return
                }
                first / second
            }
            else -> return
        }

        display = formatNumber(result)
        firstNumber = null
        operator = null
        resetDisplay = true
    }

    fun clear() {
        display = "0"
        firstNumber = null
        operator = null
        resetDisplay = false
    }

    fun backspace() {
        if (resetDisplay) return
        display = if (display.length > 1) {
            display.dropLast(1)
        } else {
            "0"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            fontSize = 36.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            maxLines = 2
        )

        @Composable
        fun Key(label: String, onClick: () -> Unit) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp)
                    .padding(4.dp)
            ) {
                Text(label, fontSize = 22.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Key("AC", ::clear)
            Key("⌫", ::backspace)
            Key("÷") { chooseOperator("/") }
            Key("×") { chooseOperator("*") }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Key("7") { pressDigit("7") }
            Key("8") { pressDigit("8") }
            Key("9") { pressDigit("9") }
            Key("−") { chooseOperator("-") }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Key("4") { pressDigit("4") }
            Key("5") { pressDigit("5") }
            Key("6") { pressDigit("6") }
            Key("+") { chooseOperator("+") }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Key("1") { pressDigit("1") }
            Key("2") { pressDigit("2") }
            Key("3") { pressDigit("3") }
            Key("=") { calculate() }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Key("0") { pressDigit("0") }
            Key(".") { pressDigit(".") }
        }
    }
}
