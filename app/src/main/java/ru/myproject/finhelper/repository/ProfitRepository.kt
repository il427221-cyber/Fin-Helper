package ru.myproject.finhelper.repository

interface ProfitRepository {
    fun calculateROI(expenses: Double, income: Double): Double
    fun calculateROS(income: Double, profit: Double): Double

    fun calculateMonthPayment(sum: Double, rate: Double, period: Double): Double
}