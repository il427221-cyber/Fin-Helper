package ru.myproject.finhelper.viewmodel

fun appendNumberIfMissing(currentValue: String): String = currentValue
fun appendCommaIfMissing(currentValue: String): String {
    return if (!currentValue.contains(".")) {
        "$currentValue."
    } else {
        currentValue
    }
}
fun deleteOneChar(currentValue: String): String {
    return if (currentValue.isNotEmpty()) {
        currentValue
    } else {
        ""
    }
}