package com.demo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.composedemo.animation.Home
import com.demo.composedemo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                Home()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    ComposeDemoTheme {
        Surface(color = Color.DarkGray) {
            content()
        }
    }
}

@Preview(showBackground = true, name = "Text Preview")
@Composable
fun DefaultPreview() {
    MyApp {
        MyScreenContent()
    }
}

@Composable
fun Greeting(greeting: String) {
    var isSelected by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(if (isSelected) Color.Red else Color.Cyan)

    Text(
        text = greeting, modifier = Modifier
            .padding(24.dp)
            .background(color = backgroundColor)
            .clickable(onClick = { isSelected = !isSelected }), style = MaterialTheme.typography.body2
    )
}

@Composable
fun MyScreenContent(names: List<String> = List(1000) { "Hello Android #$it" }) {
    val countState = remember {
        mutableStateOf(0)
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        NameList(names, modifier = Modifier.weight(1f))

        Counter(
            count = countState.value,
            updateCount = { newCount ->
                countState.value = newCount
            }
        )
    }
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit) {
    Button(onClick = { updateCount(count + 1) }, colors = ButtonDefaults.buttonColors(backgroundColor = if (count > 5) Color.Red else Color.Yellow)) {
        Text("Clicked $count times")
    }
}

@Composable
fun NameList(names: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = names) { name ->
            Greeting(greeting = name)
            Divider(color = Color.Cyan)
        }
    }
}
