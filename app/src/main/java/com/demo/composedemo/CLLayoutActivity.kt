package com.demo.composedemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.demo.composedemo.ui.theme.ComposeDemoTheme

/**
 * @see <a href="https://developer.android.google.cn/codelabs/jetpack-compose-layouts?continue=https%3A%2F%2Fdeveloper.android.google
 * .cn%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-layouts">See this </a>for more information about 【Layout in
 * Jetpack Compose】
 */
class CLLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                })
        }) { innerPadding ->
        ModifiedBodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hi, there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

/*#7*/
@Preview
@Composable
fun PhotographyCardPreview() {
    ComposeDemoTheme {
        LayoutsCodelab()
    }
}

fun Modifier.firstBaselineTop(firstBaselineToTop: Dp) = this.then(
    // 基于 Compose 布局原则，只能测量一次子
    layout { measurable, constraints ->
        // Measurable 调用 measure() 的返回值是一个可以通过调用 placeRelative(x, y) 来指定位置的 Placeable
        val placeable = measurable.measure(constraints)
        // 这个 composable 已经测量了，现在需要通过调用接受 lambda 的 layout(width, height) 来计算大小并指定位置

        // 这个例子，确认当前是否有 FirstBaseLine
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]
        // 高度
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY

        layout(placeable.width, height) {
            // 调用 placeable.placeRelative(x, y) 来放置 composable
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview
@Composable
fun TextWithPaddingToBaselinePreview() {
    ComposeDemoTheme {
        Text(text = "Hi, principle of Layout in Compose!", Modifier.firstBaselineTop(32.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview() {
    ComposeDemoTheme {
        Text(text = "Hi, principle of Layout in Compose!", Modifier.padding(top = 32.dp))
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constrains ->
        // 不光要测量每个子，还要关注最大宽、高
        val placeable = measurables.map { measurable ->
            measurable.measure(constrains)
        }

        var yPosition = 0

        layout(constrains.maxWidth, constrains.maxHeight) {
            placeable.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)

                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun ModifiedBodyContent(modifier: Modifier = Modifier) {
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}