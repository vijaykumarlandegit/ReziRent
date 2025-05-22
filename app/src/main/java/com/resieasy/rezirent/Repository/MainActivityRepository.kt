package com.resieasy.rezirent.Repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import com.resieasy.rezirent.Class.SellResiClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityRepository @Inject constructor(private val firebase: FirebaseFirestore) {

    suspend fun fetchHostelPG(): List<AddHostelClass> {
        return fetchData("Hostel", AddHostelClass::class.java)
    }

    suspend fun fetchRent(): List<AddFlatClass> {
        return fetchData("Rent", AddFlatClass::class.java)
    }

    suspend fun fetchSell(): List<SellResiClass> {
        return fetchData("Sell", SellResiClass::class.java)
    }

    private suspend fun <T> fetchData(rtype: String, clazz: Class<T>): List<T> {
        return try {
            val snapshot = firebase
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status", "Active")
                .whereEqualTo("rtype", rtype)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(clazz) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
