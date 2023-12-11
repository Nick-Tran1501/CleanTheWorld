package com.example.cleantheworld.myFirebaseManager

import android.content.ContentValues.TAG
import android.util.Log
import com.example.cleantheworld.models.CleanUpSite
import com.example.cleantheworld.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

object CleanUpSiteManager {
    private val db: FirebaseFirestore get() = Firebase.firestore

    fun createCleanUpSite(site: CleanUpSite) {
        db.collection("sites").document(site.id)
            .set(site)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun getAllSites(): List<CleanUpSite> {
        val sitesList: MutableList<CleanUpSite> = mutableListOf()
        val documents = db.collection("sites").get().await()
        documents.let { result ->
            for (d in result) {
                val site = d.toObject<CleanUpSite>()
                sitesList.add(site)
            }
        }
        return sitesList
    }

    suspend fun getSiteById(id: String): List<CleanUpSite> {
        val db = FirebaseFirestore.getInstance()
        val sitesList = mutableListOf<CleanUpSite>()

        try {
            // Fetch sites where the user is a participant
            val participantSites = db.collection("sites")
                .whereArrayContains("participantIds", id)
                .get()
                .await()
                .toObjects(CleanUpSite::class.java)
            sitesList.addAll(participantSites)

            // Fetch sites where the user is the admin
            val adminSites = db.collection("cleanUpSites")
                .whereEqualTo("adminId", id)
                .get()
                .await()
                .toObjects(CleanUpSite::class.java)
            sitesList.addAll(adminSites)
        } catch (e: Exception) {
            // Handle exceptions
        }

        return sitesList.distinctBy { it.id }
    }

    suspend fun getSiteDetailById(siteId: String): CleanUpSite? {
        return try {
            val documentSnapshot = db.collection("sites").document(siteId).get().await()
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(CleanUpSite::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            // Handle exceptions appropriately
            null
        }
    }

    fun joinSite(siteId: String, userId: String) {
        val siteRef = db.collection("sites").document(siteId)

        // Atomically add a new participant ID to the "participantIds" array field
        siteRef.update("participantIds", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
                // Handle success (e.g., show a success message or update UI)
            }
            .addOnFailureListener { e ->
                // Handle failure (e.g., show an error message)
            }
    }

    fun populateUserFromReferences(userId: String): User? {
        var user: User? = null
        db.collection("users").document(userId).get().addOnSuccessListener { userDocument ->
            user = userDocument.toObject<User>()
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting user: ", exception)
        }
        return user
    }

}