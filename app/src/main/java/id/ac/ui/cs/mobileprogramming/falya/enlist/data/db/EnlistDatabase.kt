package id.ac.ui.cs.mobileprogramming.falya.enlist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author Naveen T P
 * @since 08/11/18
 */

@Database(entities = [EnlistRecord::class], version = 1, exportSchema = false)
abstract class EnlistDatabase : RoomDatabase() {

    abstract fun todoDao(): EnlistDao

    companion object {
        private var INSTANCE: EnlistDatabase? = null

        fun getInstance(context: Context): EnlistDatabase? {
            if (INSTANCE == null) {
                synchronized(EnlistDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                        EnlistDatabase::class.java,
                        "todo_db")
                        .build()
                }
            }
            return INSTANCE
        }
    }
}