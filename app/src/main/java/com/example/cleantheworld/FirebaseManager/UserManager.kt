package com.example.cleantheworld.FirebaseManager

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cleantheworld.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class UserManager {
    val db = Firebase.firestore

    fun createUserProfile(user: User){
        db.collection("users").document(user.id)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }


}