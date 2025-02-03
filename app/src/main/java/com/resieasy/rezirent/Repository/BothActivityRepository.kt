package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.BothResiClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BothActivityRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    // Fetch all data from Firebase Firestore
    suspend fun getAllData(): List<BothResiClass> {
        val querySnapshot = firebaseFirestore
            .collection("Nanded")
            .document("NandedCity")
            .collection("AllData")
            .whereEqualTo("status", "Active")
            .get()
            .await() // Coroutine call to Firebase

        return querySnapshot.documents.mapNotNull { document ->
            document.toObject(BothResiClass::class.java)
        }
    }


}
