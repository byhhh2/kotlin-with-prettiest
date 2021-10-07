# Pathway2

- Response
    - 200~299 : Success
    - 400~499 : Client Error
    - 500~599 : Server Error

---

### 인터넷에서 데이터 가져오기

- REST
    - REST
        
        REpresentational State Transfer
        
        REST 아키텍처를 제공하는 웹 서비스를 RESTful 서비스 라고 한다.
        
    
    - HTTP 작업
        - GET : 서버 데이터 검색
        - POST / PUT : 서버에 새로운 데이터를 추가/생성/업데이트
        - DELETE : 서버에서 데이터 삭제
    
    - Retrofit 라이브러리
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/c9e1034e86327abd.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/c9e1034e86327abd.png?hl=ko)
        
        [Retrofit](https://square.github.io/retrofit/)
        
        - `build.gradle(Module: MarsPhots.app)` 수정
            
            ```kotlin
            dependencies{
            		...
            		// Retrofit
            		implementation "com.squareup.retrofit2:retrofit:2.9.0"
            		// Retrofit with Moshi Converter
            		implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
            }
            
            //자바 8 기능 사용을 위한 추가
            android {
              ...
              compileOptions {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
              }
            
              kotlinOptions {
                jvmTarget = '1.8'
              }
            }
            ```
            
- 인터넷 연결하기
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/64fe0b5717a31f71.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/64fe0b5717a31f71.png?hl=ko)
    
    - 동작 순서
        1. 네트워크 계층인 `MarsApiService` 클래스 생성
            - 웹 서비스 기본 URL 추가
                
                ```kotlin
                private const val BASE_URL =
                   "https://android-kotlin-fun-mars-server.appspot.com"
                ```
                
        2. Retrofit 객체 빌드 및 생성
            
            ```kotlin
            private val retrofit = Retrofit.Builder()
            ```
            
            웹 서비스의 기본 URI 및 변환기 팩토리가 있어야 웹 서비스 API를 빌드할 수 있다.
            
            변환기 : 웹 서비스에서 얻은 데이터로 해야 할 일을 Retrofit에 알림
            
            예시 어플에서의 진행
            
            ```kotlin
            private val retrofit = Retrofit.Builder()
               .addConverterFactory(ScalarsConverterFactory.create())
               .baseUrl(BASE_URL)
               .build()
            ```
            
            → 웹 서비스의 JSON 응답을 String으로 반환
            
            → Retrofit의 `ScalarsConverter` 사용
            
            `ScalarsConverter` 
            
            Response를 String 형태로 받을 수 있다.
            
            → baseUrl()로 웹 서비스의 기본 URI 추가
            
        
        1. Retrofit 이 웹 서버와 통신하는 방법 정의
            - `MarsApiService` 인터페이스 정의
                
                ```kotlin
                interface MarsApiService {
                    @GET("photos")
                    fun getPhotos()
                }
                ```
                
                @GET
                
                GET 요청임을 알리는 주석
                
        2. 객체 선언
            
            만든 `MarsApiService` 인터페이스의 객체를 선언한다.
            
            ```kotlin
            object MarsApi {
                val retrofitService : MarsApiService by lazy {
                   retrofit.create(MarsApiService::class.java) }
            }
            ```
            
            lazy : 지연 인스턴스화
            
            실제로 객체가 필요할 때까지는 불필요한 계산이 실행되거나 다른 컴퓨팅 리소스가 사용되지 않도록 하기 위해 객체 생성을 의도적으로 지연하는 것
            
        3. getMarsPhotos() 구현
            1. `MarsApiService` 인터페이스 속 메서드를 suspend로 변경
            2. `overview/OverviewViewModel` 의 getMarsPhotos()에 `viewModelScope.launch`를 사용하여 코루틴을 실행
                
                ```kotlin
                viewModelScope.launch {
                    }
                ```
                
                viewModelScope
                
                - 앱의 각 ViewModel을 대상으로 정의되는 코루틴
                - ViewModel이 삭제되면 자동으로 취소됨
                - 코루틴은 ViewModel이 활성 상태인 경우에만 실행
            3. viewModelScope 내부에서 MarsApi의 싱글톤 객체→ retrofitService 인터페이스→ getPhotos() 메서드 호출하여 응답 저장
            
- 인터넷 권한 및 예외 처리 추가하기
    - 인터넷 권한 추가
        
        `AndroidManifest` 파일에 `<uses-permission>` 태그를 포함하여 앱에 필요한 권한을 선언
        
        ```kotlin
        <uses-permission android:name="android.permission.INTERNET" />
        ```
        
    - 예외 처리
        - 발생할 수 있는 에러들
            - API에 사용된 URL 또는 URI가 잘못됨
            - 서버를 사용할 수 없어 앱을 서버에 연결할 수 없음
            - 네트워크 지연 문제
            - 기기의 인터넷 연결이 불안정하거나 기기가 인터넷에 연결되지 않음
        - try-catch 문으로 해결하기
