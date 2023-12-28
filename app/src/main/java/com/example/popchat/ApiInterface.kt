package com.example.popchat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers("X-RapidAPI-Key:f6ce0860e3msh153c6370b08a20ep183b74jsn7cc90bbd7112" ,
        "X-RapidAPI-Host:deezerdevs-deezer.p.rapidapi.com")
    @GET("search")
    fun getData(@Query("q") query: String ) : Call<Mydata>

}