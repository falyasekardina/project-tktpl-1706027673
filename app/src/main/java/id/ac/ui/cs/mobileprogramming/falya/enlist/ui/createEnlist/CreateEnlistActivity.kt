package id.ac.ui.cs.mobileprogramming.falya.enlist.ui.createEnlist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.Constants
import kotlinx.android.synthetic.main.activity_create_enlist.*

/**
 * @author Naveen T P
 * @since 08/11/18
 */
class CreateEnlistActivity : AppCompatActivity() {

    var todoRecord: EnlistRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_enlist)

        //Prepopulate existing title and content from intent
        val intent = intent
        if (intent != null && intent.hasExtra(Constants.INTENT_OBJECT)) {
            val todoRecord: EnlistRecord = intent.getParcelableExtra(Constants.INTENT_OBJECT)
            this.todoRecord = todoRecord
            prePopulateData(todoRecord)
        }

        title = if (todoRecord != null) getString(R.string.viewOrEditTodo) else getString(R.string.createTodo)
    }

    private fun prePopulateData(todoRecord: EnlistRecord) {
        et_todo_title.setText(todoRecord.title)
        et_todo_content.setText(todoRecord.content)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_todo -> {
                saveTodo()
            }
        }
        return true
    }

    /**
     * Sends the updated information back to calling Activity
     * */
    private fun saveTodo() {
        if (validateFields()) {
            val id = if (todoRecord != null) todoRecord?.id else null
            val todo = EnlistRecord(id = id, title = et_todo_title.text.toString(), content = et_todo_content.text.toString())
            val intent = Intent()
            intent.putExtra(Constants.INTENT_OBJECT, todo)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    /**
     * Validation of EditText
     * */
    private fun validateFields(): Boolean {
        if (et_todo_title.text.isEmpty()) {
            til_todo_title.error = getString(R.string.pleaseEnterTitle)
            et_todo_title.requestFocus()
            return false
        }
        if (et_todo_content.text.isEmpty()) {
            til_todo_content.error = getString(R.string.pleaseEnterContent)
            et_todo_content.requestFocus()
            return false
        }
        return true
    }
}