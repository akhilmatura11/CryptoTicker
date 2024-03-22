package com.crypto.ticker.data.repository.projectProfile

import com.crypto.ticker.api.ApiInterface
import com.crypto.ticker.api.BaseRemoteDataSource
import javax.inject.Inject
import com.crypto.ticker.api.Result
import com.crypto.ticker.api.models.HistoricalPriceResponse

class ProjectProfileRemoteDataSource @Inject constructor(private val service: ApiInterface) : BaseRemoteDataSource(){

    //fetch historical price from backend
    suspend fun historicalPrice(symbol: String, targetCurrency: String, days: Int = 30): Result<HistoricalPriceResponse> =
        getResult { service.historicalPrice(symbol, targetCurrency, days) }

}