package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists

import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord

/**
 * RecycleView touch event callbacks
 * */
interface EnlistEvents {
    fun onDeleteClicked(todoRecord: EnlistRecord)
    fun onViewClicked(todoRecord: EnlistRecord)
}