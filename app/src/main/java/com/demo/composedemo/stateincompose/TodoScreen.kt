package com.demo.composedemo.stateincompose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.composedemo.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * 当 Compose 第一次运行组合时，它搭建了所有被调用的 composable 的视图树
 * 然后在重组时，更新树中被调用的 composable
 *
 * 副作用（Side-effect）是指在 composable 作用于之外对数据进行了改动
 * 重组时应避免 side-effect
 * 诸如 修改 ViewModel 中的状态、调用 Random.nextInt()、或者修改数据库都是典型的 side-effect
 *
 * remember 给 composable 提供记忆能力
 * 通过 remember 实例化的值将被存储在组合树中，且只有在 remember 的 key 被修改时才会重新计算
 * 可以类比成对象中的 private val 属性
 * 组合树中存储的值会在 composable 被移出树时立刻被清除，也会在 composable 重新加到树中时被重新创建
 * 比如会在 LazyColumn 中移除顶部的 item 时发生上述情况，所以不要用 remember 去存储重要的数据
 *
 * 幂等的 composable 对于相同的输入会保持相同的输出，当然更没有 side-effect
 *
 * State hoisting(实在不会翻) 是一种将状态上移实现 composable stateless 的模式
 * 这种模式应用到 composable 上通常意味着给 composable 引入两个参数
 * @param value: T 当前需要展示的数据
 * @param onValueChange: (T) -> Unit 响应数据变化的事件
 * 这样提升状态层级有几个重要的特性：
 * - 单一真相源 移动状态而不是复制确保了 text 只有一个可信的数据源
 * - 封装性     当其他 composable 发送事件到特定的 composable 时，只有它可以修改状态
 * - 共享性     可以作为一个不可变的值被多个 composable 访问
 * - 可拦截性   这个 composable 可以在修改状态前决定忽略或修改事件
 * - 解耦       composable 的状态可以被存在任何地方
 *
 * @author Randall.
 * @Date 2021-06-02.
 * @Time 23:55.
 */

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */
@Composable
fun TodoScreen(
    items: List<TodoItem>,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit
) {
    Column {
        TodoItemInputBackground(elevate = true, modifier = Modifier.fillMaxWidth()) {
            TodoItemEntryInput(onItemComplete = onAddItem)
        }
    }
}

/**
 * Stateless composable that displays a full-width [TodoItem].
 *
 * @param todo item to show
 * @param onItemClicked (event) notify caller that the row was clicked
 * @param modifier modifier for this element
 */
@Composable
fun TodoRow(
    todo: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconAlpha: Float = remember(todo.id) { randomTint() }
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task)
        Icon(
            imageVector = todo.icon.imageVector,
            // LocalContentColor 提供了当前内容颜色的实例
            // 会被绘制背景的 Surface 这类 Composable 修改
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDescription)
        )
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}

@Composable
fun TodoInputTextField(textInput: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val mutableState = remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    val (text, setText) = remember { mutableStateOf("") }
    TodoInputText(textInput, onTextChange, modifier)
}

/**
 * onItemComplete 是一个当用户完成输入时触发的事件
 */
@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }
    val iconVisible = text.isNotBlank()
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }

    TodoItemInput(text, setText, icon, setIcon, submit, iconVisible)
}

@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submit: () -> Unit,
    iconVisible: Boolean
) {
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputText(
                text,
                onTextChange,
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onImeAction = submit
            )

            TodoEditButton(
                onClick = submit,
                text = "Add",
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = text.isNotBlank()
            )
        }

        if (iconVisible) {
            AnimatedIconRow(icon, onIconChange, Modifier.padding(top = 8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun PreviewTodoItemInput() = TodoItemEntryInput(onItemComplete = {})
