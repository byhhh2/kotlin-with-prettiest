# Pathway1

### Kotlin의 컬렉션

- 컬렉션 종류
    - 목록
        
        list들
        
    - Set (집합)
        
        순서가 중요하지 않음.
        
        동일한 요소는 한개만 취급함
        
        - 변경 가능한 집합 : mutableSet
        - 변경 불가능한 집합 : set
    - 맵 (딕셔너리)
        
        키 - 쌍 집합
        
        - 변경 가능한 맵 : mutableMap
            
            mutableMapOf<(키) , (값)>()
            
            ```kotlin
            val peopleAges = mutableMapOf<String, Int>(
                    "Fred" to 30,
                    "Ann" to 23
                )
            ```
            
            - 항목 추가 : put()
                
                peopleAges.put("Barbara", 42)
                
        - 변경 불가능한 맵 : Map
        
- 컬렉션 사용하기
    - forEach()문
        
        현재 항목의 변수 : it (고정값으로 사용, 사용자가 지정하는 변수가 아님)
        
        ```kotlin
        peopleAges.forEach { print("${it.key} is ${it.value}, ") }
        ```
        
    - map
        
        ```kotlin
        peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ")
        ```
        
        `.joinToString(", ")` : 변환된 컬렉션의 각 항목을 문자열에 추가하고 `,` 로 구분함, 마지막에는 기호를 추가하지 않음 
        
    - filter
        
        반환값 : LinkedHashMap
        
        ```kotlin
        peopleAges.filter { it.key.length < 4 }
        ```
        
- 람다 함수 및 고차 함수
    - 람다 함수
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/4d3f2be4f253af50.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/4d3f2be4f253af50.png?hl=ko)
        
        Fuction Type : (입력 타입) → 반환 타입
        
        Lambda : (값) : (값의 유형) → (값을 계산하는 식)
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/252712172e539fe2.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/252712172e539fe2.png?hl=ko)
        
        단일 매개변수의 경우 특수 식별자 **it**를 사용
        ex ) val triple: (Int) -> Int = { it * 3 }
        
    - 고차 함수
        - sortedWith
            
            sortedWith{ (매개변수) → (매개변수로 하려는 식) }
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/7005f5b6bc466894.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/7005f5b6bc466894.png?hl=ko)
            
            ⇒ 람다의 반환값 : 문자열의 길이 차이를 반환 
            
            ⇒ 문자열의 길이 차이로 sort를 진행함
            
        - OnClickListener
            
            단일 추상 메서드 변환으로 축약 버전이 가능하다
            
            **SAM (Single Abstract Method : 단일 추상 메서드)**
            
            - SAM 변환
                
                자바로 작성한 Functional Interface의 경우 동작함
                
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/29760e0a3cac26a2.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/29760e0a3cac26a2.png?hl=ko)
            
        
- 단어 목록 만들기
    - startsWith()
        
        ```kotlin
        // ignoreCase : 대소문자 구분 여부 -> 안함
        startsWith("b", ignoreCase = true)
        ```
        
    - shuffled() : 무작위로 섞기
    - take(): ()안의 숫자 개수 만큼 컬렉션의 앞에서 가져오기

---

### 활동 및 인텐트

- Words 앱 개요
    - `LetterAdapter`
        
        `MainActivity` 의 `RecyclerView` 에서 사용
        
        각 문자는 `onClickListener`가 포함된 버튼
        
        버튼 누름을 처리해서 `DetailActivity`로 이동
        
    - `WordAdapter`
        
        `RecyclerView` 의 `DetailActivity`에서 단어 목록을 표시하는데 사용
        
        단어의 정의를 표시하기 위해 브라우저로 이동하는 코드 추가 
        
- 인텐트
    
    실행할 작업을 나타내는 객체
    
    - 암시적 인텐트
        
        추상적 → 해야할 활동을 잘 모름
        
        작업 유형을 알려주고 시스템은 요청 처리 방법을 파악(시스템에 링크 열기나 이메일 작성, 전화 걸기와 같은 작업)
        
        → 해야할 활동을 모르기 때문에 작업 유형을 알려주면 시스템이 알아서 파악함
        
    - 명시적 인텐트
        
        매우 구체적
        
        실행할 활동을 정확하게 알 수 있고 자체 앱 화면인 경우가 많음
        
    
- 명시적 인텐트 설정
    
    → 문자를 탭하면 단어 목록이 있는 두번째 화면으로 이동하기
    
    **Intent(Context, Class)** 
    
    ```kotlin
    val intent = Intent(context, DetailActivity::class.java)
    ```
    
    Context : A Context of the application package implementing this class. (아마 현재 클래스의 context를 말하는 듯)
    
    Class : 인텐트의 대상이 되는 클래스
    
    **putExtra**
    
    ```kotlin
    intent.putExtra("letter", holder.button.text.toString())
    ```
    
    매개변수 전달과 비슷한 개념
    
    **startActivity(intent)**
    
    컨텍스트 객체에서 intent 전달
    
    🧐 현재 앱의 정보를 가진 context보고 "startActivity를 해라" 라고 말하면서 intent를 전달해줌 → 인텐트 정보를 보고 대상 클래스로 작업 요청을 함
    
    ```kotlin
    context.startActivity(intent)
    ```
    
