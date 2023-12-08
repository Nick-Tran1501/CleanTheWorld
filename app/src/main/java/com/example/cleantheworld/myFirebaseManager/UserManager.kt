package com.example.cleantheworld.myFirebaseManager

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cleantheworld.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class UserManager {
    private val db = Firebase.firestore

    fun createUserProfile(user: User) {
        db.collection("users").document(user.id)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun getUser(userId: String): User? {
        val userData = db.collection("users").document(userId).get().await()
        var user: User? = null
        userData.let { data ->
            user = data.toObject<User>()

        }
        return user
    }
}