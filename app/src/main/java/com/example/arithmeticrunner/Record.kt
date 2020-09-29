package com.example.arithmeticrunner

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Record(
    var digit1: Int = 1,
    var digit2: Int = 1,
    var opStr: String = Operator.ADD.opStr,
    var problemsNum: Int = 20,
    var timerStr: String = "99'99'99"
) : Parcelable, RealmObject() {
}