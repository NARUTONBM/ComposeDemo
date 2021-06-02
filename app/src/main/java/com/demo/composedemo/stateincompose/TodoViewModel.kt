package com.demo.composedemo.stateincompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * @author Randall.
 * @Date 2021-06-03.
 * @Time 0:18.
 */
class TodoViewModel : ViewModel() {
    private var _todoItems = MutableLiveData(listOf<TodoItem>())
    val todoItem: LiveData<List<TodoItem>> = _todoItems

    fun addItem(item: TodoItem) {
        _todoItems.value = _todoItems.value!! + listOf(item)
    }

    fun removeItem(item: TodoItem) {
        _todoItems.value = _todoItems.value!!.toMutableList().also {
            it.remove(item)
        }
    }
}