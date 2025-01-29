package com.resieasy.rezirent.Repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.resieasy.rezirent.Class.AddFlatClass
import com.resieasy.rezirent.Class.AddHostelClass
import com.resieasy.rezirent.Class.SellResiClass
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MainActivityRepository @Inject constructor(private val instance: FirebaseFirestore) {

    suspend fun fetchHostelPG():List<AddHostelClass>{
        return try{
            val snapshot =instance.collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active").whereEqualTo("rtype", "Hostel")
                .get().await()
            snapshot.documents.map {
                AddHostelClass().apply {
                    status = it.getString("status") ?: ""
                    rtype =  it.getString("rtype") ?: ""
                    type =   it.getString("type") ?: ""
                    subtype = it.getString("subtype") ?: ""
                    name = it.getString("name") ?: ""
                    lowercase = it.getString("lowercase") ?: ""
                    address = it.getString("address") ?: ""
                    area = it.getString("area") ?: ""
                    oname = it.getString("oname") ?: ""
                    number = it.getString("number") ?: ""
                    whatsapp = it.getString("whatsapp") ?: ""
                    mail = it.getString("mail") ?: ""
                    rent = it.getString("rent") ?: ""
                    erent = it.getString("erent") ?: ""
                    deposit = it.getString("deposit") ?: ""
                    extra = it.getString("extra") ?: ""
                    more = it.getString("more") ?: ""
                    policy = it.getString("policy") ?: ""
                    gopen = it.getString("gopen") ?: ""
                    gclose = it.getString("gclose") ?: ""
                    userid = it.getString("userid") ?: ""
                    id = it.getString("id") ?: ""
                    f1 = it.getString("f1") ?: ""
                    f2 = it.getString("f2") ?: ""
                    f3 = it.getString("f3") ?: ""
                    i1 = it.getLong("i1")?.toInt() ?: 0
                    input = it.getLong("in")?.toInt() ?: 0
                    period = it.getLong("period")?.toInt() ?: 0
                    latitude = it.getDouble("latitude") ?: 0.0
                    longitude =it.getDouble("longitude") ?: 0.0
                    time = it.getLong("time") ?: 0
                }
            }
        }catch (e:Exception){
            Log.e("UserViewModel", "Error loading users $e")

            emptyList()
        }
    } suspend fun fetchRent():List<AddFlatClass>{
        return try{
            val snapshot =instance.collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active").whereEqualTo("rtype", "Rent")
                .get().await()
            snapshot.documents.map {
                AddFlatClass().apply {
                    status = it.getString("status") ?: ""
                    rtype =  it.getString("rtype") ?: ""
                    type =   it.getString("type") ?: ""
                    subtype = it.getString("subtype") ?: ""
                    name = it.getString("name") ?: ""
                    lowercase = it.getString("lowercase") ?: ""
                    address = it.getString("address") ?: ""
                    area = it.getString("area") ?: ""
                    oname = it.getString("oname") ?: ""
                    number = it.getString("number") ?: ""
                    whatsapp = it.getString("whatsapp") ?: ""

                    mail = it.getString("mail") ?: ""
                    rent = it.getString("rent") ?: ""
                    erent = it.getString("erent") ?: ""
                    deposit = it.getString("deposit") ?: ""
                    extra = it.getString("extra") ?: ""
                    more = it.getString("more") ?: ""
                    policy = it.getString("policy") ?: ""

                    userid = it.getString("userid") ?: ""
                    id = it.getString("id") ?: ""
                    f1 = it.getString("f1") ?: ""
                    f2 = it.getString("f2") ?: ""
                    f3 = it.getString("f3") ?: ""
                    i1 = it.getLong("i1")?.toInt() ?: 0
                    input = it.getLong("in")?.toInt() ?: 0
                    period = it.getLong("period")?.toInt() ?: 0
                    latitude = it.getDouble("latitude") ?: 0.0
                    longitude =it.getDouble("longitude") ?: 0.0
                    time = it.getLong("time") ?: 0
                }
            }
        }catch (e:Exception){
            Log.e("UserViewModel", "Error loading users $e")

            emptyList()

        }
    }suspend fun fetchSell():List<SellResiClass>{
        return try{
            val snapshot =instance.collection("Nanded").document("NandedCity")
                .collection("AllData").whereEqualTo("status", "Active").whereEqualTo("rtype", "Sell")
                .get().await()
            snapshot.documents.map {
                SellResiClass().apply {
                    status = it.getString("status") ?: ""
                    rtype =  it.getString("rtype") ?: ""
                    type =   it.getString("type") ?: ""
                    subtype = it.getString("subtype") ?: ""
                    name = it.getString("name") ?: ""
                    lowercase = it.getString("lowercase") ?: ""
                    address = it.getString("address") ?: ""
                    area = it.getString("area") ?: ""
                    oname = it.getString("oname") ?: ""
                    number = it.getString("number") ?: ""
                    whatsapp = it.getString("whatsapp") ?: ""
                    mail = it.getString("mail") ?: ""
                    prize = it.getString("prize") ?: ""
                    eprize = it.getString("eprize") ?: ""

                    more = it.getString("more") ?: ""

                    userid = it.getString("userid") ?: ""
                    id = it.getString("id") ?: ""
                    size = it.getString("size") ?: ""
                    f1 = it.getString("f1") ?: ""
                    f2 = it.getString("f2") ?: ""
                    f3 = it.getString("f3") ?: ""
                    i1 = it.getLong("i1")?.toInt() ?: 0
                    input = it.getLong("in")?.toInt() ?: 0
                      latitude = it.getDouble("latitude") ?: 0.0
                    longitude =it.getDouble("longitude") ?: 0.0
                    time = it.getLong("time") ?: 0
                }
            }
        }catch (e:Exception){
            Log.e("UserViewModel", "Error loading users $e")

            emptyList()
        }
    }
}