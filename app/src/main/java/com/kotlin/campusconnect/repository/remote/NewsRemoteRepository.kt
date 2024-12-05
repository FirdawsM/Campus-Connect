package com.kotlin.campusconnect.repository.remote

import com.kotlin.campusconnect.api.RetrofitInstance

class NewsRemoteRepository {

    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun search(query: String, pageNumber: Int) =
        RetrofitInstance.api.search(query, pageNumber)

}