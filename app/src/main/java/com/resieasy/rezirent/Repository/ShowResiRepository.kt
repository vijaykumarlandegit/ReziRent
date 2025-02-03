package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.AddFlatClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShowResiRepository @Inject constructor(private var databse:FirebaseFirestore) {

    suspend fun fetchResiData(id:String):AddFlatClass?{
        return try {
            val snapshot=databse.collection("Nanded")
                .document("NandedCity").collection("AllData").document(id).get().await()
            snapshot.toObject(AddFlatClass::class.java)
        }catch (e:Exception){
            null
        }
    }
}