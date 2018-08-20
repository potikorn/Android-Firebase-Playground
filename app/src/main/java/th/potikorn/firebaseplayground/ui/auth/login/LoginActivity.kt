package th.potikorn.firebaseplayground.ui.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import th.potikorn.firebaseplayground.R
import java.util.Arrays

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogout.setOnClickListener {
            it.visibility = View.GONE
            mAuth.signOut()
            btnReLogin.visibility = View.VISIBLE
        }
        btnReLogin.setOnClickListener { openFirebaseAuthUI() }
    }

    override fun onStart() {
        super.onStart()
        // Choose authentication providers
        if (mAuth.currentUser == null) {
            openFirebaseAuthUI()
        } else {
            btnLogout.visibility = View.VISIBLE
            Log.e("BEST", mAuth.currentUser?.displayName.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            Log.d("BEST", "ENTER THIS")
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private fun openFirebaseAuthUI() {
        val providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }
}