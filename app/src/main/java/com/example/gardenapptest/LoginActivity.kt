package com.example.gardenapptest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.loginBtn) as Button
        loginButton.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.RegisterBtn) as Button
        registerButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val userEmail: EditText = findViewById(R.id.et_user_email) as EditText
        val userPassword: EditText = findViewById(R.id.et_user_password) as EditText

        loginButton.setOnClickListener{
            when {
                //make sure email isn't blank
                TextUtils.isEmpty(userEmail.text.toString().trim { it <= ' ' }) -> {
                    showToast("Please enter an email")
                }

                //make sure password isn't blank
                TextUtils.isEmpty(userPassword.text.toString().trim { it <= ' ' }) -> {
                    showToast("Please enter a password")
                }
                else -> {
                    //take email and password and trim it to make sure the user did not leave any hanging spaces
                    val email: String = userEmail.text.toString().trim{it <= ' ' }
                    val password: String = userPassword.text.toString().trim{it <= ' '}

                    //Login with email and password
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                val intent = Intent(this@LoginActivity, MainActivity::class.java) // upon registering, sends user to the main activity
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clears unused activities
                                //intent.putExtra("user_id", firebaseUser.uid)
                                //intent.putExtra("email_id",email)
                                startActivity(intent)
                                finish()
                            } else{
                                showToast(task.exception!!.message.toString())
                                //showToast("Registration Broken!")
                            }
                        }


                }
            }

        }

    }

    //Easy Toast
    fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }
}