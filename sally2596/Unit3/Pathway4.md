# Pathway4

- 앱의 구조
    - 레이아웃
        - `fragment_start.xml`
            - 앱에 표시되는 첫 번째 화면
            - 컵케이크 이미지와 주문할 컵케이크 수를 선택할 수 있는 버튼 3개(1개, 6개, 12개)으로 구성
        - `fragment_flavor.xml`
            - 컵케이크 맛 목록(라디오 버튼 옵션)
            - **Next** 버튼
        - `fragment_pickup.xml`
            - 수령일을 선택하는 옵션
            - **Next** 버튼(요약 화면으로 이동)
        - `fragment_summary.xml`
            - 주문 세부정보의 요약 표시(수량, 맛..)
            - 버튼(주문을 다른 앱으로 전송)
    - 프래그먼트
        - `StartFragment.kt`
            - 앱에 표시되는 첫 번째 화면
            - 3개의 버튼을 위한 클릭 핸들러 & 뷰 결합 코드
        - `FlavorFragment.kt`, `PickupFragment.kt`, `SummaryFragment.kt`
            - 상용구 코드
            - 토스트 메시지를 표시하는 **Next**
            - **Send Order to Another App** 버튼의 클릭 핸들러
    - 리소스
        - `drawable`
            - 첫 번째 화면의 컵케이크 애셋
            - 런처 아이콘 파일
        - `navigation/nav_graph.xml`
            - *작업*이 없는 4개의 프래그먼트 대상(`startFragment`, `flavorFragment`, `pickupFragment`, `summaryFragment`)
        - `values`
            - 앱 테마 → 사용되는 색상, 크기, 문자열, 스타일, 테마
- 탐색 그래프 완성하기 (UI 선으로 연결하는거)
    1. `navigation` > `nav_graph.xml` 에서 연결
    2. 각 Fragment 파일의 Toast메세지를 연결선(각 화면과 화면 사이 연결선)으로 바꾸기
        
        ```kotlin
        findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
        
        findNavController().navigate(R.id.action_flavorFragment_to_pickupFragment)
        
        findNavController().navigate(R.id.action_pickupFragment_to_summaryFragment)
        ```
        
    3. 각 Fragment 화면에서 라벨로 제목 붙이기
        
        ```kotlin
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
        
                val navHostFragment = supportFragmentManager
                        .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
        
                setupActionBarWithNavController(navController)
            }
        ```
        
        `setupActionBarWithNavController(navController)` 
        
        - 대상의 라벨을 기반으로 앱 바에 제목이 표시됨
        - 최상위 대상에 있지 않을 경우 뒤로 버튼 표시
- Fragment간 공유 가능한 ViewModel 만들기
    - 속성
        - 주문 수량(`Integer`)
        - 컵케이크 맛(`String`)
        - 수령 날짜(`String`)
        - 가격(`Double`)
    - 메서드
        - `setQuantity(numberCupcakes: Int)`
        - `setFlavor(desiredFlavor: String)`
        - `setDate(pickupDate: String)`
    
