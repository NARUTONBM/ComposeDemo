package com.demo.composedemo.stateincompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 *
 * @author Randall.
 * @Date 2021-06-03.
 * @Time 0:18.
 */
class TodoViewModel : ViewModel() {
    private var currentEditPosition by mutableStateOf(-1)

    var todoItems by mutableStateOf(listOf<TodoItem>())
        private set

    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(currentEditPosition)

    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todoItems.indexOf(item)
    }

    fun onEditDone() {
        currentEditPosition = -1
    }

    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {}

        todoItems = todoItems.toMutableList().also {
            it[currentEditPosition] = item
        }
    }

    fun addItem(item: TodoItem) {
        todoItems = todoItems + listOf(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems = todoItems.toMutableList().also {
            it.remove(item)
        }
        onEditDone()
    }
}