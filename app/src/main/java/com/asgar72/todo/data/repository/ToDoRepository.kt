package com.asgar72.todo.data.repository

import androidx.lifecycle.LiveData
import com.asgar72.todo.data.ToDoDao
import com.asgar72.todo.data.models.ToDoData

class ToDoRepository (private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

}