package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.EnlistRepository
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord

/**
 * @author Naveen T P
 * @since 08/11/18
 */
class EnlistViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: EnlistRepository = EnlistRepository(application)
    private val allTodoList: LiveData<List<EnlistRecord>> = repository.getAllTodoList()

    fun saveTodo(todo: EnlistRecord) {
        repository.saveTodo(todo)
    }

    fun updateTodo(todo: EnlistRecord){
        repository.updateTodo(todo)
    }

    fun deleteTodo(todo: EnlistRecord) {
        repository.deleteTodo(todo)
    }

    fun getAllTodoList(): LiveData<List<EnlistRecord>> {
        return allTodoList
    }

}