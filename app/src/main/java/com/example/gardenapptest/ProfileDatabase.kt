package com.example.gardenapptest

class ProfileDatabase {
    fun loadAffirmations(): List<ProfileData> {
        return listOf<ProfileData>(
            ProfileData("Kane Gugello","An heirloom plant, heirloom variety, heritage fruit, or heirloom vegetable is an old cultivar of a plant used for food that is grown and maintained by gardeners and farmers, particularly in isolated or ethnic minority communities of the Western world.", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture),
            ProfileData("Name","Description", R.drawable.ic_profile_picture)
        )
    }
}