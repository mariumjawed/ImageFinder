package com.android.searchimage.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.searchimage.manager.WebServiceFactory
import com.android.searchimage.model.Hit
import com.android.searchimage.repository.LoyaltyWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private var _searchData: MutableLiveData<LoyaltyWrapper<ArrayList<Hit>>>? = null
    var searchData: LiveData<LoyaltyWrapper<ArrayList<Hit>>>? = null
    var _isViewLoading: MutableLiveData<Boolean>? = null
    var isViewLoading: LiveData<Boolean>? = null

    init {
        _searchData = MutableLiveData()
        searchData = _searchData
        _isViewLoading = MutableLiveData()
        isViewLoading = _isViewLoading
    }

    fun search(
        key: String?,
        query: String?,
        imageType: String?

    ) {
        _isViewLoading?.value = true

        val feedsCall: Call<LoyaltyWrapper<ArrayList<Hit>>> =
            WebServiceFactory.getInstanceBaseURL("").search(
                key, query, imageType
            )
        feedsCall.enqueue(object : Callback<LoyaltyWrapper<ArrayList<Hit>>> {
            @SuppressLint("LongLogTag")
            override fun onResponse(
                call: Call<LoyaltyWrapper<ArrayList<Hit>>>,
                responseResult: Response<LoyaltyWrapper<ArrayList<Hit>>>
            ) {
                if (responseResult.isSuccessful)
                    _searchData!!.postValue(responseResult.body())
                else {
                    _isViewLoading?.value = false
                }


            }

            override fun onFailure(
                call: Call<LoyaltyWrapper<ArrayList<Hit>>>,
                t: Throwable
            ) {
                _isViewLoading?.value = false
            }
        })
    }
}