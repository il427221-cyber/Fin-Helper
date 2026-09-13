package ru.myproject.finhelper.repository

class ProfitRepositoryImpl: ProfitRepository {

    override fun calculateROI(expenses: Double, income: Double): Double {
        val profit = income - expenses
        return profit / expenses * 100.0
    }

    override fun calculateROS(income: Double, profit: Double): Double {
        return profit / income * 100.0
    }
}