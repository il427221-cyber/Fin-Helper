package ru.myproject.finhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.myproject.finhelper.repository.FinRepository

class FinViewModel(private val finRepository: FinRepository): ViewModel() {
    private val _vatAmount = MutableStateFlow<Double>(0.0)
    val vatAmount: StateFlow<Double> = _vatAmount
    private val _totalSum = MutableStateFlow<Double>(0.0)
    val totalWithVat: StateFlow<Double> = _totalSum

    private val _sumInput = MutableStateFlow("")
    val sumInput: StateFlow<String> = _sumInput

    private val _deductionInput = MutableStateFlow("")
    val deductionInput: StateFlow<String> = _deductionInput
    private val _taxInput = MutableStateFlow("")
    val taxInput: StateFlow<String> = _taxInput

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

                _vatAmount.value = calculateVATAmount
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

                _vatAmount.value = extractVATAmount
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
        } else {
        viewModelScope.launch {
            val calculatePersonalTax = finRepository.calculatePersonalTax(sum, deduction, tax)
            val extractSumWithoutPersonalTax =
                finRepository.calculateTotalSum_Without_PersonalTax(sum, deduction, tax)

            _vatAmount.value = calculatePersonalTax
            _totalSum.value = extractSumWithoutPersonalTax
        }
        }
    }
    fun clearOutputFields() {
        _vatAmount.value = 0.0
        _totalSum.value = 0.0
    }
}