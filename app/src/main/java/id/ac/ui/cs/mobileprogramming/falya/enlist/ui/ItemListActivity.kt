package id.ac.ui.cs.mobileprogramming.falya.enlist.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.data.db.EnlistRecord
import id.ac.ui.cs.mobileprogramming.falya.enlist.dummy.DummyContent
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.createEnlist.CreateEnlistActivity
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists.EnlistAdapter
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.lists.EnlistViewModel
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.Constants
import kotlinx.android.synthetic.main.activity_item_list.*
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
    private lateinit var searchView: SearchView
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
            resetSearchView()
            val intent = Intent(this@ItemListActivity, CreateEnlistActivity::class.java)
            startActivityForResult(intent, Constants.INTENT_CREATE_TODO)
        }

//DEFAULT
//        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//            twoPane = true
//        }
//
//        setupRecyclerView(findViewById(R.id.item_list))

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
            GoogleSignInOptions.DEFAULT_SIGN_IN).build()
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
                Toast.makeText(this, "HOME CLICKED", Toast.LENGTH_LONG).show()
                home()
                return true
            }
            R.id.appbar_history -> {
                Toast.makeText(this, "HISTORY CLICKED", Toast.LENGTH_LONG).show()
                history()
                return true
            }
            R.id.app_bar_signout -> {
                signOut()
                return true
            }
//            R.id.app_bar_indonesian -> {
//                setLocale("in")
//            }
//            R.id.app_bar_english -> {
//                setLocale("en")
//            }
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
            date.text = "Today is ${currentDateTime.format(
                DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.FULL
                )
            )}"
            totalList.text= "You have 3 list(s) to do"
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, twoPane)
    }

    private fun home() {
    }

    private fun history() {
        intent = Intent(this, ItemDetailActivity::class.java)
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
        resetSearchView()
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

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return when (item?.itemId) {
//            R.id.search_todo -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onBackPressed() {
        resetSearchView()
        super.onBackPressed()
    }

    private fun resetSearchView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
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