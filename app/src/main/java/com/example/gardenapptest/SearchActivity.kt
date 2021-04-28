package com.example.gardenapptest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val myDataset = ProfileDatabase().loadAffirmations()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_profile)
        recyclerView.adapter = RecyclerAdapter(this, myDataset)
        recyclerView.setHasFixedSize(true)
    }
}
