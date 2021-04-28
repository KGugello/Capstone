package com.example.gardenapptest

    import androidx.annotation.DrawableRes

data class ProfileData(
    val stringResourceId: String,
    val stringResourceId2: String,
    @DrawableRes val imageResourceId: Int
){}