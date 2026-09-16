package ru.myproject.finhelper.dto.profit_ui_state

data class ProfitUiState(
    val incomeInput: String = "",
    val expensesInput: String = "",
    val profitInput: String = "",
    val sumInput: String = "",
    val rateInput: String = "",
    val periodInput: String = "",
    var activeField: ActiveField = ActiveField.INCOME,
    val roiIndex: Double = 0.0,
    val rosResult: Double = 0.0,
    val monthPayment: Double = 0.0
)

enum class ActiveField {
    INCOME, EXPENSES, PROFIT, SUM, RATE, PERIOD
}