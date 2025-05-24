package com.resieasy.rezirent.data.remote.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.data.remote.firebase.BothResiClass
import com.resieasy.rezirent.data.remote.firebase.UnifiedResidencyClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BothActivityRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) {

    suspend fun getAllData(): List<UnifiedResidencyClass> {
        return try {
            val querySnapshot = firebaseFirestore
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status", "Active")
                .get()
                .await()

            querySnapshot.documents.mapNotNull { document ->
                document.toObject(UnifiedResidencyClass::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


}
