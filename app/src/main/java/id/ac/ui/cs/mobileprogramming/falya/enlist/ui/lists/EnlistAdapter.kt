package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.ItemListActivity
import kotlinx.android.synthetic.main.activity_create_enlist.view.*
import kotlinx.android.synthetic.main.enlist_item.view.*

/**
 * @author Naveen T P
 * @since 08/11/18
 */
class EnlistAdapter(todoEvents: ItemListActivity) : RecyclerView.Adapter<EnlistAdapter.ViewHolder>() {

    private var todoList: List<EnlistRecord> = arrayListOf()
    private val listener: TodoEvents = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.enlist_item, parent, false)
        Log.d("jumlah size", todoList.size.toString())
        return ViewHolder(view)
    }

    fun itemCount() {
        todoList.size.toString()
    }

    override fun getItemCount(): Int {
         return todoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todoList[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todo: EnlistRecord, listener: TodoEvents) {
            itemView.tv_item_title.text = todo.title
            itemView.tv_item_content.text = todo.content

            itemView.iv_item_delete.setOnClickListener {
                listener.onDeleteClicked(todo)
            }

            itemView.setOnClickListener {
                listener.onViewClicked(todo)
            }
        }
    }

    /**
     * Activity uses this method to update todoList with the help of LiveData
     * */
    fun setAllTodoItems(todoItems: List<EnlistRecord>) {
        this.todoList = todoItems
        notifyDataSetChanged()
    }

    /**
     * RecycleView touch event callbacks
     * */
    interface TodoEvents {
        fun onDeleteClicked(todoRecord: EnlistRecord)
        fun onViewClicked(todoRecord: EnlistRecord)
    }

}