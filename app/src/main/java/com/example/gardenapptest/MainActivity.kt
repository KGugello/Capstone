/*
    Kane Gugello
    Gardening App for Capstone Project
    Last updated: 28 April 2021
*/

package com.example.gardenapptest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gardenapptest.EditPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()

        displayUserProfileDatabase()

        //user presses the create post button
        val createPostButton: TextView = findViewById(R.id.create_post_button) as TextView
        createPostButton.setOnClickListener {
              createMessage()
        }

        //user presses the Edit Page button
        val editPageButton: Button = findViewById(R.id.edit_post_button) as Button
        editPageButton.setOnClickListener {
            val intent = Intent(this, EditPageActivity::class.java)
            startActivity(intent)
        }

        //user presses the search button
        val searchButton: Button = findViewById(R.id.search_button) as Button
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        //User presses the logout button
        val logoutButton: Button = findViewById(R.id.logout_button) as Button
        logoutButton.setOnClickListener{

            //Log out of app and firebase
            FirebaseAuth.getInstance().signOut()

            //Return to log in screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    //Buttons will make the pop noise using on click without actually doing anything
    //or so I thought, may not work.
    fun buttonPop(view: View){
        println("I just like the noise.")
    }

    fun createMessage() {
        val intent = Intent(this, CreatePostActivity::class.java).apply {
        }
        startActivity(intent)
    }

    //display the user profile per the database
    fun displayUserProfileDatabase(){
        val db = FirebaseFirestore.getInstance()
        //get user's email
        val user = Firebase.auth.currentUser
        val userEmail = user.email.toString()

        //get user's email
        val docRef = db.collection("users").document(userEmail)

        val name: TextView = findViewById(R.id.name_text)
        val usdaZone: TextView = findViewById(R.id.usda_text) as TextView
        val shortDescription: TextView = findViewById(R.id.description_text) as TextView
        val userPost: TextView = findViewById(R.id.postText) as TextView

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    name.text = (document.getString("userName"))
                    usdaZone.text = (document.getString("userZone"))
                    shortDescription.text = (document.getString("userDescription"))
                    userPost.text = (document.getString("userPost"))

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    //Easy Toast
    fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }
}