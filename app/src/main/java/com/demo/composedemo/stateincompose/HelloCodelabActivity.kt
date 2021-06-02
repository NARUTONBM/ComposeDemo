package com.demo.composedemo.stateincompose

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.composedemo.databinding.ActivityHelloCodelabBinding

/**
 *
 * @author Randall.
 * @Date 2021-06-02.
 * @Time 20:57.
 *
 * @see <a href="https://developer.android.google.cn/codelabs/jetpack-compose-state?continue=https%3A%2F%2Fdeveloper.android.google
 * .cn%2Fcourses%2Fpathways%2Fcompose%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fjetpack-compose-state#2">See this </a>for more information about 【Using state in
 * Jetpack Compose】
 */
class HelloCodelabActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelloCodelabBinding
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.textInput.doAfterTextChanged { text ->
            name = text.toString()
            updateHello()
        }
    }

    private fun updateHello() {
        binding.helloText.text = "Hello, $name"
    }
}

class HelloCodelabViewModel : ViewModel() {
    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    fun onNameChanged(newName: String) {
        _name.value = newName
    }
}

class HelloCodelabActivityWithViewModel : AppCompatActivity() {
    private val helloViewModel by viewModels<HelloCodelabViewModel>()
    private lateinit var binding: ActivityHelloCodelabBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.textInput.doAfterTextChanged {
            helloViewModel.onNameChanged(it.toString())
        }

        helloViewModel.name.observe(this) { name ->
            binding.helloText.text = "Hello, $name"
        }
    }
}
