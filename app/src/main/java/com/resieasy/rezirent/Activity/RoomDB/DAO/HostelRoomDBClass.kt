package com.resieasy.rezirent.Activity.RoomDB.DAO

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import javax.annotation.Nonnull

@Entity(tableName = "hostelRoomDB")
data class HostelRoomDBClass (
    @PrimaryKey
    @Nonnull
    var mail: String,
    var status: String? = null,
    var rtype: String? = null,
    var type: String? = null,
    var subtype: String? =null,
    var name: String = "",
    var lowercase: String = "",
    var address: String = "",
    var area: String = "",
    var oname: String = "",
    var number: String? = null,
    var whatsapp: String? = null,
    var rent: String = "",
    var erent: String = "",
    var deposit: String = "",
    var extra: String = "",
    var more: String = "",
    var policy: String = "",
    var gopen: String = "",
    var gclose: String = "",
    var userid: String? = null,
    var id: String? = null,
    var f1: String = "",
    var f2: String = "",
    var f3: String = "",
    var i1: Int = 0,
    @get:PropertyName("in") var input: Int = 0,
    var period: Int = 0,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var time: Long = 0L
)