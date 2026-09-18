package ru.myproject.finhelper.repository

interface TaxRepository {
    fun calculateVAT(sum: Double,tax: Double): Double
    fun calculateTotalSumWithVAT(sum: Double,tax: Double): Double
    fun extractVAT(sum: Double,tax: Double): Double
    fun extractSumWithoutVAT(sum: Double,tax: Double): Double

    fun calculatePersonalTax(sum:Double,deduction:Double, tax: Double): Double

    fun calculateTotalSum_Without_PersonalTax(sum: Double,deduction:Double,tax: Double): Double

    fun calculatePropertyTax(
        propertyValue: Double,
        area: Double,
        tax: Double,
        share: Double,
        period: Double): Double

    fun simpleDeposit(sum:Double, rate: Double, period: Double): Double

    fun capitalizedDeposit(sum:Double, rate: Double, period: Double): Double
}