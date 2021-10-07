
package com.example.android.marsphotos.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsphotos.network.MarsApi
import com.example.android.marsphotos.network.MarsApiService
import com.example.android.marsphotos.network.MarsPhoto
import kotlinx.coroutines.launch
import java.lang.Exception


enum class MarsApiStatus { LOADING, ERROR, DONE }

// The [ViewModel] that is attached to the [OverviewFragment].
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
//    private val _status = MutableLiveData<String>()
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status
//    val status: LiveData<String> = _status
    val status: LiveData<MarsApiStatus> = _status

//    private val _photos = MutableLiveData<MarsPhoto>()
    private val _photos = MutableLiveData<List<MarsPhoto>>()
    val photos: LiveData<List<MarsPhoto>> = _photos



    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
    // 자리 표시자 응답 업데이트. 서버에서 가져온 데이터 표시
    private fun getMarsPhotos() {
//        _status.value = "Set the Mars API status response here!"

        //코루틴 실행
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
//                val listResult = MarsApi.retrofitService.getPhotos()
//                _photos.value = MarsApi.retrofitService.getPhotos()[0]
                _photos.value = MarsApi.retrofitService.getPhotos()

//                _status.value = listResult
//                _status.value = "Success: ${listResult.size} Mars photos retrieved"
//                _status.value = "   First Mars image URL : ${_photos.value!!.imgSrcUrl}"
//                _status.value = "Success: Mars properties retrieved"
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception){
//                _status.value = "Failure: ${e.message}"
                _status.value = MarsApiStatus.ERROR
                _photos.value = listOf()
            }

        }
    }
}
