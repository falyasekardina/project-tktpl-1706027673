package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.ItemListActivity
import kotlinx.android.synthetic.main.enlist_item.view.*

/**
 * @author Naveen T P
 * @since 08/11/18
 */
class EnlistAdapter(todoEvents: ItemListActivity) : RecyclerView.Adapter<EnlistAdapter.ViewHolder>(), Filterable {

    private var todoList: List<EnlistRecord> = arrayListOf()
    private var filteredTodoList: List<EnlistRecord> = arrayListOf()
    private val listener: TodoEvents = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.enlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = todoList.size

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
     * Search Filter implementation
     * */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredTodoList = if (charString.isEmpty()) {
                    todoList
                } else {
                    val filteredList = arrayListOf<EnlistRecord>()
                    for (row in todoList) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())
                            || row.content.contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredTodoList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredTodoList = p1?.values as List<EnlistRecord>
                notifyDataSetChanged()
            }

        }
    }

    /**
     * Activity uses this method to update todoList with the help of LiveData
     * */
    fun setAllTodoItems(todoItems: List<EnlistRecord>) {
        this.todoList = todoItems
        this.filteredTodoList = todoItems
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