package ru.myproject.finhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField
import ru.myproject.finhelper.dto.profit_ui_state.ProfitUiState
import ru.myproject.finhelper.repository.ProfitRepository

open class ProfitViewModel(
    private val profitRepository: ProfitRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfitUiState())
    open val uiState: StateFlow<ProfitUiState> = _uiState.asStateFlow()

    private val _UIMessages = MutableSharedFlow<String>()
    val uiMessages: SharedFlow<String> = _UIMessages

    // Методы для обновления состояния
    open fun updateIncomeInput(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(incomeInput = newValue)
        }
    }

    open fun updateExpensesInput(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(expensesInput = newValue)
        }
    }

    open fun setActiveField(field: ActiveField) {
        _uiState.update { currentState ->
            currentState.copy(activeField = field)
        }
    }
    open fun appendNumberToActiveField(number: String) {
        _uiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.INCOME -> {
                    val currentIncome = currentState.incomeInput
                    currentState.copy(incomeInput = currentIncome + number)
                }
                ActiveField.EXPENSES -> {
                    val currentExpenses = currentState.expensesInput
                    currentState.copy(expensesInput = currentExpenses + number)
                }
                ActiveField.NONE -> currentState // Ничего не делаем, если поле не активно
            }
        }
    }
    open fun appendCommaToActiveField() {
        _uiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.INCOME -> {
                    val currentIncome = currentState.incomeInput
                    if (!currentIncome.contains(".")) { // Добавляем запятую только если её нет
                        currentState.copy(incomeInput = "$currentIncome.")
                    } else currentState
                }
                ActiveField.EXPENSES -> {
                    val currentExpenses = currentState.expensesInput
                    if (!currentExpenses.contains(".")) {
                        currentState.copy(expensesInput = "$currentExpenses.")
                    } else currentState
                }
                ActiveField.NONE -> currentState
            }
        }
    }
    open fun deleteLastCharFromActiveField() {
        _uiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.INCOME -> {
                    val currentIncome = currentState.incomeInput
                    if (currentIncome.isNotEmpty()) {
                        currentState.copy(incomeInput = currentIncome.dropLast(1))
                    } else currentState
                }
                ActiveField.EXPENSES -> {
                    val currentExpenses = currentState.expensesInput
                    if (currentExpenses.isNotEmpty()) {
                        currentState.copy(expensesInput = currentExpenses.dropLast(1))
                    } else currentState
                }
                ActiveField.NONE -> currentState
            }
        }
    }
    open fun clearInputFields() {
        _uiState.update { currentState -> currentState.copy(incomeInput = "", expensesInput = "") }
    }
   open fun calculateROI() {
       viewModelScope.launch {
           _uiState.update { currentState ->
               val income = currentState.incomeInput.toDoubleOrNull() ?: 0.0
               val expenses = currentState.expensesInput.toDoubleOrNull() ?: 0.0
               val roi = profitRepository.calculateROI(expenses, income)
               when {
                   (roi > 0.0) -> _UIMessages.emit("Инвестиции приносят прибыль")
                   (roi == 0.0) -> _UIMessages.emit("Инвестиции окупились, но прибыли не принесли")
                   (roi < 0.0) -> _UIMessages.emit("Инвестиции убыточны")

               }
               currentState.copy(roiIndex = roi)
           }
       }
    }
    fun clearOutputFields() {
        _uiState.value = ProfitUiState(roiIndex = 0.0) // Сбрасываем к начальному состоянию
    }
}