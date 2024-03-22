package com.crypto.ticker.data.repository.coinsList

import com.crypto.ticker.api.ApiInterface
import com.crypto.ticker.api.BaseRemoteDataSource
import javax.inject.Inject
import com.crypto.ticker.api.Result
import com.crypto.ticker.api.models.Coin

class CoinsListRemoteDataSource @Inject constructor(private val service: ApiInterface) :
    BaseRemoteDataSource() {

    suspend fun coinsList(targetCur: String): Result<List<Coin>> =
        getResult {
            service.coinsList(targetCur)
        }
}