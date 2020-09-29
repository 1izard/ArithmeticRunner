package com.example.arithmeticrunner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class Operator(val opStr: String) : Parcelable {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    companion object {
        fun s2o(opStr: String) = when (opStr) {
            ADD.opStr -> ADD
            SUB.opStr -> SUB
            MUL.opStr -> MUL
            DIV.opStr -> DIV
            else -> ADD
        }
    }
}