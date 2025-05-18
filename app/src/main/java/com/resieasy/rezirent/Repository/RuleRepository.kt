package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.FacilityClass
import com.resieasy.rezirent.Class.RulesClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RuleRepository @Inject constructor(private var firebase: FirebaseFirestore) {

    suspend fun fetchRule(id: String): RulesClass? {
        return try {
            val snapshot = firebase
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllRule")
                .document(id)
                .get()
                .await()

            snapshot.toObject(RulesClass::class.java)
        } catch (e: Exception) {
            null
        }
    }
}