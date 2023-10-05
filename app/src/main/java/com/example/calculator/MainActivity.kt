package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {//test
    private lateinit var tv_cal: TextView
    private lateinit var tv_preview: TextView

    fun appendText(addT: String) {
        val currentText = tv_cal.text.toString()
        tv_cal.text = currentText + addT
    }

    fun clearText() {
        tv_cal.text = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_cal = findViewById(R.id.tv_cal)
        tv_preview = findViewById(R.id.tv_preview)
        val btn_0: Button = findViewById(R.id.btn_0)
        val btn_1: Button = findViewById(R.id.btn_1)
        val btn_2: Button = findViewById(R.id.btn_2)
        val btn_3: Button = findViewById(R.id.btn_3)
        val btn_4: Button = findViewById(R.id.btn_4)
        val btn_5: Button = findViewById(R.id.btn_5)
        val btn_6: Button = findViewById(R.id.btn_6)
        val btn_7: Button = findViewById(R.id.btn_7)
        val btn_8: Button = findViewById(R.id.btn_8)
        val btn_9: Button = findViewById(R.id.btn_9)
        val btn_minus: Button = findViewById(R.id.btn_minus)
        val btn_point: Button = findViewById(R.id.btn_point)
        val btn_plus: Button = findViewById(R.id.btn_plus)
        val btn_sub: Button = findViewById(R.id.btn_sub)
        val btn_mul: Button = findViewById(R.id.btn_mul)
        val btn_div: Button = findViewById(R.id.btn_div)
        val btn_rest: Button = findViewById(R.id.btn_rest)
        val btn_equal: Button = findViewById(R.id.btn_equal)
        val btn_clear: Button = findViewById(R.id.btn_clear)
        val btn_del: Button = findViewById(R.id.btn_del)

        btn_0.setOnClickListener { appendText("0") }
        btn_1.setOnClickListener { appendText("1") }
        btn_2.setOnClickListener { appendText("2") }
        btn_3.setOnClickListener { appendText("3") }
        btn_4.setOnClickListener { appendText("4") }
        btn_5.setOnClickListener { appendText("5") }
        btn_6.setOnClickListener { appendText("6") }
        btn_7.setOnClickListener { appendText("7") }
        btn_8.setOnClickListener { appendText("8") }
        btn_9.setOnClickListener { appendText("9") }
        btn_plus.setOnClickListener { appendText("+") }
        btn_sub.setOnClickListener { appendText("-") }
        btn_mul.setOnClickListener { appendText("x") }
        btn_div.setOnClickListener { appendText("/") }
        btn_rest.setOnClickListener { appendText("%") }
        btn_point.setOnClickListener { appendText(".") }
        btn_del.setOnClickListener {
            val currentText = tv_cal.text.toString()
            if (currentText.isNotEmpty()) {
                val updatedText = currentText.substring(0, currentText.length - 1)
                tv_cal.text = updatedText
            }
        }
        btn_minus.setOnClickListener {appendText("-")
        }
        btn_clear.setOnClickListener { clearText() }

        tv_cal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                try {
                    val result = calculateExpression((tv_cal.text).toString())
                    tv_preview.text = result.toString()
                } catch (e: Exception) {
                    tv_preview.text = ""
                }
            }
        })
    }

fun calculateExpression(expression: String): Any {
    val operators = mutableListOf<Char>()
    val operands = mutableListOf<Double>()
    var currentNumber = ""
    var result: Double = 0.0

    for (char in expression) {
        if (char.isDigit() || char == '.') {
            currentNumber += char
        } else if (char in setOf('+', '-', 'x', '/', '%')) {
            if (currentNumber.isNotEmpty()) {
                operands.add(currentNumber.toDouble())
                currentNumber = ""
            }
            while (operators.isNotEmpty() && hasPrecedence(char, operators.last())) {
                val operator = operators.removeAt(operators.size - 1)
                if (operands.size < 2) {
                    throw IllegalArgumentException("Not enough operands for operator: $operator")
                }
                val operand2 = operands.removeAt(operands.size - 1)
                val operand1 = operands.removeAt(operands.size - 1)
                operands.add(performOperation(operand1, operand2, operator))
            }
            operators.add(char)
        }
    }

    if (currentNumber.isNotEmpty()) {
        operands.add(currentNumber.toDouble())
    }

    while (operators.isNotEmpty()) {
        val operator = operators.removeAt(operators.size - 1)
        if (operands.size < 2) {
            throw IllegalArgumentException("Not enough operands for operator: $operator")
        }
        val operand2 = operands.removeAt(operands.size - 1)
        val operand1 = operands.removeAt(operands.size - 1)
        operands.add(performOperation(operand1, operand2, operator))
    }

    result = operands.first()
    if ((result - result.toInt()) == 0.0) {
        return result.toInt()
    } else {
        return result
    }
}

    fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if ((op1 == 'x' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')) return false
        return true
    }

    fun performOperation(operand1: Double, operand2: Double, operator: Char): Double {
        when (operator) {
            '+' -> return operand1 + operand2
            '-' -> return operand1 - operand2
            'x' -> return operand1 * operand2
            '/' -> {
                if (operand2 == 0.0) {
                    throw ArithmeticException("Division by zero")
                }
                return operand1 / operand2
            }
            '%' -> return operand1 % operand2
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }
}
