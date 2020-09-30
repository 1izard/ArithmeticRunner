package com.example.arithmeticrunner

fun Int.fmt(digit: Int, padding: Int? = null): String {
    val specifier = "%${padding?.toString() ?: ""}${digit}d"
    return specifier.format(this)
}

fun Long.fmt(digit: Int, padding: Int? = null): String {
    val specifier = "%${padding?.toString() ?: ""}${digit}d"
    return specifier.format(this)
}

fun Int.fmtQNo(digit: Int): String {
    return "Q ${this.fmt(digit, 0)}"
}
