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
import ru.myproject.finhelper.dto.tax_ui_state.ActiveField
import ru.myproject.finhelper.dto.tax_ui_state.TaxUIState
import ru.myproject.finhelper.repository.TaxRepository

open class TaxViewModel(
    application: Application,
    private val taxRepository: TaxRepository): AndroidViewModel(application) {
    private val _taxUiState = MutableStateFlow(TaxUIState())
    open val taxUiState: StateFlow<TaxUIState> = _taxUiState.asStateFlow()
    private val _UIMessages = MutableSharedFlow<String>()
    val uiMessages: SharedFlow<String> = _UIMessages
    private val emptyState = TaxUIState()

    fun updateInput(newValue: String) {
        _taxUiState.update { currentState ->
            currentState.copy(
                sumInput = newValue,
                deductionInput = newValue,
                rateInput = newValue,
                propertyInput = newValue,
                areaInput = newValue,
                shareInput = newValue,
                periodInput = newValue
                )
        }
    }
    fun setActiveField(field: ActiveField) {
        _taxUiState.update { currentState ->
            currentState.copy(activeField = field)
        }
    }
    fun appendNumberToActiveField(number: String) {
        _taxUiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.SUM -> currentState.copy(
                    sumInput = appendNumberIfMissing(currentState.sumInput + number))

                ActiveField.DEDUCTION -> currentState.copy(
                    deductionInput = appendNumberIfMissing(currentState.deductionInput + number))

                ActiveField.RATE -> currentState.copy(
                    rateInput = appendNumberIfMissing(currentState.rateInput + number))

                ActiveField.PROPERTY -> currentState.copy(
                    propertyInput = appendNumberIfMissing(currentState.propertyInput + number))

                ActiveField.AREA -> currentState.copy(
                    areaInput = appendNumberIfMissing(currentState.areaInput + number))

                ActiveField.SHARE -> currentState.copy(
                    shareInput = appendNumberIfMissing(currentState.shareInput + number))

                ActiveField.PERIOD -> currentState.copy(
                    periodInput = appendNumberIfMissing(currentState.periodInput + number))

                ActiveField.NONE -> currentState
            }
        }
    }
    fun appendCommaToActiveField() {
        _taxUiState.update { currentState ->
            when(currentState.activeField) {
                ActiveField.SUM -> currentState.copy(
                    sumInput = appendCommaIfMissing(currentState.sumInput))

                ActiveField.DEDUCTION -> currentState.copy(
                    deductionInput = appendCommaIfMissing(currentState.deductionInput))

                ActiveField.RATE -> currentState.copy(
                    rateInput = appendCommaIfMissing(currentState.rateInput))

                ActiveField.PROPERTY -> currentState.copy(
                    propertyInput = appendCommaIfMissing(currentState.propertyInput))

                ActiveField.AREA -> currentState.copy(
                    areaInput = appendCommaIfMissing(currentState.areaInput))

                ActiveField.SHARE -> currentState.copy(
                    shareInput = appendCommaIfMissing(currentState.shareInput))

                ActiveField.PERIOD -> currentState.copy(
                    periodInput = appendCommaIfMissing(currentState.periodInput))

                ActiveField.NONE -> currentState
            }
        }
    }
    fun deleteLastCharFromActiveField() {
        _taxUiState.update { currentState ->
            when (currentState.activeField) {
                ActiveField.SUM -> currentState.copy(
                    sumInput = deleteOneChar(currentState.sumInput.dropLast(1)))

                ActiveField.DEDUCTION -> currentState.copy(
                    deductionInput = deleteOneChar(currentState.deductionInput.dropLast(1)))

                ActiveField.RATE -> currentState.copy(
                    rateInput = deleteOneChar(currentState.rateInput.dropLast(1)))

                ActiveField.PROPERTY -> currentState.copy(
                    propertyInput = deleteOneChar(currentState.propertyInput.dropLast(1)))

                ActiveField.AREA -> currentState.copy(
                    areaInput = deleteOneChar(currentState.areaInput.dropLast(1)))

                ActiveField.SHARE -> currentState.copy(
                    shareInput = deleteOneChar(currentState.shareInput.dropLast(1)))

                ActiveField.PERIOD -> currentState.copy(
                    periodInput = deleteOneChar(currentState.periodInput.dropLast(1)))

                ActiveField.NONE -> currentState
            }
        }
    }
    fun clearAllFields() {
        _taxUiState.value = emptyState
    }
    fun calculate_VAT_And_Total() {
        viewModelScope.launch {
            _taxUiState.update { currentState -> // Лямбда для update - это suspend-контекст
                val sum = currentState.sumInput.toDoubleOrNull() ?: 0.0
                val tax = currentState.rateInput.toDoubleOrNull() ?: 0.0

                if (sum <= 0.0) {
                    _UIMessages.emit(application.getString(R.string.enter_a_value_greater_than_zero))
                    return@update currentState
                }

                val VATAmount = taxRepository.calculateVAT(sum, tax)
                val totalSum = taxRepository.calculateTotalSumWithVAT(sum, tax)

                currentState.copy(taxAmount = VATAmount, totalAmount = totalSum)
            }
        }
    }
    fun extract_VAT_And_Total() {
        viewModelScope.launch {
            _taxUiState.update { currentState ->
                val sum = currentState.sumInput.toDoubleOrNull() ?: 0.0
                val tax = currentState.rateInput.toDoubleOrNull() ?: 0.0

                if (sum <= 0.0) {
                    _UIMessages.emit(application.getString(R.string.enter_a_value_greater_than_zero))
                    return@update currentState
                }

                val VATAmount = taxRepository.extractVAT(sum, tax)
                val totalSum = taxRepository.extractSumWithoutVAT(sum, tax)

                currentState.copy(taxAmount = VATAmount, totalAmount = totalSum)
            }
        }
    }
    fun calculate_PersonalTax() {
        viewModelScope.launch {
            _taxUiState.update { currentState ->
                val sum = currentState.sumInput.toDoubleOrNull() ?: 0.0
                val deduction = currentState.deductionInput.toDoubleOrNull() ?: 0.0
                val tax = currentState.rateInput.toDoubleOrNull() ?: 0.0

                if (sum <= 0.0) {
                    _UIMessages.emit(application.getString(R.string.enter_a_value_greater_than_zero))
                    return@update currentState
                }

                if (deduction < sum) {
                    val personalTax = taxRepository.calculatePersonalTax(sum, deduction,tax)
                    val totalSumWithoutTax = taxRepository.calculateTotalSum_Without_PersonalTax(sum, deduction,tax)
                    currentState.copy(taxAmount = personalTax, totalAmount = totalSumWithoutTax)
                } else {
                    _UIMessages.emit(application.getString(R.string.deduction_condition))
                    currentState
                }
            }
        }
    }

    fun calculate_PropertyTax() {
        viewModelScope.launch {
            _taxUiState.update { currentState ->
                val property = currentState.propertyInput.toDoubleOrNull() ?: 0.0
                val area = currentState.areaInput.toDoubleOrNull() ?: 0.0
                val tax = currentState.rateInput.toDoubleOrNull() ?: 0.0
                val share = currentState.shareInput.toDoubleOrNull() ?: 0.0
                val period = currentState.periodInput.toDoubleOrNull() ?: 0.0

                if(area <= 20.0) {
                    _UIMessages.emit(application.getString(R.string.area_condition))
                    return@update currentState
                }

                if(property == 0.0 || tax == 0.0 || share == 0.0 || period == 0.0) {
                    _UIMessages.emit(application.getString(R.string.filling_condition))
                    return@update currentState
                }

                if(period > 12) {
                    _UIMessages.emit(application.getString(R.string.number_condition))
                    return@update currentState
                }

                val propertyTax = taxRepository.calculatePropertyTax(
                    property, area,
                    tax, share, period)
                currentState.copy(taxAmount = propertyTax)
            }
        }
    }

    fun simpleDeposit() {
        viewModelScope.launch {
            _taxUiState.update { currentState ->
                val sum = currentState.sumInput.toDoubleOrNull() ?: 0.0
                val rate = currentState.rateInput.toDoubleOrNull() ?: 0.0
                val period = currentState.periodInput.toDoubleOrNull() ?: 0.0

                if(sum == 0.0 || rate == 0.0 || period == 0.0) {
                    _UIMessages.emit(application.getString(R.string.filling_condition))
                    return@update currentState
                }

                val totalSum = taxRepository.simpleDeposit(sum, rate, period)
                currentState.copy(totalAmount = totalSum)
            }
        }
    }

    fun capitalizedDeposit() {
        viewModelScope.launch {
            _taxUiState.update { currentState ->
                val sum = currentState.sumInput.toDoubleOrNull() ?: 0.0
                val rate = currentState.rateInput.toDoubleOrNull() ?: 0.0
                val period = currentState.periodInput.toDoubleOrNull() ?: 0.0

                if(sum == 0.0 || rate == 0.0 || period == 0.0) {
                    _UIMessages.emit(application.getString(R.string.filling_condition))
                    return@update currentState
                }

                val totalSum = taxRepository.capitalizedDeposit(sum, rate, period)
                currentState.copy(totalAmount = totalSum)
            }
        }
    }

}