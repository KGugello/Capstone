package com.example.gardenapptest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

const val USDA_ZONE_WEBSITE = "https://planthardiness.ars.usda.gov/PHZMWeb/"
const val REQUEST_CODE = 21 //used for camera integration,

class EditPageActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "KotlinActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)

        //get and display profile data
        displayUserProfileDatabase()
        
        val editZoneButton: ImageView = findViewById(R.id.imageButton) as ImageView
        editZoneButton.setOnClickListener(){
            //make the USDA_Zone spinner work
            spinnerMagic()
        }

        //Go back to the Main Activity
        val backButton: Button = findViewById(R.id.go_back_btn) as Button
        backButton.setOnClickListener {
           onBackButton()
        }

        //saves information to local text files
        val saveButton: Button = findViewById(R.id.save_btn) as Button
        saveButton.setOnClickListener{
            editProfileDatabase()
        }

        //change profile picture
        //Note: Android 11 breaks the camera functionality. Most likely permissions related.
        val profilePicture: ImageView = findViewById(R.id.edit_profile_picture) as ImageView
        profilePicture.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(takePictureIntent.resolveActivity(this.packageManager) !=null ) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
            else{
                showToast("Camera Error")
            }
        }

        //Goes to official USDA website so the user can check their zone.
        val checkButton: Button = findViewById(R.id.check_usda_btn)
        checkButton.setOnClickListener(){
            onCheckButton()
        }
    }

    //App Receives information back from the Camera App
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            val imageView: ImageView = findViewById(R.id.edit_profile_picture) as ImageView
            imageView.setImageBitmap(takenImage)

        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //makes the spinner work
    fun spinnerMagic(){
        val usda_spinner: Spinner = findViewById(R.id.usda_spinner)
        ArrayAdapter.createFromResource(
                this,
                R.array.USDA_zones_array,
                android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            usda_spinner.adapter = adapter
        }

        //Allows user to choose a different zone and saves the user's choice
        usda_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val usdaZone: TextView = findViewById(R.id.usda_zone_text) as TextView
                usdaZone.text = parent?.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                showToast("Nothing Selected")
            }
        }
    }

    //When back button pressed go back to the main activity
    fun onBackButton() {
        val intent = Intent(this, MainActivity::class.java).apply {
        }
        startActivity(intent)
    }

    //Launch website so user can find out their USDA Zone
    fun onCheckButton(){
        val url = USDA_ZONE_WEBSITE
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    fun editProfileDatabase(){

        val user = Firebase.auth.currentUser //get data on current user
        val userEmail = user.email.toString() //store user's email for later

        //Get the views
        val firstName: TextView = findViewById(R.id.first_name_edit) as TextView
        val usdaZone: TextView = findViewById(R.id.usda_zone_text) as TextView
        val shortDescription: TextView = findViewById(R.id.page_description_edit) as TextView

        //Get the text
        val nameText = firstName.text.toString() //used for full name instead of just first
        val usdaZoneText = usdaZone.text.toString()
        val shortDescriptionText = shortDescription.text.toString()

        if (nameText.isBlank()) {
            showToast("Error: Name cannot be blank")
            return
        }
        if (usdaZoneText.isBlank()) {
            showToast("Error: USDA Zone cannot be blank")
            return
        }
        if (shortDescriptionText.isBlank()) {
            showToast("Error: Description cannot be blank")
            return
        }

        val dataStorage = FirebaseFirestore.getInstance() //get current data from an instance of the database
        dataStorage.collection("users").document(userEmail).update(
            "userName", nameText,
            "userDescription", shortDescriptionText,
            "userZone", (usdaZoneText))
            //.set(profile)
            .addOnSuccessListener { showToast("Successfully Saved") }
            .addOnFailureListener{ showToast("Failed to Save")}

    }

    fun displayUserProfileDatabase() {
        val db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser //get users login info
        val userEmail = user.email.toString() //store users email for later

        val docRef = db.collection("users").document(userEmail) //get users email

        val name: TextView = findViewById(R.id.first_name_edit)
        val usdaZone: TextView = findViewById(R.id.usda_zone_text) as TextView
        val shortDescription: TextView = findViewById(R.id.page_description_edit) as TextView

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(EditPageActivity.TAG, "DocumentSnapshot data: ${document.data}")

                    name.text = (document.getString("userName"))
                    usdaZone.text = (document.getString("userZone"))
                    shortDescription.text = (document.getString("userDescription"))

                } else {
                    Log.d(EditPageActivity.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(EditPageActivity.TAG, "get failed with ", exception)
            }
    }

    //Easy Toast
    fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, text, duration).show()
    }
}






















































