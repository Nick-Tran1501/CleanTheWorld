package com.example.cleantheworld.myFirebaseManager

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cleantheworld.authentication.UserAuthManager
import com.example.cleantheworld.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.messaging.FirebaseMessaging
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

    suspend fun updateUserName(userId: String, newName: String): Boolean {
        return try {
            db.collection("users").document(userId)
                .update("name", newName)
                .await()
            true
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString() ?: "Error updating user name")
            false
        }
    }

    suspend fun updateUserAge(userId: String, age: Int): Boolean {
        return try {
            db.collection("users").document(userId)
                .update("age", age)
                .await()
            true
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString() ?: "Error updating user name")
            false
        }
    }

    suspend fun updateUserPhone(userId: String, phone: String): Boolean {
        return try {
            db.collection("users").document(userId)
                .update("phone", phone)
                .await()
            true
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString() ?: "Error updating user name")
            false
        }
    }

    suspend fun getUsersByIds(userIds: List<String>): List<User> {
        return try {
            val userList = mutableListOf<User>()
            userIds.chunked(10).forEach { chunk ->
                val snapshot =
                    db.collection("users").whereIn(FieldPath.documentId(), chunk).get().await()
                userList.addAll(snapshot.toObjects())
            }
            Log.d("UsersLists", " $userList")
            userList
        } catch (e: Exception) {
            Log.i(TAG, e.message ?: "Fail to fetch users")
            emptyList()
        }
    }

    private fun sendFCMTokenToFirebase(token: String?) {
        val fbUser = UserAuthManager.getCurUserId()
        fbUser.let {
            val devToken = hashMapOf(
                "token" to token,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("deviceTokens").document(it!!).set(devToken)
        }

    }

    fun sendFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.i("MessagingService", "New token: $token")
            sendFCMTokenToFirebase(token)
        }
    }
}