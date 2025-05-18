package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowHostelRepository @Inject constructor(private var database: FirebaseFirestore) {

    suspend fun fetchHostelData(id: String): AddHostelClass? {
        return try {
            val snapshot = database
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .document(id)
                .get()
                .await()

            snapshot.toObject(AddHostelClass::class.java)
        } catch (e: Exception) {
            null
        }
    }
}