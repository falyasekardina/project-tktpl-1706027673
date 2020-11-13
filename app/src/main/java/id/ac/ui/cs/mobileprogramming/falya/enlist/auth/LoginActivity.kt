package id.ac.ui.cs.mobileprogramming.falya.enlist.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import id.ac.ui.cs.mobileprogramming.falya.enlist.ui.ItemListActivity
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.utils.Constants.USER
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    //Google Login Request Code
    private val RC_SIGN_IN = 7
    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    //Firebase Auth
    private lateinit var mAuth: FirebaseAuth
    private var authViewModel: AuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        sign_in_button.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                // ...
            }

        }
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun createNewUser(authenticatedUser: User) {
        authViewModel!!.createUser(authenticatedUser)
        authViewModel!!.createdUserLiveData.observe(this, Observer { user: User ->
            if (user.isCreated) {
                Toast.makeText(this, user.name, Toast.LENGTH_LONG).show()
            }
            goToMainActivity(user)
        })
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@LoginActivity, LoginActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Auth Failed", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }

                // ...
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            //Do your Stuff
            startActivity(Intent(this, ItemListActivity::class.java))
        }
    }
}