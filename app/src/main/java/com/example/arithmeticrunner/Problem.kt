package com.example.arithmeticrunner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.math.pow
import kotlin.random.Random

@Parcelize
class Problem(
    val no: Int,
    val digit1: Int,
    val digit2: Int,
    val operator: Operator
) : Parcelable {
    val operand1 = setOperand(digit1)
    val operand2 = setOperand(digit2)
    val answer = when (operator) {
        Operator.ADD -> operand1 + operand2
        Operator.SUB -> operand1 - operand2
        Operator.MUL -> operand1 * operand2
        Operator.DIV -> operand1 / operand2
    }
    var correct = true

    private fun setOperand(digit: Int) =
        Random.nextInt(10.0.pow(digit - 1).toInt(), 10.0.pow(digit).toInt())
}