package id.ac.ui.cs.mobileprogramming.falya.enlist.ui

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.broadcast.NotificationPublisher
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord
import id.ac.ui.cs.mobileprogramming.falya.enlist.dummy.DummyContent
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.createEnlist.CreateEnlistActivity
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists.EnlistAdapter
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists.EnlistViewModel
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.Constants
import kotlinx.android.synthetic.main.activity_item_list.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@Suppress("DEPRECATION")
class ItemListActivity : AppCompatActivity(), EnlistAdapter.TodoEvents {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var mAuth: FirebaseAuth
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDateTime = LocalDateTime.now()

    //ViewModel
    private lateinit var todoViewModel: EnlistViewModel
    private lateinit var todoAdapter: EnlistAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        mAuth = FirebaseAuth.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        updateUI(mAuth.currentUser)
        initGoogleSignInClient()
        setSupportActionBar(bottomMenuBar)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val intent = Intent(this@ItemListActivity, CreateEnlistActivity::class.java)
            startActivityForResult(intent, Constants.INTENT_CREATE_TODO)
        }

        //Setting up RecyclerView
        rv_todo_list.layoutManager = LinearLayoutManager(this)
        todoAdapter = EnlistAdapter(this)
        rv_todo_list.adapter = todoAdapter


        //Setting up ViewModel and LiveData
        todoViewModel = ViewModelProviders.of(this).get(EnlistViewModel::class.java)
        todoViewModel.getAllTodoList().observe(this, Observer {
            todoAdapter.setAllTodoItems(it)
        })

    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions : GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        ).build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.appbar_home -> {
                home()
                return true
            }
            R.id.appbar_timer -> {
                studyTimer()
                return true
            }
            R.id.app_bar_signout -> {
                signOut()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    //language
//    private fun setLocale(localeName: String) {
//        if (localeName != currentLanguage) {
//            locale = Locale(localeName)
//            val res = resources
//            val dm = res.displayMetrics
//            val conf = res.configuration
//            conf.locale = locale
//            res.updateConfiguration(conf, dm)
//            val refresh = Intent(
//                this,
//                ItemListActivity::class.java
//            )
//            refresh.putExtra(currentLanguage, localeName)
//            startActivity(refresh)
//        } else {
//            Toast.makeText(
//                this@ItemListActivity, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
//        }
//    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUI(user: FirebaseUser?){
        if(user != null){
            //Do your Stuff
            name.text = "Hello, ${user.displayName}"
            date_now.text = "Today is ${currentDateTime.format(
                DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.FULL
                )
            )}"
            totalList.text= "You have 10 list(s) to do"
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    }

    private fun home() {
    }

    private fun studyTimer() {
        intent = Intent(this, StudyTimerActivity::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        singOutFirebase()
        signOutGoogle()
        val i = baseContext.packageManager
            .getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    private fun singOutFirebase() {
        firebaseAuth.signOut()
    }

    private fun signOutGoogle() {
        googleSignInClient.signOut()
    }

    /**
     * RecyclerView Item callbacks
     * */
    //Callback when user clicks on Delete note
    override fun onDeleteClicked(todoRecord: EnlistRecord) {
        todoViewModel.deleteTodo(todoRecord)
    }

    //Callback when user clicks on view note
    override fun onViewClicked(todoRecord: EnlistRecord) {
        val intent = Intent(this@ItemListActivity, CreateEnlistActivity::class.java)
        intent.putExtra(Constants.INTENT_OBJECT, todoRecord)
        startActivityForResult(intent, Constants.INTENT_UPDATE_TODO)
    }


    /**
     * Activity result callback
     * Triggers when Save button clicked from @CreateTodoActivity
     * */
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val todoRecord = data?.getParcelableExtra<EnlistRecord>(Constants.INTENT_OBJECT)!!
            when (requestCode) {
                Constants.INTENT_CREATE_TODO -> {
                    todoViewModel.saveTodo(todoRecord)
                }
                Constants.INTENT_UPDATE_TODO -> {
                    todoViewModel.updateTodo(todoRecord)
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ItemListActivity,
        private val values: List<DummyContent.DummyItem>,
        private val twoPane: Boolean
    ) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}