package ru.myproject.finhelper.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.myproject.finhelper.dto.tax_ui_state.TaxUIState
import ru.myproject.finhelper.repository.TaxRepository
import ru.myproject.finhelper.viewmodel.TaxViewModel

private class PreviewMockTaxRepository: TaxRepository {
    override fun calculateVAT(sum: Double, tax: Double): Double { return 123.45 }
    override fun calculateTotalSumWithVAT(sum: Double, tax: Double): Double { return 123.45 }
    override fun extractVAT(sum: Double, tax: Double): Double { return 123.45 }
    override fun extractSumWithoutVAT(sum: Double, tax: Double): Double { return 123.45 }
    override fun calculatePersonalTax(sum: Double, deduction: Double, tax: Double): Double { return 123.45 }
    override fun calculateTotalSum_Without_PersonalTax(sum: Double, deduction: Double, tax: Double): Double { return 123.45 }
    override fun calculatePropertyTax(
        propertyValue: Double,
        area: Double,
        tax: Double,
        share: Double,
        period: Double
    ): Double {
       return 123.45
    }

    override fun simpleDeposit(
        sum: Double,
        rate: Double,
        period: Double
    ): Double {
        return 123.45
    }

    override fun capitalizedDeposit(
        sum: Double,
        rate: Double,
        period: Double
    ): Double {
        return 123.45
    }

    override fun complexDeposit(
        sum: Double,
        rate: Double,
        period: Double,
        quantity: Double
    ): Double {
        return 123.45
    }
}
private class PreviewMockTaxViewModel(
    application: Application,
    initialState: TaxUIState): TaxViewModel(
    application,PreviewMockTaxRepository())
class PreviewTaxViewModelFactory(
    private val application: Application,
    private val initialState: TaxUIState): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaxViewModel::class.java)) {
            return PreviewMockTaxViewModel(application,initialState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}