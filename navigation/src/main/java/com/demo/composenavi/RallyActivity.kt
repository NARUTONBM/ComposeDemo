/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demo.composenavi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.demo.composenavi.ui.accounts.AccountsBody
import com.demo.composenavi.ui.bills.BillsBody
import com.demo.composenavi.ui.components.RallyTabRow
import com.demo.composenavi.ui.overview.OverviewBody
import com.demo.composenavi.ui.theme.RallyTheme
import com.example.compose.rally.data.UserData

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 *
 * @see <a href="https://developer.android.google.cn/codelabs/jetpack-compose-navigation?continue=https%3A%2F%2Fdeveloper.android.google
 * .cn%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-navigation#0">See this </a>for more information about 【Jetpack
 * Compose Navigation】
 *
 * @see <a href="https://developer.android.google.cn/jetpack/compose/kotlin#trailing-lambdas">see this</a> for more information about trailing lambdas
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Preview
@Composable
fun RallyApp() {
    RallyTheme {
        val allScreens = RallyScreen.values().toList()
        val navController = rememberNavController()
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = RallyScreen.fromRoute(backStackEntry.value?.destination?.route)
        Scaffold(
            topBar = {
                RallyTabRow(
                    allScreens = allScreens,
                    onTabSelected = { screen ->
                        navController.navigate(screen.name)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = RallyScreen.Overview.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                // 定义导航路线图
                composable(RallyScreen.Overview.name) {
                    OverviewBody(
                        onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                        onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) })
                }

                composable(RallyScreen.Accounts.name) {
                    AccountsBody(accounts = UserData.accounts)
                }

                composable(RallyScreen.Bills.name) {
                    BillsBody(bills = UserData.bills)
                }
            }
        }
    }
}