- Moshi를 사용하여 JSON 응답 파싱하기
    - JSON 응답의 구조
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/68fdfa54410ee03e.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-getting-data-internet/img/68fdfa54410ee03e.png?hl=ko)
        
    - Moshi 라이브러리
        
        JSON 문자열을 Kotlin 객체로 변환하는 Android JSON 파서
        
        [GitHub - square/moshi: A modern JSON library for Kotlin and Java.](https://github.com/square/moshi)
        
        - 사용하기
            1. `build.gradle (Module: app)` 에 코드 추가
                
                ```kotlin
                //추가
                implementation 'com.squareup.moshi:moshi-kotlin:1.9.3'
                
                //스칼라 변환을 모시로 바꾸기
                implementation 'com.squareup.retrofit2:converter-moshi:2.9.0'
                ```
                
            2. JSON 데이터를 파싱하여 Kotlin객체로 만들기
                
                → 데이터 클래스 필요
                
                ⇒ `MarsPhoto` 데이터 클래스 생성
                
                ```kotlin
                data class MarsPhoto(
                   val id: String,
                	@Json(name = "img_src") val imgSrcUrl: String
                )
                ```
                
                @Json 주석
                
                데이터 클래스에서 JSON 응답의 키 이름과 다른 변수 이름을 사용할 때 사용
                
            3. `MarsApiService` 및 `OverviewViewModel` 업데이트
                1. `MarsApiService` 에 moshi 객체 만들기
                    
                    ```kotlin
                    private val moshi = Moshi.Builder()
                       .add(KotlinJsonAdapterFactory())
                       .build()
                    ```
                    
                    KotlinJsonAdapterFactory
                    
                    Moshi의 주석이 Kotlin과 원활하게 작동하게 하기
                    
                2. retrofit 객체 선언의 Retrofit 빌더에서 MoshiConverterFactory 사용으로 바꾸기

---

### 인터넷에서 이미지 로드 및 표시

- 인터넷 이미지 표시하기
    - 웹 URL에서 이미지 표시
        1. 이미지 다운
        2. 내부적으로 저장
        3. 압축 형식에서 Android가 사용할 수 있는 이미지로 디코딩
        4. 이미지는 메모리 내 캐시/ 저장소 기반 캐시, 둘 다 혹은 둘 중 하나에 캐시해야함
    - coil 라이브러리
        
        이미지를 다운로드,버퍼링, 디코딩, 캐시
        
        [사용 순서]
        
        1. `build.gradle (Module: app)` 에 코드 추가
            
            ```kotlin
            // Coil
                implementation "io.coil-kt:coil:1.1.1"
            ```
            
        2. `build.gradle (Project: MarsPhotos)` 에 코드 추가
            
            ```kotlin
            repositories {
               ...
               mavenCentral()
            }
            ```
            
        3. ViewModel 업데이트 하기
            
            속성 추가
            
            ```kotlin
            private val _photos = MutableLiveData<MarsPhoto>()
            val photos: LiveData<MarsPhoto> = _photos
            ```
            
            try문 수정
            
            ```kotlin
            _photos.value = MarsApi.retrofitService.getPhotos()[0]
            _status.value = "   First Mars image URL : ${_photos.value!!.imgSrcUrl}"
            ```
            
        4. 결합 어댑터 만들기 및 Coil 사용하기
            
            결합 어댑터
            
            ```kotlin
            @BindingAdapter("imageUrl")
            fun bindImage(imgView: ImageView, imgUrl: String?) {
                imgUrl?.let {
                    // Load the image in the background using Coil.
                    }
                }
            }
            ```
            
            - @BindingAdapter : 속성 이름을 매개변수로 사용
            - bindImage 메서드 :
                
                1번째: 타겟 뷰의 유형
                
                2번째 : 속성에 설정되는 값
                
            
            에러가 남
            
            프레임워크에서 제공되지 않는 맞춤 속성이기 때문에 setImageUrl(String)을 자동으로 찾지 못함
            
            ```
            <ImageView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:imageUrl="@{product.imageUrl}"/>
            ```
            
            - `BindingAdapter.kt` 파일 생성
                
                ```kotlin
                package com.example.android.marsphotos.overview
                
                import android.widget.ImageView
                import androidx.databinding.BindingAdapter
                
                @BindingAdapter("imageUrl")
                fun bindImage(imgView: ImageView, imgUrl: String?) {
                
                }
                ```
                
            - bindImage() 함수 내부에 ImageURL 인수 추가
                
                ```kotlin
                imgUrl?.let {
                        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                        imgView.load(imgUri)
                    }
                ```
                
                toUri() : URL 문자열을 Uri 객체로 변환
                
                buildUpon.scheme("https") : HTTPS 스키마 사용
                
                imgView.load(imgUri) : imgView로 imgUri 객체에서 이미지 로드
                
        5. 레이아웃 및 프래그먼트 업데이트
            - 레이아웃 업데이트
                1. 데이터 결합 : `res` / `layout` / `grid_view_item.xml` 에서 `OverviewViewModel` 결합
                    
                    ```kotlin
                    <data>
                       <variable
                           name="viewModel"
                           type="com.example.android.marsphotos.overview.OverviewViewModel" />
                    </data>
                    
                    // ImageView에 결합
                    ... 
                    app:imageUrl="@{viewModel.photos.imgSrcUrl}"
                    ```
                    
                2. 로딩 이미지와 에러 이미지 표시하기
                    
                    `BindingAdapter.kt` 에서 imgView에 로드하는 이미지에 추가하기
                    
                    ```kotlin
                    imgView.load(imgUri) {
                       placeholder(R.drawable.loading_animation)
                       error(R.drawable.ic_broken_image)
                    }
                    ```
                    
                    placeholder() : 로드하는 동안 사용할 자리 표시자
                    
                    error() : 이미지를 로드하지 못한 경우 사용할 이미지
                    
- RecyclerView를 사용하여 이미지 그리드 표시하기
    - 뷰 모델 업데이트
        
        속성 업데이트
        
        ```kotlin
        private val _photos = MutableLiveData<List<MarsPhoto>>()
        val photos: LiveData<List<MarsPhoto>> = _photos
        ```
        
    
    - 그리드 레이아웃
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-internet-images/img/b5744904f1a1d3fb.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-internet-images/img/b5744904f1a1d3fb.png?hl=ko)
        
    
    - Recyclerview 추가하기
        1. `layout/gridview_item.xml` 에서 데이터 결합 바꾸기
            
            ```kotlin
            <data>
               <variable
                   name="photo"
                   type="com.example.android.marsphotos.network.MarsPhoto" />
            </data>
            
            ...
            app:imageUrl="@{photo.imgSrcUrl}"
            ```
            
        
         2. `layout/fragment_overview.xml`  수정하기
        
        TextView 삭제 후 RecyclerVeiw 추가
        
        ```kotlin
        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/photos_grid"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:padding="6dp"
            app:layoutManager=
                "androidx.recyclerview.widget.GridLayoutManager"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:spanCount="2"
            tools:itemCount="16"
            tools:listitem="@layout/grid_view_item"  />
        ```
        
        1. 앱에 ListAdapter 추가하기
            
            ```kotlin
            // RecyclerView에서 사용할 어댑터
            class PhotoGridAdapter : ListAdapter<MarsPhoto,
                    PhotoGridAdapter.MarsPhotoViewHolder>(DiffCallback) {
                
            		companion object DiffCallback : DiffUtil.ItemCallback<MarsPhoto>() {
                    override fun areItemsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
                        return oldItem.id == newItem.id
                    }
            
                    override fun areContentsTheSame(oldItem: MarsPhoto, newItem: MarsPhoto): Boolean {
                        return oldItem.imgSrcUrl == newItem.imgSrcUrl
                    }
                }
                
            		// viewHolder 객체 생성
            		// viewHolder 클래스는 하단에 이너클래스로 삽입
            		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridAdapter.MarsPhotoViewHolder {
                    return MarsPhotoViewHolder(GridViewItemBinding.inflate(
                        LayoutInflater.from(parent.context)))
                }
            
            		// viewHolder에 데이터 바인딩
                override fun onBindViewHolder(holder: PhotoGridAdapter.MarsPhotoViewHolder, position: Int) {
                    val marsPhoto = getItem(position)
                    holder.bind(marsPhoto)
                }
            	
            		// viewHolder 클래스
                class MarsPhotoViewHolder(private var binding: GridViewItemBinding):
                    RecyclerView.ViewHolder(binding.root) {
                    fun bind(MarsPhoto: MarsPhoto) {
                        binding.photo = MarsPhoto
                        binding.executePendingBindings()
                    }
                }
            }
            ```
            
        2. 결합 어댑터 추가하고 연결하기
            
            BindingAdapter를 사용 → RecyclerView 데이터를 설정
            
            ⇒ 데이터 결합이 자동으로 MarsPhoto 객체 목록의 LiveData를 관찰
            
            ```kotlin
            @BindingAdapter("listData")
            fun bindRecyclerView(recyclerView: RecyclerView,
                                 data: List<MarsPhoto>?) {
                // 어댑터 객체 생성
                val adapter = recyclerView.adapter as PhotoGridAdapter
                adapter.submitList(data)
            }
            ```
            
- RecyclerView에 오류 처리 추가하기
    - enum
        
        상수 집합을 보유할 수 있는 데이터 유형
        
        ```kotlin
        enum class Direction {
            NORTH, SOUTH, WEST, EAST
        }
        
        var direction = Direction.NORTH;
        ```