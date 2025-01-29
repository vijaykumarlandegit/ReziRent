package com.resieasy.rezirent.Class

import com.google.firebase.firestore.PropertyName


data class AddHostelClass(
    var status: String = "",
    var rtype: String = "",
    var type: String = "",
    var subtype: String = "",
    var name: String = "",
    var lowercase: String = "",
    var address: String = "",
    var area: String = "",
    var oname: String = "",
    var number: String = "",
    var whatsapp: String = "",
    var mail: String = "",
    var rent: String = "",
    var erent: String = "",
    var deposit: String = "",
    var extra: String = "",
    var more: String = "",
    var policy: String = "",
    var gopen: String = "",
    var gclose: String = "",
    var userid: String = "",
    var id: String = "",
    var f1: String = "",
    var f2: String = "",
    var f3: String = "",
    var i1: Int = 0,
    @get:PropertyName("in") var input: Int = 0,
    var period: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var time: Long? = null
)
