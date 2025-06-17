package com.resieasy.rezirent.data.remote.repository

import com.google.firebase.firestore.DocumentSnapshot
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
    private var lastVisible: DocumentSnapshot? = null
    private var isLastPage = false

    suspend fun getNextPage(pageSize: Long = 20): List<UnifiedResidencyClass> {
        if (isLastPage) return emptyList()

        return try {
            val query = firebaseFirestore
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status", "Active")
                .orderBy("timestamp") // or any field for consistent ordering
                .let {
                    if (lastVisible != null) it.startAfter(lastVisible!!)
                    else it
                }
                .limit(pageSize)
                .get()
                .await()

            if (query.isEmpty) {
                isLastPage = true
                return emptyList()
            }

            lastVisible = query.documents.last()
            query.toObjects(UnifiedResidencyClass::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun resetPagination() {
        lastVisible = null
        isLastPage = false
    }
}
