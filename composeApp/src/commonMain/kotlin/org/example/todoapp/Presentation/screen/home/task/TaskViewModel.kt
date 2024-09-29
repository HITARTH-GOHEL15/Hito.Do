package org.example.todoapp.Presentation.screen.home.task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.example.todoapp.data.MongoDB
import org.example.todoapp.domain.TaskAction
import org.example.todoapp.domain.TodoTask

class TaskViewModel(
    private val mongoDB: MongoDB
) : ScreenModel {

    fun setAction(action: TaskAction) {
        when (action) {
            is TaskAction.Add -> {
                addTask(action.task)
            }
            is TaskAction.Update -> {
                updateTask(action.task)
            }

            else -> {}
        }
    }

    private fun addTask(task: TodoTask) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.addTask(task)
        }
    }

    private fun updateTask(task: TodoTask) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.updateTask(task)
        }
    }
}