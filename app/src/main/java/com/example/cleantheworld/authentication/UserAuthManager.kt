package com.example.cleantheworld.authentication

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.cleantheworld.FirebaseManager.UserManager
import com.example.cleantheworld.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


object UserAuthManager {
    var auth: FirebaseAuth = Firebase.auth

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "An unknown error occurred")
                }
            }
    }

    fun registerUser(email: String, password: String, name: String, age: String, phone: String, onSuccess: () -> Unit, onError: (String) -> Unit){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser!!
                    val newUser = User(id = user.uid, email = user.email.toString(), name = name,age = age.toInt(), phone = phone, joinedSiteIds = listOf())
                    UserManager().createUserProfile(newUser)
                    Log.d(TAG, "createUserWithEmail:success")
                    onSuccess()
//                updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
//                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    onError(task.exception?.message ?: "An unknown error occurred")
//                updateUI(null)
                }
            }
    }

    fun logOutUser(){
        auth.signOut()
    }

}
