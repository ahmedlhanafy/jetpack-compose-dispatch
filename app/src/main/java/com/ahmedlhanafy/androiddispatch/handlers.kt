package com.ahmedlhanafy.androiddispatch

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

suspend fun Dispatch.fetchUser() {
    withContext(Dispatchers.IO) {
        val profile = ctx.service.getProfile()

        rootStore.profile = profile
    }
}

suspend fun Dispatch.fetchTodos() {
    withContext(Dispatchers.IO) {
        val todos = ctx.service.getTodos()
        Log.d("TAG", "fetchTodos: $todos")
        rootStore.todos = todos
    }
}

suspend fun Dispatch.addTodo(text: String, checked: Boolean) {
    withContext(Dispatchers.IO) {
        ctx.service.addTodo(text, checked)
        fetchTodos()
    }
}

suspend fun Dispatch.deleteTodo(id: Int) {
    withContext(Dispatchers.IO) {
        ctx.service.deleteTodo(id)
        fetchTodos()
    }
}

suspend fun Dispatch.toggleTodoCheck(id: Int, checked: Boolean) {
    withContext(Dispatchers.IO) {
        ctx.service.toggleTogo(id, checked)
        fetchTodos()
    }
}

