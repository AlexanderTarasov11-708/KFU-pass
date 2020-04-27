package com.itis.kfupass.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.itis.kfupass.R
import com.itis.kfupass.presenters.LoginPresenter
import com.itis.kfupass.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(),
    View.OnClickListener,
    LoginView {

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @ProvidePresenterTag(presenterClass = LoginPresenter::class)
    fun provideLoginPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideLoginPresenter() = LoginPresenter()

    private lateinit var auth: FirebaseAuth
    val dbreference = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_register.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else {

        }
    }

    private fun signIn() {
        if (!TextUtils.isEmpty(etLogin.text.toString()) && !TextUtils.isEmpty(etPassword.text.toString()))
            auth.signInWithEmailAndPassword(etLogin.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        else
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> signIn()
            R.id.btn_register -> signUp()
        }
    }

    private fun signUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = "SignInActivity"
        private val RC_SIGN_IN = 9001
    }
}