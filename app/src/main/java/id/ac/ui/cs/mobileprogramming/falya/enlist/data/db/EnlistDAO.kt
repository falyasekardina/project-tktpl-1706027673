package id.ac.ui.cs.mobileprogramming.falya.enlist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

/**
 * @author Naveen T P
 * @since 08/11/18
 */

@Dao
interface EnlistDAO {

    @Insert
    suspend fun saveTodo(enlistRecord: EnlistRecord)

    @Delete
    suspend fun deleteTodo(enlistRecord: EnlistRecord)

    @Update
    suspend fun updateTodo(enlistRecord: EnlistRecord)

    @Query("SELECT * FROM todo ORDER BY id DESC")
    fun getAllTodoList(): LiveData<List<EnlistRecord>>
}