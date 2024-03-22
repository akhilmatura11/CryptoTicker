package com.crypto.ticker.api.models



data class HistoricalPriceResponse(
    val prices: List<DoubleArray>
)