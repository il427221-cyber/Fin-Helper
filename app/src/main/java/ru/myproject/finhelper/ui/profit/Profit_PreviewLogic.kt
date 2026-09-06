package ru.myproject.finhelper.ui.profit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.myproject.finhelper.dto.profit_ui_state.ProfitUiState
import ru.myproject.finhelper.repository.ProfitRepository
import ru.myproject.finhelper.viewmodel.ProfitViewModel

// 1. Создаём Mock-реализацию ProfitRepository для Mock-ViewModel для Preview.
private class PreviewMockProfitRepository : ProfitRepository {
    override fun calculateROI(expenses: Double, income: Double): Double {
        return 123.45 // Просто фиксированное значение для Preview
    }
}
/* 2. Создаём Mock-ViewModel для Preview
    Эта ViewModel будет возвращать конкретное состояние,
    которое будет в Preview.
 */
private class PreviewMockProfitViewModel(initialState: ProfitUiState) :
    ProfitViewModel(PreviewMockProfitRepository())

// 3. Создаём Mock-фабрику, которая будет создавать Mock-ViewModel для Preview.
class PreviewProfitViewModelFactory(private val initialState: ProfitUiState) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfitViewModel::class.java)) {
            return PreviewMockProfitViewModel(initialState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}