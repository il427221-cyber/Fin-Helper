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

open class ProfitViewModel(private val profitRepository: ProfitRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfitUiState())
    open val uiState: StateFlow<ProfitUiState> = _uiState.asStateFlow()

    private val _uiMessages = MutableSharedFlow<String>()
    val uiMessages: SharedFlow<String> = _uiMessages

    private val emptyState = ProfitUiState()

    // Методы для обновления состояния
    fun updateInput(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(incomeInput = newValue, expensesInput = newValue)
        }
    }

    fun setActiveField(field: ActiveField) {
        _uiState.update { currentState ->
            currentState.copy(activeField = field)
        }
    }
    fun appendNumberToActiveField(number: String) {
        _uiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.INCOME -> currentState.copy(
                    incomeInput = appendNumberIfMissing(currentState.incomeInput + number))

                ActiveField.EXPENSES -> currentState.copy(
                    expensesInput = appendNumberIfMissing(currentState.expensesInput + number))

                else -> currentState // Ничего не делаем, если поле не активно
            }
        }
    }
    fun appendCommaToActiveField() {
        _uiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.INCOME -> currentState.copy(
                    incomeInput = appendCommaIfMissing(currentState.incomeInput))

                ActiveField.EXPENSES -> currentState.copy(
                    expensesInput = appendCommaIfMissing(currentState.expensesInput))

                else -> currentState
            }
        }
    }
    fun deleteLastCharFromActiveField() {
        _uiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.INCOME -> currentState.copy(
                    incomeInput = deleteOneChar(currentState.incomeInput.dropLast(1)))

                ActiveField.EXPENSES -> currentState.copy(
                    expensesInput = deleteOneChar(currentState.expensesInput.dropLast(1)))

                else -> currentState
            }
        }
    }
    fun clearAllFields() { _uiState.value = emptyState }
    fun calculateROI() {
       viewModelScope.launch {
           _uiState.update { currentState ->
               val income = currentState.incomeInput.toDoubleOrNull() ?: 0.0
               val expenses = currentState.expensesInput.toDoubleOrNull() ?: 0.0
               val roi = profitRepository.calculateROI(expenses, income)
               when {
                   (roi > 0.0) -> _uiMessages.emit("Инвестиции приносят прибыль")
                   (roi == 0.0) -> _uiMessages.emit("Инвестиции окупились, но прибыли не принесли")
                   (roi < 0.0) -> _uiMessages.emit("Инвестиции убыточны")
               }
               currentState.copy(roiIndex = roi)
           }
       }
    }
}