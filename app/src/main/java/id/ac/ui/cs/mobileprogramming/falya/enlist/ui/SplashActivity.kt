package id.ac.ui.cs.mobileprogramming.falya.enlist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import id.ac.ui.cs.mobileprogramming.falya.enlist.R
import id.ac.ui.cs.mobileprogramming.falya.enlist.auth.LoginActivity

/**
 * @author Bobby Akyong
 * */
class SplashActivity : Activity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}