package ru.myproject.finhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.myproject.finhelper.repository.FinRepository

/*
 С помощью FinViewModelFactory ViewModel автоматически создается и управляется Jetpack Compose
 Также это нужно, т.к. репозиторий является аргументом ViewModel
 */

class FinViewModelFactory(private val finRepository: FinRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FinViewModel:: class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinViewModel(finRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}