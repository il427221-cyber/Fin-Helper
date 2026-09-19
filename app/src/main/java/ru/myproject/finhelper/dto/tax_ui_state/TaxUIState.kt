package ru.myproject.finhelper.dto.tax_ui_state

data class TaxUIState(
    val sumInput: String = "",
    val deductionInput: String = "",
    val rateInput: String = "",
    val propertyInput: String = "",
    val areaInput: String = "",
    val shareInput: String = "",
    val periodInput: String = "",
    val quantityInput: String = "",
    var activeField: ActiveField = ActiveField.SUM,
    val taxAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
)

enum class ActiveField {
    SUM, DEDUCTION, RATE, PROPERTY, AREA, SHARE, PERIOD, QUANTITY, NONE
}