package ru.barinov.notes.domain.telegramm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TelegrammApi {
    //todo Переделать, вынести всё кроме сообщения в baseURL
    @GET("sendMessage")
    fun sendMassage(@Query("chat_id") id: String, @Query("text") message: String): Call<Void>
}