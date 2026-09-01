package ru.myproject.finhelper.repository

interface ProfitRepository {

    fun calculateROI(expenses: Double, income: Double): Double
}