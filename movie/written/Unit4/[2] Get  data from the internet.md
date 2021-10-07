# Get data from the internet

- HTTP & REST

![image](https://user-images.githubusercontent.com/52737532/136253393-c1163cfc-422f-47f1-875c-9e39840f9d89.png)

![image](https://user-images.githubusercontent.com/52737532/136253502-3f4493cd-c356-47d3-b820-dcf5e41117e5.png)

![image](https://user-images.githubusercontent.com/52737532/136253742-854407d2-47b3-4eef-90eb-21052420e41d.png)

![image](https://user-images.githubusercontent.com/52737532/136253841-33737e6e-69b1-427a-8c0d-8e3aaab015a1.png)

![image](https://user-images.githubusercontent.com/52737532/136253940-6d066187-ecfc-4151-a95c-dd127efcc25b.png)

![image](https://user-images.githubusercontent.com/52737532/136254055-db30de37-0c47-4404-9621-ee796fb1ddc7.png)

<br>

## MarsPhotos 앱 개요

- 화성 표면의 이미지를 웹 서비스와 연결하여 검색하고 표시한다.
- 데이터가 올바르게 검색되고 파싱되도록 백앤드 서버에서 받은 수의 사진만 텍스트 뷰에 출력

<br>
<br>

```
> MainActivity
    > OverviewFragment - OverviewViewModel
```

- `OverviewFragment`

  - `MainActivity`내에 표시되는 fragment
  - `OverviewViewModel`를 참조
  - 데이터 결합을 사용하여 `fragment_overview` 레이아웃을 확장
  - 수명 주기 소유자를 할당하여 LiveData 변경 감지

- `OverviewViewModel`

  - `getMarsPhotos()` : 서버에서 가져온 데이터를 표시할 메서드

<br>
<br>

## Retrofit

- **백앤드 서버와 통신하는** 네트워크 서비스의 레이어를 만들고 이러한 구현을 위해 Retrofit 라이브러리 사용
- `OverviewViewModel`에서 네트워크를 호출하여 화성 사진 데이터를 가져오고 `ViewModel`에서 수명 주기 인식 데이터 결합과 함께 `LiveData`를 사용하여 앱 UI를 업데이트
- Retrofit은 웹 서비스에 전달하는 매개 변수를 기반으로 웹 서비스의 URI를 만든다.

![image](https://user-images.githubusercontent.com/52737532/136256232-38018f5c-361c-4d81-b0c6-554fb6b93fb1.png)

![image](https://user-images.githubusercontent.com/52737532/136258498-03664988-27f6-4d1d-8cbd-ec070c25fcdb.png)

<br>
<br>

### 종속 설정

```
//build.gradle (project)
repositories {
   google()
   jcenter()
}
```

```
//build.gradle (module)

// Retrofit
implementation "com.squareup.retrofit2:retrofit:2.9.0"
// Retrofit with Moshi Converter
implementation "com.squareup.retrofit2:converter-scalars:2.9.0" //JSON -> String

...

android {
  ...

  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = '1.8'
  }
}// 많은 타사 라이브러리가 자바 8 언어기능을 사용하므로 자바 8을 위한 지원
```

<br>
<br>

---

### REST

- stateless 웹 아키텍서
- URI를 통해 RESTful 웹 서비스에 요청 전송

### URI

- 리소스 위치나 리소스에 엑세스하는 방법을 암시하지 않고 서버의 리소스를 **이름으로 식별**

```
https://android-kotlin-fun-mars-server.appspot.com/photos
//화성 사진의 목록을 가져온다.
// </photo> 등 으로 식별되는 리소스 참조 (= 엔드포인트)
```

### 웹 서비스 요청

- 각 웹 서비스 요청은 URI를 포함한다.
- HTTP 프로토콜을 사용하여 서버에 전송된다.
- HTTP 요청 : 해야할 일을 서버에 알림
  - GET : 서버 데이터 검색
  - POST : 서버에 새로운 데이터 추가
  - PUT : 서버에 데이터 업데이트
  - DELETE : 서버에서 데이터 삭제

```
앱이 화성 사진 정보를 가져오라고 요청 (HTTP GET) <-> 서버는 이미지 URL을 포함한 응답을 반환
// 응답 : XML 또는 JSON
```

---

<br>
<br>

## 인터넷에 연결하기

- Retrofit : 매개 변수를 기반으로 웹 서비스의 URI를 만들고, JSON응답을 String 객체로 만든다.

<br>

### 네트워크 계층인 MarsApiService 클래스 만들기

- new package : network | class : network/MarsApiService.kt

```kotlin
//MarsApiService.kt

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com" //[1]

private val retrofit = Retrofit.Builder() //[2]
    .addConverterFactory(ScalarsConverterFactory.create()) //[3]
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService { //[4]
    @GET("photos") //[5]
    suspend fun getPhotos(): String //[6]
}

object MarsApi { //[7] 싱글톤 객체
    val retrofitService : MarsApiService by lazy { //[8]
        retrofit.create(MarsApiService::class.java) //[9]
    }
}
```

- [1] : 웹 서비스 기본 URL
- [2] : Retrofit 객체를 빌드하고 만듦
- [3] : JSON->String 하는 converter
- [4] : HTTP 요청을 사용해서 웹 서버와 통신하는 방법을 정의하는 MarsApiService라는 인터페이스 정의
- [5] : Retrofit에 GET 요청임을 알리고 이 웹 서비스 메서드의 엔드포인트를 지정 (/photos)
- [6] : 반환 타입 String인 getPhotos() 메서드 => getPhotos() 메서드가 호출되면 Retrofit은 요청을 시작 (기본 URL에서 앤드포인트 추가) 왜 suspend인지는 뒤에 참고
- [8] : 최초 사용 시 초기화되도록 하기 위해 지연 초기화 사용
- [9] : `MarsApiService` 인터페이스와 함께 `retrofit.create()` 메서드를 사용하여 `retrofitService` 변수를 초기화

> 빋드 : 컴파일된 코드를 실제 실행할 수 있는 상태로 만듦

<br>

---

### 객체 선언 (싱글톤 객체)

- 싱글톤 객체를 선언하는데에 사용
- 객체 인스턴스가 하나임을 보장
- 전역 엑세스 포인트 하나를 가진다.
- 이를 사용하는 이유는 Retrofit 객체에서 create() 함수를 호출하는 데는 리소스가 많이 들고, 앱에는 Retrofit API 서비스의 인스턴스가 하나만 필요하기 때문
- 객체 선언을 사용하여 나머지 앱의 나머지 부분에 서비스를 **노출**한다.

### lazy

- 호출 시점에서 최초 1회 초기화 (val만 사용가능)
- lateinit과 다르게 선언과 동시에 초기화 해야한다.
- **변수를 선언과 동시에 초기화 하기만 호출 시점에서 초기화가 이뤄진다는 것**

---

<br>

### ViewModel에서 웹 서비스 호출

```kotlin
//MarsApiService.kt

interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): String
}
```

- 코루틴 내에서 호출될 수 있게 `getPhotos()`를 `suspend`로 지정

<br>

```kotlin
//OverviewViewModel.kt

    private fun getMarsPhotos() {
        viewModelScope.launch { //[1]
            val listResult = MarsApi.retrofitService.getPhotos() //[2]
            _status.value = listResult //[3]
        }
    }
```

- [1] : 코루틴을 실행
- [2] : 싱글톤 객체 `MarsApi`를 사용하여 `retrofitService` 인터페이스에서 `getPhotos()` 메서드를 호출, 반환된 응답을 `listResult`라는 val에 저장
- [3] : 백앤드에서 받아온 결과 저장

<br>
<br>

### 인터넷 권한 주기

- `manifests/AndroidManifest.xml` 의 `<application>` 태그 바로 앞에 코드 추가

```
<uses-permission android:name="android.permission.INTERNET" />
```

<br>

### 예외 처리

- try-catch 사용

```kotlin
try {
    // some code that can cause an exception.
}
catch (e: SomeException) {
    // handle the exception to avoid abrupt termination.
}

//ex

viewModelScope.launch {
   try {
       val listResult = MarsApi.retrofitService.getPhotos()
       _status.value = listResult
   } catch (e: Exception) {

   }
}
```

<br>
<br>

## Moshi로 JSON 파싱

- 일반적인 데이터는 XML 또는 JSON으로 응답된다.
- JSON을 Kotlin객체로 바꾸기 위해 `Moshi`를 사용한다.

<br>

### 종속 추가

```
//build.gradle (module)

// Moshi
implementation 'com.squareup.moshi:moshi-kotlin:1.9.3'

// Retrofit with Moshi Converter
implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'

```

<br>

### 데이터 클래스 MarsPhoto추가

- 파싱한 결과를 저장하기 위한 데이터 클래스
- network 패키지에서 new class : MarsPhoto

```kotlin
data class MarsPhoto (
    val id: String, @Json(name = "img_src") val imgSrcUrl: String
)
```

- `@Json` 주석 : kotlin은 카멜 표기법을 따르기 때문에 json 키 이름과 kotlin 속성을 맞춰주기 위해 사용

<br>

### MarsApiService & OverviewViewModel 업데이트

```kotlin
private val moshi = Moshi.Builder() //Moshi 객체 생성
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi)) //Moshi converter 사용
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto> //JSON 배열(문자열) -> 객체 배열이므로 객체 목록을 반환하도록 수정
}
```

<br>
<br>

## 인터넷 이미지 표시하기

- 웹 URL에서 이미지를 표시하려면 이미지를 다운로드, 버퍼링 및 디코딩 하고 캐시해야한다.
- 이를 위해 coil 라이브러리 사용

<br>

### coil

- 이미지를 다운로드, 버퍼링 및 디코딩 하고 캐시
- 필요로 하는 것 : 이미지의 URL, 이미지를 표시하는 ImageView 객체

```
//build.gradle (module)

// Coil
implementation "io.coil-kt:coil:1.1.1"

repositories {
   google()
   jcenter()
   mavenCentral() //
}

```

<br>

<br>

<br>

### ViewModel 업데이트

- 하나의 사진의 URL을 가져오도록 수정

```kotlin
    private val _photos = MutableLiveData<MarsPhoto>()
    val photos: LiveData<MarsPhoto> = _photos

    private fun getMarsPhotos() {
        //_status.value = "Set the Mars API status response here!"
        viewModelScope.launch {
            try {
                val listResult = MarsApi.retrofitService.getPhotos()
                _photos.value = MarsApi.retrofitService.getPhotos()[0]
                _status.value = "   First Mars image URL : ${_photos.value!!.imgSrcUrl}"
            }

    ..
```

<br>
<br>

### 결합 어댑터 사용하기

- xml에서 `android:text="Sample Text"`를 사용하면 자동으로 `setText(String: text)` 메서드를 통해 text 속성과 같은 이름의 setter를 찾는다.
- 결합 어댑터를 사용하여 유사한 동작을 **맞춤 설정** 할 수 있다.
- 즉 xml에서 맞춤 설정 함수를 쓰기 위해서 사용

<br>

- `com.example.android.marsphotos`패키지에서 `BindingAdapters`라는 `Kotlin` 파일을 만듦

```kotlin
//BindingAdapters의 클래스 밖

@BindingAdapter("imageUrl") //뷰 항목에 imageUrl 속성이 있는 경우 이 결합 어댑터를 실행하도록
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        // URL문자를 Uri 객체로 변환 한 후 HTTPS 스키마를 사용하게 하고 빌드

        imgView.load(imgUri) //coli의 load로 imgUri 객체에서 imgView로 이미지를 로드
        {//후행 람다
            placeholder(R.drawable.loading_animation) //로드이미지
            error(R.drawable.ic_broken_image) //에러 이미지
        }
    }
}
```

- let : 객체의 컨텍스트 내에서 코드블록 실행가능

<br>

### 바인딩 바꾸고 xml파일 바꾸기

```kotlin
//OverviewFragment

val binding = GridViewItemBinding.inflate(inflater) //grid_view_item.xml

```

<br>

```xml
<!-- grid_view_item.xml --> <!-- GridViewItem -->

<data>
   <variable
       name="viewModel"
       type="com.example.android.marsphotos.overview.OverviewViewModel" />
</data>

...

<ImageView
        android:id="@+id/mars_image"
        ...
        app:imageUrl="@{viewModel.photos.imgSrcUrl}"
        ... />
```

<br>
<br>
<br>

## `RecyclerView` 를 사용하여 이미지 그리드 표시

- `_photos` 유형을 `MarsPhoto` 객체 목록으로 변경

<br>

### | `GridLayout`

- `RecyclerView`의 `GridLayoutManager`는 데이터를 스크롤 가능한 그리드로 배치
- 스팬 : 행에 몇개의 데이터를 배치할 지 (`app:spanCount="2"`)
- 항목 정렬은 `LayoutManager` 에서 하는 일

### | 데이터를 `RecyclerView` 어댑터를 통해 `RecyclerView`에 결합

<br>

---

### `ListAdapter`

- `RecyclerView` 구현을 위해서 `Adapter`와 `ViewHolder` 구현해야함
- `ViewHolder`는 `Adapter`의 내부클래스로 구현
- `RecyclerView.Adapter` 클래스의 서브클래스
- 리스트 데이터를 `RecyclerView`에 표시하기 위함인데, 스레드의 리스트 간 차이를 계산하는 작업을 포함한다.
- 새로운 `PhotoGridAdapter.kt` 클래스 생성
- **리스트 데이터를 뷰로 전환하는 어댑터**
- ViewHolder : `RecyclerView` 용 뷰 풀
- <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit2/%5B3%5D%20Display%20a%20scrollable%20list.md#%EC%A0%95%EB%A6%AC">Adapter</a>

```kotlin
class PhotoGridAdapter : ListAdapter<MarsPhoto,
        PhotoGridAdapter.MarsPhotoViewHolder>(DiffCallback) {


}
```

---

<br>

```kotlin
class PhotoGridAdapter : ListAdapter<MarsPhoto,
        PhotoGridAdapter.MarsPhotoViewHolder>(DiffCallback) {
            //adapter 구현을 위해 ListAdapter를 확장하고 , MarsPhoto 타입, ViewHolder,  DiffUtil.ItemCallback구현 필요
            // onCreateViewHolder, onBindViewHolder메서드 구현 필요


    companion object DiffCallback : DiffUtil.ItemCallback<MarsPhoto>() { //컴패니언 객체
        // 두 화성 사진의 객체를 비교
        //  DiffUtil은 아래의 메서드를 사용하여 새 MarsPhoto 객체가 이전 MarsPhoto 객체와 동일한지 확인

        override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean { //비교기 메서드
            return oldItem.id == newItem.id
        }

        //데이터가 동일한지 아려고
        override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean { // 비교기 메서드
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

    }


    override fun onCreateViewHolder( //adapter 필수 구현 메서드
        parent: ViewGroup,
        viewType: Int
    ): PhotoGridAdapter.MarsPhotoViewHolder { // 반환 타입 MarsPhotoViewHolder
        return MarsPhotoViewHolder(GridViewItemBinding.inflate(
            LayoutInflater.from(parent.context))) //LayoutInflater를 사용하여 생성된 새 MarsPhotoViewHolder를 반환
    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.MarsPhotoViewHolder, position: Int) {//adapter 필수 구현 메서드
        val marsPhoto = getItem(position) //getItem()을 호출하여 현재 RecyclerView 위치와 연결된 MarsPhoto 객체를 가져온 다음
        holder.bind(marsPhoto) // 이 속성을 MarsPhotoViewHolder의 bind() 메서드에 전달
    }


    //ViewHolder (adapter의 내부 클래스 )
    class MarsPhotoViewHolder(private var binding:
                              GridViewItemBinding
    )://MarsPhoto를 레이아웃에 결합하기 위한 GridViewItemBinding 변수가 필요 (grid_view_item)
        RecyclerView.ViewHolder(binding.root) {
        fun bind(MarsPhoto: MarsPhoto) {
            binding.photo = MarsPhoto //photo로 사용
            binding.executePendingBindings() //업데이트 즉시 실행
        }
    }
}
```

- companion 객체 : static 같은 너낌

<br>
<br>

### Adapter를 바인딩하는 곳에서 결합 어댑터 추가 (`PhotoGridAdapter`)

```kotlin
//BindingAdapters.kt

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,
                     data: List<MarsPhoto>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data) //새 list를 사용할 수 있을 떄에 RecyclerView에게 알려줌
}
```

```kotlin
//OverviewFragment.kt
binding.photosGrid.adapter = PhotoGridAdapter() //PhotoGridAdapter 객체로 초기화
```

<br>
<br>
