package th.potikorn.firebaseplayground.ui.auth.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.extensions.showToast
import java.util.Arrays

class LoginActivity : BaseActivity() {

    private val RC_SIGN_IN = 123
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userViewMode: UserViewModel by lazy { bindViewModel<UserViewModel>() }

    override fun layoutToInflate(): Int = R.layout.activity_login

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        btnLogout.setOnClickListener {
            it.visibility = View.GONE
            mAuth.signOut()
            btnReLogin.visibility = View.VISIBLE
        }
        btnReLogin.setOnClickListener { openFirebaseAuthUI() }
        // Choose authentication providers
        if (mAuth.currentUser == null) {
            openFirebaseAuthUI()
        }
    }

    override fun initialize() {
        userViewMode.liveMessageData.observe(this, Observer { showToast(it) })
    }

    override fun onResume() {
        super.onResume()
        if (mAuth.currentUser != null) {
            btnLogout.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            Logger.e(response.toString())
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                userViewMode.saveFCMToken()
            } else {
                response?.let {
                    showToast("${it.error?.errorCode} : ${it.error?.message}")
                }
                finish()
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