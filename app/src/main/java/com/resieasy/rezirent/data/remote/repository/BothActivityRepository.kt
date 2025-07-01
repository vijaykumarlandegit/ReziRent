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
    private var lastDocumentSnapshot: DocumentSnapshot? = null
    private var isDataEnds = false
    private val pageSize = 10
    suspend fun getNextPage():List<UnifiedResidencyClass>{

        if (isDataEnds) return emptyList()
        return try {
            val query=firebaseFirestore
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllData")
                .whereEqualTo("status","Active")
                .orderBy("Timestamp")
                .let {
                    if (lastDocumentSnapshot!=null) it.startAfter(lastDocumentSnapshot!!)
                    else it
                }
                .limit(pageSize.toLong())
                .get()
                .await()

            if (query.isEmpty){
                isDataEnds=true
                return emptyList()
            }
            lastDocumentSnapshot=query.documents.last()
            query.toObjects(UnifiedResidencyClass::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }
    fun resetPagination(){
        lastDocumentSnapshot=null
        isDataEnds=false
    }
}
