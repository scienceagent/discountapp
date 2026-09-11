package com.example.model

data class CalculationRecord(
  val initialPrice: Double,
  val discountPercent: Double,
  val discountAmount: Double,
  val finalPrice: Double,
  val currency: String,
)
