package id.ac.ui.cs.mobileprogramming.falya.enlist.data.db

import android.os.Parcelable
import android.widget.ImageView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @author Naveen T P
 * @since 08/11/18
 */
@Entity(tableName = "todo")
@Parcelize()
data class EnlistRecord(@PrimaryKey(autoGenerate = true) val id: Long?,
                      @ColumnInfo(name = "title") val title: String,
                      @ColumnInfo(name = "content") val content: String,
                        @ColumnInfo(name = "image") val image: String) : Parcelable