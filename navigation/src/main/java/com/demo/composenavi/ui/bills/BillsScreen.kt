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

package com.demo.composenavi.ui.bills

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.demo.composenavi.R
import com.demo.composenavi.data.Bill
import com.demo.composenavi.ui.components.BillRow
import com.demo.composenavi.ui.components.StatementBody

/**
 * The Bills screen.
 */
@Composable
fun BillsBody(
    bills: List<Bill>,
    onBillNameClick: (String) -> Unit = {},
) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Bills" },
        items = bills,
        amounts = { bill -> bill.amount },
        colors = { bill -> bill.color },
        amountsTotal = bills.map { bill -> bill.amount }.sum(),
        circleLabel = stringResource(R.string.due),
        rows = { bill ->
            BillRow(
                modifier = Modifier.clickable {
                    onBillNameClick(bill.name)
                },
                bill.name,
                bill.due,
                bill.amount,
                bill.color
            )
        }
    )
}

/**
 * Detail screen for a single bill.
 */
@Composable
fun SingleBillBody(bill: Bill) {
    StatementBody(
        items = listOf(bill),
        colors = { bill.color },
        amounts = { bill.amount },
        amountsTotal = bill.amount,
        circleLabel = bill.name,
    ) { row ->
        BillRow(
            name = row.name,
            due = row.due,
            amount = row.amount,
            color = row.color
        )
    }
}
