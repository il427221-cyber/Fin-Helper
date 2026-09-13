package ru.myproject.finhelper.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.myproject.finhelper.repository.TaxRepository
import ru.myproject.finhelper.repository.ProfitRepository

/*
 С помощью FinViewModelFactory ViewModel автоматически создается и управляется Jetpack Compose
 Также это нужно, т.к. репозиторий является аргументом ViewModel
 */
class FinViewModelFactory(
    private val application: Application,
    private val taxRepository: TaxRepository,
    private val profitRepository: ProfitRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaxViewModel:: class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaxViewModel(application,taxRepository) as T
        }
        if(modelClass.isAssignableFrom(ProfitViewModel:: class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfitViewModel(application,profitRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : $modelClass")
    }
}