package com.example.android.marsphotos.network

import com.squareup.moshi.Json


// data 키워드를 추가하여 데이터 클래스로 생성
data class MarsPhoto(
    val id: String,
    @Json(name = "img_src") val imgSrcUrl: String
)