package com.resieasy.rezirent.data.remote.firebase

import com.google.firebase.database.PropertyName

data class UnifiedResidencyClass(
    var status: String? = null,
    var rtype: String? = null,
    var type: String? = null,        // "hostel", "rent", or "sell"
    var subtype: String? = null,
    var name: String = "",
    var lowercase: String = "",
    var address: String = "",
    var area: String = "",
    var oname: String = "",
    var number: String? = null,
    var whatsapp: String? = null,
    var mail: String? = null,

    // Rent & Hostel
    var rent: String? = null,
    var erent: String? = null,
    var deposit: String? = null,

    // Sell only
    var prize: String? = null,
    var eprize: String? = null,
    var size: String? = null,

    // Shared optional
    var extra: String? = null,
    var more: String? = null,
    var policy: String? = null,

    // Hostel only
    var gopen: String? = null,
    var gclose: String? = null,

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
