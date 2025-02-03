package com.resieasy.rezirent.Repository

import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

     class ShowHostelRepository @Inject constructor(private var databse: FirebaseFirestore) {

        suspend fun fetchHostelData(id:String): AddHostelClass?{
            return try {
                val snapshot=databse.collection("Nanded")
                    .document("NandedCity").collection("AllData").document(id).get().await()
                snapshot.toObject(AddHostelClass::class.java)
            }catch (e:Exception){
                null
            }
        }
    }