package com.ahmedlhanafy.androiddispatch

import retrofit2.http.*


data class Profile(val name: String, val age: Int)

data class Todo(val id: Int, val text: String, val checked: Boolean)

interface ApiService {
    @GET("/profile")
    suspend fun getProfile(): Profile

    @GET("/todos")
    suspend fun getTodos(): List<Todo>

    @FormUrlEncoded
    @POST("/todos")
    suspend fun addTodo(@Field("text") text: String, @Field("checked") checked: Boolean)

    @DELETE("/todos/{id}")
    suspend fun deleteTodo(@Path("id") id: Int)

    @FormUrlEncoded
    @PATCH("/todos/{id}")
    suspend fun toggleTogo(@Path("id") id: Int, @Field("checked") checked: Boolean)
}
