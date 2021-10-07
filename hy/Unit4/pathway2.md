
### REST(REpresentational State Transfer)
* 스테이트리스(stateless) 웹 아키텍처
* 대부분의 웹 서버가 REST 웹 아키텍처를 이용해 웹 서비스 실행
* REST 아키텍처를 제공하는 웹서비스 = RESTful 서비스 <br>
    : 표준 웹 구성요소 및 프로토콜을 사용하여 빌드됨
* 표준화된 방법으로 URI를 통해 RESTful 웹서비스에 요청이 전송됨

>URI 
> * 리소스 위치나 리소스 접근 방법을 암시하지 않고 서버의 리소스를 이름으로 식별함 <br>
> 
>URL  <br>
> * 리소스 표현을 획득하거나 표현에 대해 조치를 취하는 수단을 지정함
> * 기본 액세스 메커니즘과 네트워크 위치 모두 지정
> * http를 통해 네트워크에서 가져올 수 있으며 `엔드포인트`(/photos)로 식별되는 리소스를 참조함    



<br><br><br>
 
## 웹 서비스 요청
### HTTP
* 각 웹 서비스 요청은 URI를 포함하고 있으며 웹브라우저에서 사용하는 것과 동일한 HTTP프로토콜을 사용해 서버에 전송됨
* HTTP 요청은 해야 할 일을 서버에 알리는 작업 포함
* HTTP 작업
  * GET : 서버 데이터 검색
  * POST / PUT : 서버에 새로운 데이터 추가,생성,업데이트
  * DELETE : 서버에서 데이터 삭제
* 웹 서비스 응답은 일반적으로 XML, JSON 등 키-값 쌍으로 데이터가 구조화된 웹 형식


<br><br><br>

## Retrofit 라이브러리
<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/c9e1034e86327abd.png?hl=ko" style="width:70%; height:70%; margin-top: 20px; margin-bottom:10px;">
 
* 백엔드와 통신
* 웹 서비스를 전달하는 매개변수를 기반으로 웹 서비스 URI를 생성
* 웹 서비스 콘텐츠를 기반으로 앱의 네트워크 API 생성
* 웹 서비스에서 데이터를 가져와 디코딩 후 변환기라이브러리를 통해 라우팅
  
  <br>

* Retrofit 종속 항목 추가
  1. build.gradle(Project) > repositories >` google()`, `jcenter()` 추가
  2. build.gradle(Module) > dependencies > Retrofit 라이브러리 추가
      ```gradle
      // Retrofit
      implementation "com.squareup.retrofit2:retrofit:2.9.0"
      // Retrofit with Moshi Converter
      implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
      ```
  3. build.gradle (Module) > 자바8 언어 기능 지원 추가
      ```gradle
      compileOptions {
      sourceCompatibility JavaVersion.VERSION_1_8
      targetCompatibility JavaVersion.VERSION_1_8
      }

      kotlinOptions {
      jvmTarget = '1.8'
      }
      ```

<br><br><br>

### 인터넷 연결
* ViewModel이 웹 서비스와 통신하는 데 사용할 네트워크 계층 추가<br>
    : Retrofit 서비스 API 구현
  1. 네트워크 계층인 MarsApiService 클래스 생성
  2. 기본 URL / 변환기 팩토리가 포함된 Retrofit 객체 생성
  3. Retrofit이 웹 서버와 통신하는 방법을 설명하는 인터페이스 생성
  4. Retrofit 서비스를 만들고 나머지 부분에 관해 인스턴스를 API 서비스에 노출시킴

<br>