- 각 Fragment 파일에서 해당 ViewModel 사용하기
    
    ```kotlin
    private val sharedViewModel: OrderViewModel by activityViewModels()
    ```
    
    **ViewModel 초기화 방법**
    
    1. `viewModels()`를 이용한 초기화
        
        해당 viewModel이 초기화되는 Activity 혹은 Fragment의 Lifecycle에 종속
        
        ViewModels()를 사용하기 위해서 build.gradle에 아래 코드 삽입
        
        ```kotlin
        dependencies { 
        		implementation 'androidx.activity:activity-ktx:1.2.2' 
        		implementation 'androidx.fragment:fragment-ktx:1.3.3' 
        		.. 
        }
        ```
        
    2. `activityViewModels()`를 이용한 초기화
        
        Fragment는 Activity에 종속되어 있기 때문에 Fragment가 생성된 Activity의 Lifecycle에 ViewModel을 종속
        
    
    [[ViewModel] 4. ViewModel을 이용한 Fragment간 데이터 공유](https://kotlinworld.com/88?category=918952)
    
- 데이터 결합 사용하기
    - 각 Fragment에 viewModel 설정
        
        이때 startFragment는 가져올 데이터가 없으므로 설정하지 않아도 됨
        
        ```kotlin
        binding?.apply {
            viewModel = sharedViewModel
            ...
        }
        ```
        
        apply()
        
        ```kotlin
        clark.apply {
            firstName = "Clark"
            lastName = "James"
            age = 18
        }
        
        // 위 아래 같은 내용임
        
        clark.firstName = "Clark"
        clark.lastName = "James"
        clark.age = 18
        ```
        
    - 뷰 결합 설정 (xml 파일 수정)
        
        ```kotlin
        android:checked="@{viewModel.flavor.equals(@string/coffee)}"
        ```
        
    - 리스너 결합
        
        ```kotlin
        android:onClick="@{() -> viewModel.setFlavor(@string/vanilla)}"
        ```
        
- pickup 날짜 설정하기
    - SimpleDateFormat
        
        ```kotlin
        SimpleDateFormat("E MMM d", Locale.getDefault())
        ```
        
        Locale
        
         특정한 지리적, 정치적 또는 문화적 지역을 나타냄
        
        날짜 형식
        
        [](https://developer.android.com/reference/java/text/SimpleDateFormat?hl=ko#date-and-time-patterns)
        
    - Calender
        
        ```kotlin
        val calendar = Calendar.getInstance()
        
        // 날짜 계산
        calendar.add(Calendar.DATE, 1)
        calendar.add(Calendar.MONTH, 2)
        calendar.add(Calendar.DATE, -3)
        ```
        
    
- 가격 계산 설정
    - 엘비스 연산자
        
        왼쪽값이 null이면 ?: 의 오른쪽 값으로 대체
        
        ```kotlin
        _price.value = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        ```
        
    - Transformations.map()
        
        LiveData 및 함수를 매개변수로 사용
        
        LiveData 변환을 사용할 수 있는 예시
        
        - 표시할 날짜 및 시간 문자열 형식 지정
        - 항목 목록 정렬
        - 항목 필터링 또는 그룹화
        - 모든 항목 합계, 항목 수, 마지막 항목 반환 등과 같이 목록의 결과 계산
        

---

- up 버튼 (상단바의 뒤로가기 버튼)
    
    ```kotlin
    override fun onSupportNavigateUp(): Boolean {
       return navController.navigateUp() || super.onSupportNavigateUp()
    }
    ```
    
- 주문 취소하기
    - 백 스택
        
        스택 맨 위에 있는 현재 활동 삭제 & 폐기 → 아래에 있는 활동으로 다시 시작
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/517054e483795b46.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/517054e483795b46.png?hl=ko)
        
    - cupcake 앱 동작 순서
        1. 앱을 처음 열면 StartFragment 대상이 표시, 스택 상단으로 푸시
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/cf0e80b4907d80dd.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/cf0e80b4907d80dd.png?hl=ko)
            
        2. 컵케이크 수량을 선택하면 FlavorFragment로 이동, 백 스택으로 푸시
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/39081dcc3e537e1e.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/39081dcc3e537e1e.png?hl=ko)
            
        3. 맛을 선택하고 Next를 탭하면 PickupFragment로 이동, 백 스택으로 푸시
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/37dca487200f8f73.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/37dca487200f8f73.png?hl=ko)
            
        4. 수령 날짜를 선택하고 Next를 탭하면 SummaryFragment로 이동, 백 스택의 맨 위에 추가
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/d67689affdfae0dd.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/d67689affdfae0dd.png?hl=ko)
            
        5. 이전 단계로 이동, 스택의 맨 위 Fragment 폐기
            
            ![Untitled](Pathway4%2084b4e829d00c4a91809f928dc6dde161/Untitled.png)
            
        6. 마지막 단계에서 주문 취소의 경우 → start를 제외한 모든 Fragment 폐기
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/e3dae0f492450207.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/e3dae0f492450207.png?hl=ko)
            
        
        ```kotlin
        app:popUpTo="@id/startFragment"
        ```
        
        스택에서  해당 Fragment까지 pop함
        
        ```kotlin
        app:popUpToInclusive="true"
        ```
        
        백 스택에 첫번째 Fragment 인스턴스가 하나만 생성
        
- 주문 전송하기
    - 암시적 인텐트 생성
        
        ```kotlin
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)
        ```
        
        → 사용할 앱이 있을 때 실행하기
        
        ```kotlin
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        }
        ```