- DetailActivity 설정
    
    ```kotlin
    val letterId = intent?.extras?.getString("letter").toString()
    ```
    
    intent가 있다면 > extra가 있다면? > getString 진행
    
    만약 'letter' 말고도 extras가 많아진다면?
    
    → 컴패니언 객체 활용!
    
    **컴패니언 객체**
    
    - 객체에 이름을 지을 수 있다.
        
        ```kotlin
        companion object MyCompanion{
                val prop = "나는 Companion object의 속성이다."
                fun method() = "나는 Companion object의 메소드다."
         }
        ```
        
    - 클래스의 특정 인스턴스 없이 상수를 구분하여 사용
    - 클래스 내에서 컴패니언 객체는 하나만 쓸 수 있다.
    - static과 비슷하게 사용된다
        
        → (class이름). (컴패니언 객체 내의 변수) 바로 사용이 가능 
        
        → (컴패니언 객체 이름).(컴패니언 객체 내의 변수) 
        
        위의 2가지 방법으로 모두 사용 가능
        
    
    1. `DetailActivity` 클래스에 `companion` 객체 선언
        
        ```kotlin
        companion object {
                const val LETTER = "letter"
            }
        ```
        
    2. `DetailActivity` 의 letterId 수정
        
        ```kotlin
        val letterId = intent?.extras?.getString(LETTER).toString()
        ```
        
    3. `LetterAdapter` 의 putExtra() 수정
        
        → 컴패니언 오브젝트를 전달해줌
        
        ```kotlin
        intent.putExtra(DetailActivity.LETTER, holder.button.text.toString())
        ```
        
