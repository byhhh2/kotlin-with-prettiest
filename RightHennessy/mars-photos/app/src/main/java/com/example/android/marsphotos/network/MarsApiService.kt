package com.example.android.marsphotos.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Retrofit이 HTTP 요청을 사용하여 웹 서버와 통신하는 방법을 정의하는 인터페이스
interface MarsApiService {

    // Retrofit에 GET 요청임을 알림, 이 웹 서비스 메서드의 엔드포인터 지정 (photos)
    @GET("photos")
    //웹 서비스에서 응답 문자열 가져옴
    suspend fun getPhotos(): List<MarsPhoto>
}

// MarsApi 라는 공개 객체를 정의하여 Retrofit 서비스를 초기화
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java) }
}