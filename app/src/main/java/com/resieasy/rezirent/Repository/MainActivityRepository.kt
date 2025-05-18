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
        return try {
            val snapshot = firebase
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status", "Active")
                .whereEqualTo("rtype", "Hostel")
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(AddHostelClass::class.java)
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchRent(): List<AddFlatClass> {
        return try {
            val querySnapshot = firebase
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status", "Active")
                .whereEqualTo("rtype", "Rent")
                .get().await()

            querySnapshot.documents.mapNotNull {
                it.toObject(AddFlatClass::class.java)
            }
        } catch (e: Exception) {
            emptyList()

        }
    }

    suspend fun fetchSell(): List<SellResiClass> {
        return try {
            val querySnapshot = firebase
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status", "Active")
                .whereEqualTo("rtype", "Sell")
                .get()
                .await()

            querySnapshot.documents.mapNotNull {
                it.toObject(SellResiClass::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}