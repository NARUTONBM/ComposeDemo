package com.demo.composedemo

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.demo.composedemo.ui.theme.ComposeDemoTheme

/**
 * @see <a href="https://developer.android.google.cn/codelabs/jetpack-compose-layouts?continue=https%3A%2F%2Fdeveloper.android.google
 * .cn%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-layouts#9">See this </a>for more information about 【Layout in
 * Jetpack Compose】
 */

/**
 * 边界（以及其他所有的辅助约束）可以在 ConstraintLayout 内创建，但不能在 constraintAs 内
 *
 * linkTo 可以用来基于辅助线或者布局边界进行约束
 */
@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        // 创建 composable 的实例
        val (button1, button2, text) = createRefs()

        Button(
            onClick = { /**/ },
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button 1")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end)
        })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = { /**/ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button 2")
        }
    }
}

@Preview
@Composable
fun ConstraintLayoutContentPreview() {
    ComposeDemoTheme {
        DecoupleConstraintLayout()
    }
}

@Composable
fun DecoupleConstraintLayout() {
    BoxWithConstraints {
        val constrains = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp)
        } else {
            decoupledConstraints(margin = 32.dp)
        }

        ConstraintLayout(constrains) {
            Button(
                onClick = { /**/ },
                modifier = Modifier.layoutId("button")
            ) {
                Text(text = "Button")
            }

            Text(text = "Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}
