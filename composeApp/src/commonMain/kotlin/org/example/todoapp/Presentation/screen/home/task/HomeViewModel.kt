package org.example.todoapp.Presentation.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.example.todoapp.data.MongoDB
import org.example.todoapp.domain.RequestState
import org.example.todoapp.domain.TaskAction
import org.example.todoapp.domain.TodoTask


typealias MutableTasks = MutableState<RequestState<List<TodoTask>>>
typealias Tasks = MutableState<RequestState<List<TodoTask>>>

class HomeViewModel(
    private val mongoDB: MongoDB
) : ScreenModel {
    private var _activeTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val activeTasks: Tasks = _activeTasks

    private var _completedTasks: MutableTasks = mutableStateOf(RequestState.Idle)
    val completedTasks: Tasks = _completedTasks

    init {
        _activeTasks.value = RequestState.Loading
        _completedTasks.value = RequestState.Loading
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mongoDB.readActiveTasks().collectLatest {
                _activeTasks.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mongoDB.readCompletedTasks().collectLatest {
                _completedTasks.value = it
            }
        }
    }

    fun setAction(action: TaskAction) {
        when(action) {
            is TaskAction.Delete -> {
                deleteTask(action.task)
            }
            is TaskAction.SetCompleted -> {
                setCompleted(action.task, action.completed)
            }
            is TaskAction.SetFavorite -> {
                setFavorite(action.task, action.isfavorite)
            }
            else -> {

            }
        }
    }

    private fun setCompleted(task: TodoTask, completed: Boolean) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.setCompleted(task, completed)
        }
    }

    private fun setFavorite(task: TodoTask, isFavorite: Boolean) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.setFavorite(task, isFavorite)
        }
    }

    private fun deleteTask(task: TodoTask) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.deleteTask(task)
        }
    }

}