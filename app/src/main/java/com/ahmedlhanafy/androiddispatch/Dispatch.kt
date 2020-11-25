package com.ahmedlhanafy.androiddispatch

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import retrofit2.Retrofit
import retrofit2.http.GET


class RootStore : ViewModel() {
    var profile: Profile? by mutableStateOf(null)
    var todos: List<Todo> by mutableStateOf(listOf())
}

data class Ctx(val service: ApiService)

data class Dispatch(val rootStore: RootStore, val ctx: Ctx)

val DispatchAmbient = ambientOf<Dispatch> { error("New dispatch created!") }

@Composable
fun getDispatch(): Dispatch {
    return DispatchAmbient.current;
}