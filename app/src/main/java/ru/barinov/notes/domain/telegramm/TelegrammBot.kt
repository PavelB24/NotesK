package ru.barinov.notes.domain.telegramm

import com.squareup.moshi.Moshi
import retrofit2.converter.gson.GsonConverterFactory


class TelegrammBot {
    private val baseURL = "https://api.telegram.org/bot2100274976:AAGs4zvS8RaizEjwwluY6pLEFdaMIscsduY/"
    private val botApiKey = "2100274976:AAGs4zvS8RaizEjwwluY6pLEFdaMIscsduY"
    private val channelName = "@kotlin_test_ch"
    private val message = ""
    private val retrofit = retrofit2.Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseURL).build()
    private val service = retrofit.create(TelegrammApi::class.java)





    fun getService(): TelegrammApi{
        return service
    }
    fun getChanelName(): String{
        return channelName
    }
    fun getBotApiKey(): String{
        return botApiKey
    }



}