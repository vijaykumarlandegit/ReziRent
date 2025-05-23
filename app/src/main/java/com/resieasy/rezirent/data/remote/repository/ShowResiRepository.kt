package com.resieasy.rezirent.data.remote.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.data.remote.firebase.AddFlatClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowResiRepository @Inject constructor(private var database: FirebaseFirestore) {

    suspend fun fetchResiData(id: String): AddFlatClass? {
        return try {
            val snapshot = database
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .document(id)
                .get()
                .await()

            snapshot.toObject(AddFlatClass::class.java)
        } catch (e: Exception) {
            null
        }
    }
}