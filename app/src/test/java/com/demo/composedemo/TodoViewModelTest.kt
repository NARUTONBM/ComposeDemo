package com.demo.composedemo

import com.demo.composedemo.stateincompose.TodoViewModel
import com.demo.composedemo.util.generateRandomTodoItem
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 *
 * @author Randall.
 * @Date 2021-06-03.
 * @Time 17:58.
 */
class TodoViewModelTest {

    @Test
    fun whenRemovingItem_updateList() {
        val viewModel = TodoViewModel()
        val item1 = generateRandomTodoItem()
        val item2 = generateRandomTodoItem()
        viewModel.addItem(item1)
        viewModel.addItem(item2)

        viewModel.removeItem(item1)

        assertThat(viewModel.todoItems).isEqualTo(listOf(item2))
    }
}