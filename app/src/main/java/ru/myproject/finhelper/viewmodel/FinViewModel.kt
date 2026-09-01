package ru.myproject.finhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.myproject.finhelper.repository.FinRepository

class FinViewModel(
    private val finRepository: FinRepository): ViewModel() {
    private val _taxAmount = MutableStateFlow (0.0)
    val taxAmount: StateFlow<Double> = _taxAmount
    private val _totalSum = MutableStateFlow (0.0)
    val totalWithTax: StateFlow<Double> = _totalSum

    private val _UIMessages = MutableSharedFlow<String>()
    val uiMessages: SharedFlow<String> = _UIMessages

    fun calculate_VAT_And_Total(sum: Double, tax: Double) {
        if(sum == 0.0) {
            viewModelScope.launch {
                _UIMessages.emit("Введите сумму больше нуля")
            }
            return
        } else {
            viewModelScope.launch {
                val calculateVATAmount = finRepository.calculateVAT(sum, tax)
                val calculateTotalWithVAT = finRepository.calculateTotalSumWithVAT(sum, tax)

                _taxAmount.value = calculateVATAmount
                _totalSum.value = calculateTotalWithVAT
            }
        }
    }
    fun extract_VAT_And_Total(sum: Double, tax: Double) {
        if(sum == 0.0) {
            viewModelScope.launch {
                _UIMessages.emit("Введите сумму больше нуля")
            }
            return
        } else {
            viewModelScope.launch {
                val extractVATAmount = finRepository.extractVAT(sum, tax)
                val extractTotalWithoutVAT = finRepository.extractSumWithoutVAT(sum, tax)

                _taxAmount.value = extractVATAmount
                _totalSum.value = extractTotalWithoutVAT
            }
        }
    }

    fun calculate_PersonalTax(sum: Double, deduction: Double, tax: Double) {
        if(sum == 0.0) {
            viewModelScope.launch {
                _UIMessages.emit("Введите сумму больше нуля")
            }
            return
        }

        if(deduction < sum) {
            viewModelScope.launch {
                val calculatePersonalTax = finRepository.calculatePersonalTax(sum, deduction, tax)
                val extractSumWithoutPersonalTax =
                    finRepository.calculateTotalSum_Without_PersonalTax(sum, deduction, tax)

                _taxAmount.value = calculatePersonalTax
                _totalSum.value = extractSumWithoutPersonalTax
            }
        } else {
            _taxAmount.value = 0.0
            _totalSum.value = 0.0
            viewModelScope.launch {
                _UIMessages.emit("НДФЛ не будет начисляться, т.к. вычеты больше или равны сумме")
            }
        }
    }

    fun calculate_PropertyTax(propertyValue: Double, area: Double, tax: Double,
                              share: Double, period: Double) {
        if(area <= 20.0) {
            viewModelScope.launch {
                _UIMessages.emit("Налог не начисляется, если площадь меньше или равна 20 кв.м")
            }
            return
        }
        if(propertyValue == 0.0 || tax == 0.0 || share == 0.0 || period == 0.0) {
            viewModelScope.launch {
                _UIMessages.emit("Заполните все поля: введите значения больше нуля")
            }
            return
        }
        if(period > 12) {
            viewModelScope.launch {
                _UIMessages.emit("Введите число от 1 до 12")
            }
            return
        }
        viewModelScope.launch {
            val calculatePropertyTax = finRepository.calculatePropertyTax(propertyValue,area, tax, share, period)
            _taxAmount.value = calculatePropertyTax
        }
    }
    fun clearOutputFields() {
        _taxAmount.value = 0.0
        _totalSum.value = 0.0
    }
}