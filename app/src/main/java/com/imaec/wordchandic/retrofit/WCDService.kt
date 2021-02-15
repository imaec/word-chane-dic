package com.imaec.wordchandic.retrofit

import com.imaec.wordchandic.model.Channel
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface WCDService {

    companion object {
        private const val SERVICE_URL = "https://opendict.korean.go.kr/"
        private val rxAdapter = RxJava3CallAdapterFactory.create()
        private val xmlConverter = SimpleXmlConverterFactory.create()
        private val client = OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

        private val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(xmlConverter)
            .addCallAdapterFactory(rxAdapter)
            .client(client)
            .build()

        val instance: WCDService = retrofit.create(WCDService::class.java)
    }

    @GET("api/search")
    fun callSearch(@Query("key") key: String,
                   @Query("q") q: String,
                   @Query("start") start: Int,
                   @Query("num") num: Int = 10,
                   @Query("method") method: String = "start",
                   @Query("letter_s") letter_s: Int = 2,
                   @Query("letter_e") letter_e: Int = 80,
                   @Query("type1") type1: String = "word",
                   @Query("pos") pos: Array<Int> = arrayOf(1,11,15,17,18)): Flowable<Channel>
}