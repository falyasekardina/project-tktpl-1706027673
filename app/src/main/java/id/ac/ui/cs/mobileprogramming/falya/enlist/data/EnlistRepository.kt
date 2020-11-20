package id.ac.ui.cs.mobileprogramming.falya.enlist.data

import android.app.Application
import androidx.lifecycle.LiveData
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistDao
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistDatabase
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.ItemListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @author Naveen T P
 * @since 08/11/18
 */
class EnlistRepository(application: Application) {

    private val enlistDao: EnlistDao
    private val allTodos: LiveData<List<EnlistRecord>>

    init {
        val database = EnlistDatabase.getInstance(application.applicationContext)
        enlistDao = database!!.todoDao()
        allTodos = enlistDao.getAllTodoList()
    }

    fun saveTodo(todo: EnlistRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            enlistDao.saveTodo(todo)
        }
    }

    fun updateTodo(todo: EnlistRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            enlistDao.updateTodo(todo)
        }
    }


    fun deleteTodo(todo: EnlistRecord) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                enlistDao.deleteTodo(todo)
            }
        }
    }

    fun getAllTodoList(): LiveData<List<EnlistRecord>> {
        return allTodos
    }
}