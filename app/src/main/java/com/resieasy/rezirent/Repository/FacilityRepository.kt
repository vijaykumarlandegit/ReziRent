package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.FacilityClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FacilityRepository @Inject constructor(private val firebase:FirebaseFirestore) {

    suspend fun fetchFacility(id:String):FacilityClass?{
        return try {
            val snapshot= firebase
                .collection("Nanded")
                .document("NandedCity")
                .collection("AllFacility")
                .document(id)
                .get()
                .await()

            snapshot.toObject(FacilityClass::class.java)
        }catch (e:Exception){
            null
        }
    }
}