package com.demo.composedemo.layoutsincompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        ComplexBodyContent(
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

/**
 * 参考#7
 */
@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constrains ->
        // 记录宽高
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->

            val placeable = measurable.measure(constrains)

            // 记录最大宽高
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = kotlin.math.max(rowHeights[row], placeable.height)

            placeable
        }

        // 网格的宽度就是最大宽度
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constrains.minHeight.rangeTo(constrains.maxWidth)) ?: constrains.minWidth
        // 高是高度约束下的每行最高的总和
        val height = rowHeights.sumBy { it }
            .coerceIn(constrains.minHeight.rangeTo(constrains.maxHeight))
        // 每行的 Y 坐标基于前面行的高度累计
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        // 设置父布局的尺寸
        layout(width, height) {
            // X 坐标，所在行放置一个叠加一次
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun ComplexBodyContent(modifier: Modifier = Modifier) {
    // 用 Row 包装 StaggeredGrid 增加滑动效果
    // modifier 从左到右更新约束，返回从右到左的尺寸
    Row(
        modifier = modifier
            .background(color = Color.LightGray)
            .size(200.dp)
            .padding(16.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        StaggeredGrid(modifier = modifier, rows = 5) {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    ComposeDemoTheme {
        Chip(text = "Hi there")
    }
}
