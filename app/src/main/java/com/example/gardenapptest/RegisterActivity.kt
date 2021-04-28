package com.example.gardenapptest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerButton: Button = findViewById(R.id.RegisterBtn) as Button
        val registerEmail: EditText = findViewById(R.id.et_register_email) as EditText
        val registerPassword: EditText = findViewById(R.id.et_register_password) as EditText
        val backButton: Button = findViewById(R.id.back_button) as Button

        backButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener{
            when {
                //make sure email isn't blank
                TextUtils.isEmpty(registerEmail.text.toString().trim { it <= ' ' }) -> {
                    showToast("Please enter an email")
                }

                //make sure password isn't blank
                TextUtils.isEmpty(registerPassword.text.toString().trim { it <= ' ' }) -> {
                    showToast("Please enter a password")
                }
                else -> {
                    //take email and password and trim it to make sure the user did not leave any hanging spaces
                    val email: String = registerEmail.text.toString().trim{it <= ' ' }
                    val password: String = registerPassword.text.toString().trim{it <= ' '}

                    //Register the user and show success message
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                val firebaseUser: FirebaseUser = task.result!!.user!!

                                //create default profile
                                //This should only go off when a user is registering for the first time
                                //Creates a default profile with all fields set to "Default"
                                storePostFirestore()

                                val intent = Intent(this@RegisterActivity, MainActivity::class.java) // upon registering, sends user to the main activity
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clears unused activities
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
    //create default profile, uses email as firebase document name and sets document fields to "Default"
    fun storePostFirestore(){
        val regEmail: EditText = findViewById(R.id.et_register_email) as EditText
        val userEmail = regEmail.text.toString()

        val dataStorage = FirebaseFirestore.getInstance()
        val profile = hashMapOf(
            "userName" to "Name",
            "userDescription" to "Default",
            "userPost" to "Default",
            "userZone" to "USDA Zone: ?"
        )

        dataStorage.collection("users").document(userEmail)
            .set(profile)
            .addOnSuccessListener { showToast("Successfully added") }
            .addOnFailureListener{ showToast("Failed")}

    }

    //Easy Toast
    fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }

    //Buttons will make the pop noise using on click without actually doing anything
    fun buttonPop(view: View){
        println("I just like the noise.")
    }
}