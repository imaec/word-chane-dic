package com.imaec.wordchandic.retrofit

import com.imaec.wordchandic.model.Channel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WCDService {

    companion object {
        const val SERVICE_URL = "https://opendict.korean.go.kr/"
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val instance = retrofit.create(WCDService::class.java)
    }

    @GET("api/search")
    fun callSearch(@Query("key") key: String,
                   @Query("q") q: String,
                   @Query("start") start: Int,
                   @Query("num") num: Int,
                   @Query("method") method: String = "start",
                   @Query("pos") pos: String = "1,11,15,17,18"): Flowable<Channel>
}