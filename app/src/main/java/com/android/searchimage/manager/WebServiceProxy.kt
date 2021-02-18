package com.android.searchimage.manager

import com.android.searchimage.model.Hit
import com.android.searchimage.repository.LoyaltyWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServiceProxy {

    @GET("api/")
    fun search(@Query("key") key: String?, @Query("q") query: String?, @Query("image_type") imageType: String?)
            : Call<LoyaltyWrapper<ArrayList<Hit>>>
}