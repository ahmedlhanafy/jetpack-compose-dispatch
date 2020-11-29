package com.ahmedlhanafy.androiddispatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmedlhanafy.androiddispatch.ui.AndroidDispatchTheme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @ExperimentalFocus
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(x: Bundle?) {
        super.onCreate(x)
//        val rootStore: RootStore by viewModels()
//        val webservice by lazy {
//            Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:3000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(ApiService::class.java)
//        }
//
//        val ctx = Ctx(webservice)


        setContent {
            AndroidDispatchTheme {
                Mail()
            }
        }
    }
//            val dispatch = remember(rootStore) { Dispatch(rootStore, ctx) }
//
//            Providers(DispatchAmbient provides dispatch) {
//                    Surface(color = MaterialTheme.colors.background) {
//                        Column {
//                            Profile()
//                            Todos()
//                        }
//                    }
//                }
//            }
//        }
//    }
}


@ExperimentalMaterialApi
@Composable
fun Todo(todo: Todo, onCheck: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Checkbox(checked = todo.checked, onCheckedChange = onCheck)
        Spacer(Modifier.width(8.dp))
        Text(text = todo.text)
    }
}

@ExperimentalAnimationApi
@Composable
fun Test() {
    val list = remember { mutableStateListOf("one", "two", "three") }
    val deletedList = remember { mutableStateMapOf<String, Unit>() }

    LazyColumnFor(
        list
    ) { item ->
        AnimatedVisibility(!deletedList.contains(item)) {
            onDispose {
                list.remove(item)
            }
            Text(item, modifier = Modifier.clickable(onClick = { deletedList[item] = Unit }))
        }
    }
}

data class CallbackContainer(var current: () -> Unit)

@ExperimentalFocus
@ExperimentalMaterialApi
@Composable
fun Todos() {
    var text by remember { mutableStateOf("") }
    val dispatch = getDispatch()
    val scope = rememberCoroutineScope()

    launchInComposition {
        dispatch.fetchTodos()
    }

    val callback = remember { CallbackContainer({}) }

    Box(Modifier.fillMaxSize()) {
        LazyColumnFor(
            items = dispatch.rootStore.todos,
        ) { todo ->
            key(todo.id) {
                SwipeableBox(leftOnAction = {
                    scope.launch { dispatch.deleteTodo(todo.id) }
                }) {
                    Todo(
                        todo,
                        onCheck = {
                            scope.launch {
                                dispatch.toggleTodoCheck(
                                    todo.id,
                                    it
                                )
                            }
                        })
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Enter your todo") }
            )
            Spacer(Modifier.width(8.dp))
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        val newTodoText = text
                        text = ""
                        dispatch.addTodo(newTodoText, false)
                    }
                }) {
                Icon(asset = Icons.Default.Add)
            }
        }
    }
}

@Composable
fun Profile() {
    val dispatch = getDispatch()

    launchInComposition {
        dispatch.fetchUser()
    }

    Text(
        text = "Welcome back, ${dispatch.rootStore.profile?.name}!",
        modifier = Modifier.padding(16.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5
    )
}

