package com.example.android.marsphotos.network

import com.squareup.moshi.Json

data class MarsPhoto(
    //Json 객체의 key에 상응
//    val id: String, val img_src: String
    val id: String, @Json(name = "img_src") val imgSrcUrl: String
)