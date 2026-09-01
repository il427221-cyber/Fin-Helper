package ru.myproject.finhelper.dto.profit_ui_state

data class ProfitUiState(
    val incomeInput: String = "",
    val expensesInput: String = "",
    var activeField: ActiveField = ActiveField.INCOME,
    val roiIndex: Double = 0.0
)

enum class ActiveField {
    INCOME, EXPENSES, NONE
}