* 구현 방법
  1. `network` 패키지 생성 후 `MarsApiService` 코틀린 파일 생성
  2. 웹서비스 기본 URL 상수 추가
  3. Retrofit 객체 빌드 및 생성 > 변환기 팩토리 추가 > 웹서비스의 기본 URI 추가
  4. `MarsApiService` 인터페이스 정의 > 내부에 웹 서비스에서 응답 문자열을 가져옴 > 반환 유형 추가
  5. `@GET`주석으로 Retrofit에 GET요청을 알리고 웹서비스 메서드의 엔드포인트(photos) 지정
  6. `MarsApi` 공개 객체로 Retrofit 서비스 초기화 : [객체 선언](#객체-선언) 사용
  7. MarsApiService 유형의 지연초기화 속성 추가 후 초기화 : 최초 사용 시 초기화되기 위해 사용
    
  ```kotlin
  //2. 웹서비스 기본 url 상수
  private const val BASE_URL =
      "https://android-kotlin-fun-mars-server.appspot.com"

  //3
  private val retrofit = Retrofit.Builder()
      .addConverterFactory(ScalarsConverterFactory.create())
      .baseUrl(BASE_URL)
      .build()

  //4,5
  interface MarsApiService {
      @GET("photos")
      fun getPhotos() : String
  }

  //6,7
  object MarsApi {
      val retrofitService: MarsApiService by lazy {
          retrofit.create(MarsApiService::class.java)
      }
  ```

<br><br><br>

### 객체 선언
* 싱글톤 객체를 선언하는 데 사용
* 싱글톤 패턴 : 객체의 인스턴스가 하나만 생성되도록 보장. 객체의 전역 액세스 포인트 하나를 가짐
* 객체 선언 초기화 : 스레드로부터 안전하며 처음 액세스할 때 실행
* `object` 키워드 뒤 이름 지정

```kotlin
// Object declaration
object DataProviderManager {
    fun registerDataProvider(provider: DataProvider) { ... }
​
    val allDataProviders: Collection<DataProvider>
        get() = // ...
}

// To refer to the object, use its name directly.
DataProviderManager.registerDataProvider(...)
```


<br><br><br>

### ViewModel에서 웹 서비스 호출
1. MarsApiService > getPhotos() 를 suspend로 정의 
2. ViewModel 클래스 > `getMarsPhotos()` 에서 `viewModelScope.launch`로 코루틴 실행
3. 싱글톤객체 MarsApi의 인터페이스에서 getPhotos 호출 > 반환값 저장
4. 백엔드 서버에서 받은 결과를 _status.value.에 할당

* `ViewModelScope` : viewModel을 대상으로 정의된 코루틴 범위. ViewModel 삭제 시 자동 취소

```kotlin
private fun getMarsPhotos() {
    viewModelScope.launch {
        val listResult = MarsApi.retrofitService.getPhotos()
        _status.value = listResult
    }
}
```
 

<br><br><br>

### `INTERNET` 권한
manifests/AndroidManifest.xml > application태그 앞에 권한 선언
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
 

<br><br><br>

### 예외 처리
* 예외 : 런타임 시에 발생할 수 있는 오류. 사용자에게 알리지 않고 앱을 종료함.
* 서버 연결시 발생할 수 있는 문제
  * API에 사용된 URL/URI 가 잘못됨
  * 서버를 사용할 수 없어 서버에 연결 불가능
  * 네트워크 지연 문제
  * 기기의 인터넷 연결 문제
* try - catch 블록 구문
  * try : 예외가 발생한 것으로 예상되는 코드 실행
  * catch : 앱이 갑자기 종료되는 것을 방지하는 코드 구현

<br><br><br>

### JSON
<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/68fdfa54410ee03e.png?hl=ko" style="width:70%; height:70%; margin-top: 20px;"> 

* JSON 응답 : 대괄호로 표시된 배열. 배열에는 JSON 객체가 포함됨.
* JSON 객체 : 중괄호로 묶여있음. 각 객체에는 콜론으로 구분되는 이름-값 쌍의 집합이 포함됨. 
* name : 따옴표로 묶여 있음.
* value : 숫자, 문자열, 부울, 배열, 객체(JSON 객체) 또는 null
  

<br><br><br>

### Moshi 라이브러리
* 종속 항목 추가
  1. build.gradle(Module) > dependencies > Moshi 라이브러리 추가
  2. converter-moshi 변환기로 변경
  ```gradle
  // Moshi
  implementation 'com.squareup.moshi:moshi-kotlin:1.9.3'
  // Retrofit with Moshi Converter
  implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
  ```

<br>  

* JSON 데이터를 파싱하여 코틀린 객체로 변경
  1. network > `MarsPhoto` 클래스 생성
  2. `data` 키워드를 추가하여 데이터 클래스로 변경
  3. JSON 객체에 해당되는 속성 추가 > `@JSON` 주석을 통해 변수 매핑
  ```kotlin
  data class MarsPhoto(
      val id: String, 
      @Json(name = "img_src") val imgSrcUrl: String
  )
  ```
* MarsApiService / ViewModel 에서 Moshi객체 사용


<br><br><br>

### Coil 라이브러리
* 필요 요소
  * 로드하고 표시할 이미지 URL
  * 이미지를 실제로 표시하는 ImageView 객체
  
<br>

* 종속 항목 추가
  1. build.gradle(Module) > dependencies > Moshi 라이브러리 추가
      ```gradle
      // Coil
      implementation "io.coil-kt:coil:1.1.1"
      ```
  2. build.gradle(Project) > repositories > `mavenCentral()` 추가 

<br>  

* ViewModel 업데이트
  1. ViewModel에서 MarsPhoto 객체를 저장할 수 있는 `_photos`속성 추가
  2. `photos` 공개 지원필드 추가
  3. getMarsPhotos() 메서드 내 웹서비스에서 검색되는 데이터를 저장하는 변수에 첫번째 사진 할당

  ```kotlin
  class OverviewViewModel : ViewModel() {
      private val _photos = MutableLiveData<MarsPhoto>()
      val photos: LiveData<List<MarsPhoto>> = _photos
      ...
      
      private fun getMarsPhotos() {
          viewModelScope.launch {
              _status.value = MarsApiStatus.LOADING
              try {
                  _photos.value = MarsApi.retrofitService.getPhotos()[0]
                  _status.value = "First Mars image URL : ${_photos.value!!.imgSrcUrl}"
              } 
      ...
  }        
  ```

<br><br><br>

### 결합 어댑터
* 뷰의 맞춤 속성을 위한 맞춤 setter를 만드는 데 사용되는 주석 처리된 메서드
* 예시 코드
    ```kotlin
    @BindingAdapter("imageUrl")
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            // Load the image in the background using Coil.
            }
        }
    }
    ```
    * `@BindingAdapter` : 속성 이름을 매개변수로 사용합니다.
    * `bindImage()` : 첫 번째 매개변수 - 타겟 뷰 유형, 두 번째 매개변수 - 속성에 설정되는 값

<br>

* 생성
  1. `BindingAdapter` 코틀린 파일 생성
  2. 매개변수로 ImageView, String 을 사용하는 `bindImage()` 메서드 생성
  3. 메서드에 `@BindingAdapter` 주석 추가
  4. 메서드 내부에서 `imageURL` 인수에 [let](#let-범위-함수) 블럭 추가
  5. toUri() : URL 문자열을 Uri 객체로 변환 <br>
     buildUpon.scheme("https") : HTTPS 스키마 사용
  6. Coil의 `load(){}` 를 사용해 imgUri 객체에서 imgView로 이미지 로드
  7. grid_view_item.xml > ImageView요소 위 ViewModel의 data요소 추가
  8. ImageView요소에 `app : imageUrl` 속성 추가
  

  ```kotlin
  //BindingAdapter.kt
  @BindingAdapter("imageUrl")
  fun bindImage(imgView: ImageView, imgUrl: String?) {
      imgUrl?.let {
          val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
          imgView.load(imgUri)
      }
  }
  ```

  ```xml
  <!-- grid_view_item.xml -->
  <data>
  <variable
      name="viewModel"
      type="com.example.android.marsphotos.overview.OverviewViewModel" />
  </data>
  <ImageView
      android:id="@+id/mars_image"
      ...
      app:imageUrl="@{viewModel.photos.imgSrcUrl}"
      ... />
  ```


<br><br><br>

### `let` 범위 함수
* 객체의 context 내에서 코드 블럭 실행 가능
* 호출 체인의 결과에서 함수를 하나 이상 호출 하는데 사용
* `?.` 안전호출 연산자와 함께 객체에서 null 안전 연산을 실행하는데 사용 <br>
  : 객체가 null 이 아닌 경우에만 코드 블럭 실행

 

<br><br><br>

### 로딩이미지/오류이미지 추가
* BindingAdapters > bindImage() > imgView.load 업데이트

```kotlin
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}
```
<br><br><br>

### 그리드 레이아웃
* 세로 스크롤 사용 시 행의 각 항목은 스팬 하나를 차지함
* 아래 예 : 각 행 = 스팬 3개
  
<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-internet-images/img/2262933d015af49c.png?hl=ko" style="width:30%;  margin-left:30px; margin-bottom: 20px;">



  <br>

* Recyclerview 추가
  1. grid_view_item.xml > data > `MarsPhoto` 유형의 변수 추가
  2. ImageView요소 > `app:imageUrl="@{photo.imgSrcUrl}"` 속성 변경
  3. fragment_overview.xml > RecyclerView 요소 추가 <br>
      app:layoutManager= "androidx.recyclerview.widget.GridLayoutManager" <br>
      app:spanCount="2" - 한 행에 2개의 열<br>
      android:clipToPadding="false" - 내부 콘텐츠를 패딩에 맞춰 자르지 않도록 설정
      
  4. 미리보기 설정 <br>
     tools:itemCount="16" - 레이아웃에 표시되는 항목의 수 <br>
     tools:listitem="@layout/grid_view_item" 
  ```xml
  <!-- fragment_overview.xml -->
  <androidx.recyclerview.widget.RecyclerView
      android:id="@+id/photos_grid"
      android:layout_width="0dp"
      android:layout_height="0dp"
      android:clipToPadding="false"
      app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintLeft_toLeftOf="parent"
      app:layout_constraintRight_toRightOf="parent"
      app:layout_constraintTop_toTopOf="parent"
      app:spanCount="2" 
      tools:itemCount="16"
      tools:listitem="@layout/grid_view_item" />
  ```


<br><br><br>

### `enum`
* 열거의 단축형. 컬렉션의 모든 항목을 순서가 지정된 목록으로 나열
* 상수 집합을 보유할 수 있는 데이터 유형
```kotlin
//definition
enum class Direction {
    NORTH, SOUTH, WEST, EAST
} 

//usage
var direction = Direction.NORTH;
```  

<br><br><br>

### 상태 ImageView 추가
* ViewModel에 상태 추가
  1. ViewModel 클래스의 import 다음 enum 클래스 정의 추가
  2. `_status`, `status` 속성을 MarsApiStatus 로 변경
  3. `getMarsPhotos()`에서 문자열을 `MarsApiStatus.DONE`/`MarsApiStatus.ERROR` 로 변경
  4. viewModelScope.launch > try-catch 블럭 이전 `MarsApiStatus.LOADING` 추가
  5. catch 블럭 오류 상태 다음 _photos를 빈 리스트로 설정 : RecyclerView 삭제됨
  6.  fragment_overview.xml > RecyclerView > ImageView 추가

  ```kotlin
  enum class MarsApiStatus { LOADING, ERROR, DONE }

  class OverviewViewModel : ViewModel() {
      ...
      private fun getMarsPhotos() {
      viewModelScope.launch {
              _status.value = MarsApiStatus.LOADING
              try {
              _photos.value = MarsApi.retrofitService.getPhotos()
              _status.value = MarsApiStatus.DONE
              } catch (e: Exception) {
              _status.value = MarsApiStatus.ERROR
              _photos.value = listOf()
              }
          }
      } 
  }
  ```
    

  ```xml
  <!-- fragment_overview.xml -->
  <ImageView
     android:id="@+id/status_image"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      app:layout_constraintBottom_toBottomOf="parent"
      app:layout_constraintLeft_toLeftOf="parent"
      app:layout_constraintRight_toRightOf="parent"
      app:layout_constraintTop_toTopOf="parent"
      app:marsApiStatus="@{viewModel.status}" /> 
  ```
   
<br>

* ImageView에 결합 어댑터 추가
  1. BindingAdapters에 어댑터 추가
  2. `bindStatus()` 내부 `when` 블럭으로 서로 다른 상태 간 전환
     * MarsApiStatus.LOADING : 상태 ImageView를 visible로 설정하고 로드 애니메이션에 할당
     * MarsApiStatus.ERROR : 상태 ImageView를 visible로 설정하고 연결 오류 드로어블 할당
     * MarsApiStatus.DONE : 상태 ImageView를 View.GONE으로 설정하여 숨김

  ```kotlin
  @BindingAdapter("marsApiStatus")
  fun bindStatus(statusImageView: ImageView,
                 status: MarsApiStatus?) {
      when (status) {
          MarsApiStatus.LOADING -> {
              statusImageView.visibility = View.VISIBLE
              statusImageView.setImageResource(R.drawable.loading_animation)
          }
          MarsApiStatus.ERROR -> {
              statusImageView.visibility = View.VISIBLE
              statusImageView.setImageResource(R.drawable.ic_connection_error)
          }
          MarsApiStatus.DONE -> {
              statusImageView.visibility = View.GONE
          }
      }
  }
  ```
 
 
 
 

<br><br><br>
<hr>
<br>
 
### <퀴즈>
* DataProviderManager를 object 키워드로 선언하는 이유<br>
a. 객체를 싱글톤패턴으로 만들기 위해<br>
a. 객체의 인스턴스가 하나만 필요하므로 <br>

* Grid Layout <br>
a. 기본적으로 세로 스크롤 <br>
a. 아이콘, 이미지로 표현할 수 있는 리스트에 적합 <br>
a. DataProviderManager 타입으로 객체 참조 가능<br>

* 결합 어댑터 <br>
a. 레이아웃 속성이 할당되는 방식에 관한 맞춤 로직 구현 가능 <br>
a. @BindingAdapter 메서드는 xml 속성에 제공되는 맞춤 값 처리<br>

* Moshi 라이브러리가 JSON을 처리하는데 필요한 조건 <br>
a. JSON 객체 구조를 설명하는 데이터 클래스  <br>
a. Moshi.Builder 객체에 추가된 KotlinJsonAdapterFactory <br>
a. Retrofit.Builder 객체에 추가된 MoshiConverterFactory <br>


<br><br><br>
