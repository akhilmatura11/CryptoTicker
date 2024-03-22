package com.crypto.ticker.data.repository.projectProfile

import com.crypto.ticker.data.local.database.CoinsDatabase
import javax.inject.Inject

class ProjectProfileDataSource @Inject constructor(private val db: CoinsDatabase){

    fun projectBySymbol(symbol: String) = db.coinsListDao().projectLiveDataFromSymbol(symbol)

}