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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navDeepLink
import com.demo.composenavi.data.UserData
import com.demo.composenavi.ui.accounts.AccountsBody
import com.demo.composenavi.ui.accounts.SingleAccountBody
import com.demo.composenavi.ui.bills.BillsBody
import com.demo.composenavi.ui.bills.SingleBillBody
import com.demo.composenavi.ui.components.RallyTabRow
import com.demo.composenavi.ui.overview.OverviewBody
import com.demo.composenavi.ui.theme.RallyTheme

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
            RallyNavHost(navController, Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun RallyNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = RallyScreen.Overview.name,
        modifier = modifier
    ) {
        // 定义导航路线图
        composable(RallyScreen.Overview.name) {
            OverviewBody(
                onClickSeeAllAccounts = { navController.navigate(RallyScreen.Accounts.name) },
                onClickSeeAllBills = { navController.navigate(RallyScreen.Bills.name) },
                onAccountClick = { name ->
                    navigateToSingle(navController, RallyScreen.Accounts.name, name)
                },
                onBillClick = { name ->
                    navigateToSingle(navController, RallyScreen.Bills.name, name)
                })
        }

        composable(RallyScreen.Accounts.name) {
            AccountsBody(accounts = UserData.accounts) { name ->
                navigateToSingle(
                    navController = navController,
                    rootName = RallyScreen.Accounts.name,
                    name = name
                )
            }
        }

        composable(RallyScreen.Bills.name) {
            BillsBody(bills = UserData.bills) { name ->
                navigateToSingle(
                    navController = navController,
                    rootName = RallyScreen.Bills.name,
                    name = name
                )
            }
        }

        val accountsName = RallyScreen.Accounts.name
        composable(
            route = "$accountsName/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            ), deepLinks = listOf(
                navDeepLink { uriPattern = "rally://$accountsName/{name}" }
            )) { entry ->
            // 取得 NavBackStackEntry 中的 name 参数
            val accountName = entry.arguments?.getString("name")
            // 找到 UserData 中第一个匹配的 name
            val account = UserData.getAccount(accountName)
            // 将 account 传给 SingleAccountBody
            SingleAccountBody(account = account)
        }

        val billsName = RallyScreen.Bills.name
        composable(
            route = "$billsName/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            ), deepLinks = listOf(
                navDeepLink { uriPattern = "rally://$billsName/{name}" }
            )) { entry ->
            // 取得 NavBackStackEntry 中的 name 参数
            val billName = entry.arguments?.getString("name")
            // 找到 UserData 中第一个匹配的 name
            val bill = UserData.getBill(billName)
            // 将 account 传给 SingleBillBody
            SingleBillBody(bill = bill)
        }
    }
}

private fun navigateToSingle(navController: NavHostController, rootName: String, name: String) {
    navController.navigate("$rootName/$name")
}
