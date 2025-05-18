package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.SellResiClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowSellRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun fetchSingleSellData(id: String): SellResiClass? {
        return try {
            val snapshot = firestore
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .document(id)
                .get()
                .await()
            snapshot.toObject(SellResiClass::class.java)
        } catch (e: Exception) {
            null
        }
    }
}