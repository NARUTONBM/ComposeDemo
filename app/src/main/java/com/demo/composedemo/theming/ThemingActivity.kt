package com.demo.composedemo.theming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface

/**
 * @author Randall.
 * @Date 2021-06-14.
 * @Time 15:28.
 *
 * @see <a href="https://developer.android.google.cn/codelabs/jetpack-compose-theming?continue=https%3A%2F%2Fdeveloper.android.google
 * .cn%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-theming#0">See this </a>for more information about 【Jetpack
 * Compose theming】
 */
class ThemingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                Home()
            }
        }
    }
}