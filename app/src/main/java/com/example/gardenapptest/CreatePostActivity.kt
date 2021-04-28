package com.example.gardenapptest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val postContentButton: TextView = findViewById(R.id.postButton) as TextView
        postContentButton.setOnClickListener {
            storePostFirestore()
            postMessage()
        }

        val goBack: TextView = findViewById(R.id.backButton) as Button
        goBack.setOnClickListener{
            postMessage() //poorly named, actually goes back to the main screen.
        }
    }

    fun postMessage() {
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }

    fun storePostFirestore(){
        val postText: EditText = findViewById(R.id.userPostText) as EditText
        val postContents = postText.text.toString()

        //get user's email
        val user = Firebase.auth.currentUser
        val userEmail = user.email.toString()

        val dataStorage = FirebaseFirestore.getInstance()

        dataStorage.collection("users").document(userEmail).update(
            "userPost", postContents)

            .addOnSuccessListener { showToast("Successfully added") }
            .addOnFailureListener{ showToast("Failed")}
    }

    //Easy Toast
    fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }

}