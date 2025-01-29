package com.resieasy.rezirent.Class

import com.google.firebase.firestore.PropertyName

data class SellResiClass(
    var status: String? = null,
    var rtype: String? = null,
    var type: String? = null,
    var subtype: String? = null,
    var name: String? = null,
    var lowercase: String? = null,
    var address: String? = null,
    var area: String? = null,
    var oname: String? = null,
    var number: String? = null,
    var whatsapp: String? = null,
    var mail: String? = null,
    var prize: String? = null,
    var eprize: String? = null,
    var more: String? = null,
    var userid: String? = null,
    var id: String? = null,
    var size: String? = null,
    var f1: String? = null,
    var f2: String? = null,
    var f3: String? = null,
    var i1: Int = 0,
    @get:PropertyName("in") var input: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var time: Long? = null
)
