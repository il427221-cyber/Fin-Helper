package ru.myproject.finhelper.repository

interface FinRepository {
    fun calculateVAT(sum: Double,tax: Double): Double
    fun calculateTotalSumWithVAT(sum: Double,tax: Double): Double
    fun extractVAT(sum: Double,tax: Double): Double
    fun extractSumWithoutVAT(sum: Double,tax: Double): Double
}