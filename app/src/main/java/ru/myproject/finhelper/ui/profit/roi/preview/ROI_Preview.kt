package ru.myproject.finhelper.ui.profit.roi.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField
import ru.myproject.finhelper.dto.profit_ui_state.ProfitUiState
import ru.myproject.finhelper.repository.ProfitRepository
import ru.myproject.finhelper.ui.profit.roi.ShowROI
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
    ProfitViewModel(PreviewMockProfitRepository()) {
    override val uiState: StateFlow<ProfitUiState> = MutableStateFlow(initialState).asStateFlow()

    // Все методы переопределяем, чтобы они ничего не делали в Preview, так как нам нужна только отрисовка.
    override fun updateIncomeInput(newValue: String) {}
    override fun updateExpensesInput(newValue: String) {}
    override fun setActiveField(field: ActiveField) {}
    override fun appendNumberToActiveField(number: String) {}
    override fun appendCommaToActiveField() {}
    override fun deleteLastCharFromActiveField() {}
    override fun clearInputFields() {}
    override fun calculateROI() {}
}

// 3. Создаём Mock-фабрику, которая будет создавать Mock-ViewModel для Preview.
private class PreviewMockViewModelFactory(private val initialState: ProfitUiState) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfitViewModel::class.java)) {
            return PreviewMockProfitViewModel(initialState) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Preview(showBackground = true)
@Composable
fun ShowROIPreview() {
    // Определяем состояние, которое хотим видеть в этом конкретном Preview
    val initialState = ProfitUiState(
        incomeInput = "100000.00",
        expensesInput = "80000.00",
        activeField = ActiveField.INCOME,
        roiIndex = 125.0
    )
    val mockFactory = PreviewMockViewModelFactory(initialState)

    ShowROI(
        onShowBottomBar = {},
        modifier = Modifier,
        profitViewModelFactory = mockFactory
    )
}
