package ru.myproject.finhelper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.myproject.finhelper.repository.FinRepository

class FinViewModel(private val finRepository: FinRepository): ViewModel() {
    private val _vatAmount = MutableStateFlow<Double>(0.0)
    val vatAmount: StateFlow<Double> = _vatAmount
    private val _totalSum = MutableStateFlow<Double>(0.0)
    val totalWithVat: StateFlow<Double> = _totalSum

    fun calculate_VAT_And_Total(sum: Double, tax: Double) {
        viewModelScope.launch {
            val calculateVATAmount = finRepository.calculateVAT(sum, tax)
            val calculateTotalWithVAT = finRepository.calculateTotalSumWithVAT(sum, tax)

            _vatAmount.value = calculateVATAmount
            _totalSum.value = calculateTotalWithVAT
        }
    }

    fun extract_VAT_And_Total(sum: Double, tax: Double) {
        viewModelScope.launch {
            val extractVATAmount = finRepository.extractVAT(sum, tax)
            val extractTotalWithoutVAT = finRepository.extractSumWithoutVAT(sum, tax)

            _vatAmount.value = extractVATAmount
            _totalSum.value = extractTotalWithoutVAT
        }
    }
}