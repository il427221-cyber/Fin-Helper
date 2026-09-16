package ru.myproject.finhelper.repository

import kotlin.math.pow

class ProfitRepositoryImpl: ProfitRepository {

    override fun calculateROI(expenses: Double, income: Double): Double {
        val profit = income - expenses
        return profit / expenses * 100.0
    }

    override fun calculateROS(income: Double, profit: Double): Double {
        return profit / income * 100.0
    }

    override fun calculateMonthPayment(sum: Double, rate: Double, period: Double): Double {
        val monthRate = rate/ 12/ 100
        val base = (1 + monthRate)
        val result = base.pow(period)
        val payment = sum * monthRate * result/ (result - 1)
        return payment
    }
}