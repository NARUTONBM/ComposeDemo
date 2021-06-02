package com.demo.composedemo.layoutsincompose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.composedemo.ui.theme.ComposeDemoTheme

/**
 * @see <a href="https://developer.android.google.cn/codelabs/jetpack-compose-layouts?continue=https%3A%2F%2Fdeveloper.android.google
 * .cn%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-layouts#11">See this </a>for more information about 【Layout in
 * Jetpack Compose】
 *
 * 注意这里
 */

/**
 * Intrinsics 允许你在子真正被测量前查看他们
 *
 * 对于一个 composable，可以获取它的 intrinsicWidth 或 intrinsicHeight
 */

@Composable
fun TwoTextStep1(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier) {
        // 由于 Row 分别测量了每个子 composable 所以 Text 的高度不能用来约束 Divider，为实现这一点，可以使用 modifier 的 height(IntrinsicSize.Min)
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )
        // 高度将充满屏幕
        Divider(
            color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = text2,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Composable
fun TwoTextStep2(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        Divider(
            color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = text2,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    ComposeDemoTheme {
        Surface {
            TwoTextStep2(text1 = "Hi, ", text2 = "Compose")
        }
    }
}
