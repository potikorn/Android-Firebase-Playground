package th.potikorn.firebaseplayground.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.extensions.navigate
import th.potikorn.firebaseplayground.ui.auth.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnChat.setOnClickListener {
            navigate<LoginActivity> { }
        }
    }
}
