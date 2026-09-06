package ru.myproject.finhelper.repository

class TaxRepositoryImpl: TaxRepository {
    override fun calculateVAT(sum: Double, tax: Double): Double {
        val taxValue = tax / 100
        return sum * taxValue
    }

    override fun calculateTotalSumWithVAT(sum: Double, tax: Double): Double {
        val vatAmount = calculateVAT(sum, tax)
        return sum + vatAmount
    }

    override fun extractVAT(sum: Double, tax: Double) = sum * tax/(tax + 100)


    override fun extractSumWithoutVAT(sum: Double, tax: Double): Double {
        val vatAmount = extractVAT(sum,tax)
        return sum - vatAmount
    }

    override fun calculatePersonalTax(sum: Double, deduction: Double, tax: Double): Double {
        val taxValue = tax / 100
        return (sum - deduction) * taxValue
    }

    override fun calculateTotalSum_Without_PersonalTax(sum: Double, deduction: Double,tax: Double): Double {
        val vatAmount = calculatePersonalTax(sum,deduction,tax)
        return sum - vatAmount
    }

    override fun calculatePropertyTax(propertyValue: Double, area: Double, tax: Double,
                                      share: Double, period: Double): Double {
       val taxableArea = area - 20.0
       val cadastralValue = propertyValue / area
       val taxableAreaValue = cadastralValue * taxableArea
       val totalTax = taxableAreaValue * tax / 100.0 * share / 100.0 * period / 12.0
       return totalTax
    }
}