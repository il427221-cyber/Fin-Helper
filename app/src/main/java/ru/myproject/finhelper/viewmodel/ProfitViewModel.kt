package ru.myproject.finhelper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.myproject.finhelper.R
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField
import ru.myproject.finhelper.dto.profit_ui_state.ProfitUiState
import ru.myproject.finhelper.repository.ProfitRepository

    open class ProfitViewModel(
        application: Application,
        private val profitRepository: ProfitRepository) : AndroidViewModel(application) {

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

                ActiveField.PROFIT -> currentState.copy(
                    profitInput = appendNumberIfMissing(currentState.profitInput + number))

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

                ActiveField.PROFIT -> currentState.copy(
                    profitInput = appendCommaIfMissing(currentState.profitInput))

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

                ActiveField.PROFIT -> currentState.copy(
                    profitInput = deleteOneChar(currentState.profitInput.dropLast(1)))

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
                   (roi > 0.0) ->
                       _uiMessages.emit(application.getString(R.string.investments_are_profitable))
                   (roi == 0.0) ->
                       _uiMessages.emit(application.getString(R.string.investments_are_zero_profitable))
                   (roi < 0.0) ->
                       _uiMessages.emit(application.getString(R.string.unprofitable_investments))
               }
               currentState.copy(roiIndex = roi)
           }
       }
    }

    fun calculateROS() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val income = currentState.incomeInput.toDoubleOrNull() ?: 0.0
                val profit = currentState.profitInput.toDoubleOrNull() ?: 0.0

                if (income == 0.0 || profit == 0.0) {
                    _uiMessages.emit(application.getString(R.string.enter_a_value_greater_than_zero))
                    return@update currentState
                }

                if(profit >= income) {
                    _uiMessages.emit(application.getString(R.string.profit_will_always_be_less_than_revenue))
                    return@update currentState
                }

                val ros = profitRepository.calculateROS(income, profit)
                val result = "%.0f".format(ros)
                _uiMessages.emit(application.getString(R.string._ros, result))
                currentState.copy(rosResult = ros)

            }
        }
    }
}