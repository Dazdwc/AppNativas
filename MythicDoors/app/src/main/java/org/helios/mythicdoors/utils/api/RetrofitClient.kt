package org.helios.mythicdoors.utils.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.helios.mythicdoors.utils.AppConstants.RealtimeDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient {
    companion object {
        val instance: ApiService by lazy {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("Accept", "application/json")
                        .method(original.method, original.body)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(RealtimeDatabase.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()


            retrofit.create(ApiService::class.java)
        }
    }
}