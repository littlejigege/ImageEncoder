package com.qgstudio.imageencoder

import android.app.Application
import com.mobile.utils.Utils
import com.qgstudio.imageencoder.net.Api
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl("http://192.168.43.47:8080/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build())
                    .build()
        }
        val api by lazy { retrofit.create(Api::class.java) }
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

    }
}