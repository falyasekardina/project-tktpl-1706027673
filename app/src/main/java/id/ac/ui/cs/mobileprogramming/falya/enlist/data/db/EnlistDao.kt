package id.ac.ui.cs.mobileprogramming.falya.enlist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

/**
 * @author Naveen T P
 * @since 08/11/18
 */

@Dao
interface EnlistDao {

    @Insert
    fun saveTodo(enlistRecord: EnlistRecord)

    @Delete
    fun deleteTodo(enlistRecord: EnlistRecord)

    @Update
    fun updateTodo(enlistRecord: EnlistRecord)

    @Query("SELECT * FROM todo ORDER BY id DESC")
    fun getAllTodoList(): LiveData<List<EnlistRecord>>
}