package com.asgar72.todo.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.asgar72.todo.R
import com.asgar72.todo.data.models.Priority
import com.asgar72.todo.data.models.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    /** =====               List Fragment              =====**/
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>) {
        emptyDatabase.value = toDoData.isEmpty()
    }

    /** =====               Add/Update Fragment              =====**/
    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedTextView = parent?.getChildAt(0) as? TextView
            selectedTextView?.let {
                when (position) {
                    0 -> it.setTextColor(ContextCompat.getColor(application, R.color.red))
                    1 -> it.setTextColor(ContextCompat.getColor(application, R.color.yellow))
                    2 -> it.setTextColor(ContextCompat.getColor(application, R.color.green))
                }
            }
        }

    }

    //this code for check manually user fill all text or not
    fun verifyDataFromUser(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> { Priority.HIGH }
            "Medium Priority" -> { Priority.MEDIUM }
            "Low Priority" -> { Priority.LOW }
            else -> Priority.LOW
        }
    }
}