package com.example.room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.room.ui.theme.RoomTheme
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDb::class.java, "db").build()
        val dao = db.dao()

        enableEdgeToEdge()
        setContent {
            RoomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val tasks by dao.getAll().collectAsState(initial = emptyList())
                    val scope = rememberCoroutineScope()
                    var text by remember { mutableStateOf("") }

                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ){
                        Row {
                            TextField(
                                value = text,
                                onValueChange = { text = it },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Faire...") }
                            )

                            Button(onClick = {
                                scope.launch {
                                    if (text.isNotBlank()) {
                                        dao.insert(Task(title = text))
                                        text = ""
                                    }
                                }
                            }) {
                                Text("OK")
                            }
                        }

                        LazyColumn {
                            items(tasks) { task ->
                                TextButton(onClick = {
                                    scope.launch { dao.delete(task) }
                                }) {
                                    Text("${task.title} (Supprimer)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RoomTheme {
        Greeting("Android")
    }
}