- 암시적 인텐트 설정
    
    실행하려는 활동이나 앱을 알 수 없는 경우 암시적 인텐트 사용
    
    → 새 활동을 앱에 추가하는 대신 기기의 브라우저를 실행하여 검색 페이지 표시
    
    ⇒ 브라우저가 많기 때문에 어떤 브라우저를 써야 하는지 모름
    
    ⇒ 따라서 암시적 인텐트를 사용
    
    1. `DetailActivity` 의 컴패니언 객체 수정
        
        `SEARCH_PREFIX` : 검색 URL
        
        ```kotlin
        companion object {
           const val LETTER = "letter"
           const val SEARCH_PREFIX = "https://www.google.com/search?q="
        }
        ```
        
    2. `WordAdapter` 에서 버튼에 `setOnClickListener()` 추가
        1. 검색의 URI를 만들어서 변수에 할당
            
            (URI의 형식을 사용 : Uri.parse())
            
            ```kotlin
            holder.button.setOnClickListener {
                val queryUrl: Uri = Uri.parse("${DetailActivity.SEARCH_PREFIX}${item}")
            }
            ```
            
            **URI (Uniform Resource Identifier)**
            
            형식에 관한 더 일반적인 용어
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-activities-intents/img/828cef3fdcfdaed.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-activities-intents/img/828cef3fdcfdaed.png?hl=ko)
            
        2. 암시적 인텐트 생성
            
            ```kotlin
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            ```
            
            매개변수 
            
            - `URI`와 `Intent.ACTION_VIEW` 전달
                
                명시적 인텐트와 다르게 `context`와 `활동(대상 클래스나 다른 어떤 대상)`이 아님
                
            - `Intent.ACTION_VIEW` : URI를 사용하는 일반적인 인텐트
                
                다른 유형은 아래에서 참고 가능
                
                [](https://developer.android.com/guide/components/intents-common?hl=ko)
                
        3. 인텐트 전달
            
            ```kotlin
            context.startActivity(intent)
            ```
            
- 메뉴 및 아이콘 설정
    1. 아이콘 이미지 추가하기
    2. 앱 바에 표시되는 옵션과 사용할 아이콘 전용 리소스 파일 생성
        - 리소스 타입 :Menu
        - 속성
            - `id`:  코드에서 참조할 수 있는 ID
            - `title`: 실제로 표시되지 않는 텍스트, 스크린 리더에서 메뉴를 식별하는 데 유용
            - `icon`: `ic_linear_layout`, 버튼이 선택될 때 그리드 아이콘을 표시하기 위해 사용 설정되거나 사용 중지됨
            - `showAsAction`: 시스템의 버튼 표시 방법, always : 앱 바에 항상 표시 & 더보기 메뉴에 속하지 않음
    3. `MainActivity.kt` 수정하기
        1. 앱의 레이아웃 추적용 변수 생성
            
            ```kotlin
            private var isLinearLayoutManager = true
            ```
            
        2. 레이아웃 변경 메서드 작성 (앱 레이아웃 추적용 변수 값에 따라 달라짐)
            
            ```kotlin
            private fun chooseLayout() {
                if (isLinearLayoutManager) {
                    recyclerView.layoutManager = LinearLayoutManager(this)
                } else {
                    recyclerView.layoutManager = GridLayoutManager(this, 4)
                }
                recyclerView.adapter = LetterAdapter()
            }
            ```
            
        3. 아이콘 변경 메서드 작성 (앱 레이아웃 추적용 변수 값에 따라 달라짐)
            
            ```kotlin
            private fun setIcon(menuItem: MenuItem?) {
                    if (menuItem == null)
                        return
            
                    menuItem.icon =
                        if (isLinearLayoutManager)
                            ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
                        else ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
                }
            ```
            
        4. `onCreateOptionsMenu` 메서드 작성
            
            옵션 메뉴를 확장하여 추가 설정을 실행
            
        5. `onOptionsItemSelected` 메서드 작성
            
            버튼이 선택될 때 실제로 chooseLayout()을 호출
            
        
    
    ---
    
    ### 활동 수명 주기 단계
    
    - 수명 주기 메서드
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-activity-lifecycle/img/f6b25a71cec4e401.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-activity-lifecycle/img/f6b25a71cec4e401.png?hl=ko)
        
        수명 주기 메서드를 재정의할 때 슈퍼클래스 구현을 호출하여 활동 생성을 완료해야 하므로 super.(메서드 명)() 을 즉시 호출 해야함
        
        🧐 상속받았으니까 슈퍼클래스꺼 메서드 실행하라는 뜻이지 않을까?
        
        화면이 회전될 경우 : 활동이 처음부터 다시 실행 (create부터)
        
        - `onCreate()` 메서드
            - 활동이 초기화된 직후에 **한 번** 호출 (새 Activity 객체가 메모리에 만들어질 때)
            - 메서드가 실행되면 `활동이 생성됨` 으로 간주
            - 레이아웃을 확장 / 클릭 리스너를 정의 / 뷰 결합 설정
            - `onSaveInstanceState()` 로 데이터 복원하기
                
                getInt 로 데이터 가져오기
                
                ```kotlin
                override fun onCreate(savedInstanceState: Bundle) {
                			if (savedInstanceState != null) {
                				   revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
                				   dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
                			}
                ```
                
        - `onStart()` 메서드
            - `onCreate()` 직후에 호출
            - 활동이 화면에 표시
            - 활동의 수명 주기에서 여러 번 호출될 수 있음
            - `onStop()` 과 페어링
        - `onResume()` 메서드
            - 앱에 포커스 제공
            - 사용자가 상호작용할 수 있도록 활동 준비
            - 다시 시작할 대상이 없어도 시작 시 호출됨
        - `onPause()` 메서드
            - 앱에 포커스가 없는 상태
            - 일시정지 모드
        - `onStop()` 메서드
            - 앱이 더 이상 화면에 표시되지 않는 상태
            - 활동은 중지되었으나 Activity 객체는 백그라운드에서 메모리에 있음
        - `onDestroy()` 메서드
            - 앱에서 사용하는 리소스를 정리할 때  **한 번** 호출
            - 활동이 완전히 종료되었음 → 가비지 컬렉션될 수 있다.
            - 리소스가 삭제될 수 있음을 인식하고 메모리 정리를 시작함
        - `onRestart()` 메서드
            - 활동이 처음으로 시작되지 않은 경우에만 호출
        - `onSaveInstanceState()` 메서드
            - Activity가 소멸되면 필요할 수 있는 데이터 저장용 콜백
            - 활동이 중지된 후, 앱이 백그라운드로 전환될 때 호출됨
            
            ```kotlin
            override fun onSaveInstanceState(outState: Bundle) {
               super.onSaveInstanceState(outState)
            
               Log.d(TAG, "onSaveInstanceState Called")
            }
            ```
            
            Bundle 
            
            - 키-값 쌍 모음, 키: 문자열 , 값 : int/boolean과 같은 간단한 데이터
            - 너무 많은 데이터 저장 시 `TransactionTooLargeException` 오류로 앱이 비정상 종료됨
            - 번들에 데이터 추가 : putInt() / putFloat() / putString()
                
                매개변수 : ( 키값(string) , int/float/string ) 
                
            
            ```kotlin
            const val KEY_REVENUE = "revenue_key"
            const val KEY_DESSERT_SOLD = "dessert_sold_key"
            
            outState.putInt(KEY_REVENUE, revenue)
            outState.putInt(KEY_DESSERT_SOLD, dessertsSold)
            ```
            
        - 로깅
            
            시스템의 작동 정보인 로그(log)를 기록하는 행위
            
            Log 클래스는 Logcat에 메시지를 쓴다.
            
            - `Log.d()` : 디버그 메세지 작성, `D/` 로 필터링
            - `Log.i()` : 정보 메세지
            - `Log.e()` : 오류 메세지
            - `Log.w()` : 경고 메세지
            - `Log.v()` : 자세한 메세지
            
            ```kotlin
            const val TAG = "MainActivity"
            Log.d(TAG, "onCreate Called")
            ```