package com.itis.kfupass.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.itis.kfupass.R
import com.itis.kfupass.models.User
import com.itis.kfupass.views.SignUpView
import kotlinx.android.synthetic.main.activity_signup.*
import com.itis.kfupass.presenters.SignUpPresenter

class SignUpActivity : AppCompatActivity(), View.OnClickListener, SignUpView {

    @InjectPresenter
    lateinit var signupPresenter: SignUpPresenter

    @ProvidePresenterTag(presenterClass = SignUpPresenter::class)
    fun provideSignupPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideSignupPresenter() = SignUpPresenter()

    private lateinit var auth: FirebaseAuth
    lateinit var dbreference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btn_save.setOnClickListener(this)
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            show()
        }
    }

    private fun signUp() {
        if (!TextUtils.isEmpty(et_login.text.toString()) &&
            !TextUtils.isEmpty(et_pw.text.toString()) &&
            !TextUtils.isEmpty(et_rpw.text.toString()) &&
            et_pw.text.toString() == et_rpw.text.toString())
        {
                val id = 0
                val usr = User()
                val query: Query =
                    FirebaseDatabase.getInstance().reference.child("Users")
                        .orderByChild("login")
                        .equalTo(usr.name)
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.childrenCount > 0) {
                            Toast.makeText(
                                baseContext, "Данный GoodReads аккаунт уже зарегистрирован",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            auth.createUserWithEmailAndPassword(
                                et_login.text.toString(),
                                et_pw.text.toString()
                            )
                                .addOnCompleteListener(this@SignUpActivity) { task ->
                                    if (task.isSuccessful) {
                                        register(usr, et_login.text.toString())
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(
                                            baseContext, "Authentication failed.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        updateUI(null)
                                    }
                                }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(
                            baseContext, "Reading data failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun register(user: User, email: String) {
        hide()
        dbreference =
            FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)
        val hashMap: HashMap<String, String?> = HashMap()
        hashMap["id"] = auth.currentUser!!.uid
        hashMap["login"] = user.login
        hashMap["name"] = user.name
        hashMap["age"] = user.age.toString()
        hashMap["email"] = email
        hashMap["grID"] = user.id
        hashMap["imageURL"] = user.imageURL

        dbreference.setValue(hashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_save -> signUp()
        }
    }

    override fun hide() {
        sign_up_layout.visibility = CoordinatorLayout.INVISIBLE
        //progressBar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        sign_up_layout.visibility = CoordinatorLayout.VISIBLE
        //progressBar.visibility = CoordinatorLayout.INVISIBLE
    }
}