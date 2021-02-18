package com.android.searchimage.manager

import com.android.searchimage.constants.WebServiceConstants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class WebServiceFactory {

    companion object {

        private var retrofitBase: Retrofit? = null
        private var staticToken = ""

        fun getInstanceBaseURL(
            _token: String
        ): WebServiceProxy {

            if (retrofitBase == null || staticToken.isEmpty() || staticToken != _token) {
                staticToken = _token
                val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                httpClient.connectTimeout(60, TimeUnit.SECONDS)
                httpClient.readTimeout(60, TimeUnit.SECONDS)
                httpClient.addInterceptor { chain ->
                    val original: Request = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()
                    requestBuilder.addHeader(
                        "Authorization",
                        "Bearer $staticToken"
                    )
                    requestBuilder.addHeader("Accept", "application/json")

                    // Request customization: add request headers
                    val request: Request = requestBuilder.build()
                    chain.proceed(request)
                }

                httpClient.addInterceptor(interceptor)
                retrofitBase =
                    Retrofit.Builder()
                        .baseUrl(WebServiceConstants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build()
            }
            return retrofitBase!!.create(WebServiceProxy::class.java)
        }

    }

}