package com.example.arithmeticrunner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProblemsConfig(
    val digit1: Int,
    val digit2: Int,
    val operator: Operator,
    val problemsNum: Int
) : Parcelable {
}