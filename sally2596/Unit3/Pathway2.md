# Pathway2

- 프래그먼트
    - 사용자 인터페이스에서 재사용 가능한 부분
    - 수명 주기가 있음
    - 사용자 입력에 응답할 수 있음
    - Activity의 뷰 계층 구조 내에 포함
    - 단일 Activity에서 여러 프래그먼트를 동시에 호스팅 할 수 있음
    - 구성 - 레이아웃용 xml & 데이터 표시 및 상호작용 처리용 kt파일
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/74470aacefa170bd.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/74470aacefa170bd.png?hl=ko)
    
    - Lifecycle State
        - `INITIALIZED`
            - 프래그먼트의 새 인스턴스가 인스턴스화 됨
        - `CREATED`
            - 첫 번째 프래그먼트 수명 주기 메서드 호출
            - 프래그먼트와 연결된 뷰 생성
        - `STARTED`
            - 프래그먼트가 화면에 표시됨
            - 프래그먼트에 '포커스'가 없음
            - 사용자 입력에 응답할 수 없음
        - `RESUMED`
            - 프래그먼트가 표시됨
            - 프래그먼트에 포커스가 있음
        - `DESTROYED`
            - 프래그먼트 객체의 인스턴스화가 취소됨
    - Callback (수명 주기 메서드)
        - `onCreate()`
            - 프래그먼트가 인스턴스화됨→ `CREATED` 상태
            - 상응하는 뷰가 아직 만들어지지 않음
        - `onCreateView()`
            - 레이아웃을 확장
            - 프래그먼트가 `CREATED` 상태로 전환됨
        - `onViewCreated()`
            - 뷰가 만들어진 후 호출
            - 일반적으로 `findViewById()`를 호출하여 특정 뷰를 속성에 바인딩 함
        - `onStart()`
            - 프래그먼트가 `STARTED` 상태로 전환됨
        - `onResume()`
            - 프래그먼트가 `RESUMED` 상태로 전환됨
            - 포커스를 보유
            - 사용자 입력에 응답할 수 있음
        - `onPause()`
            - 프래그먼트가 `STARTED` 상태로 전환됨
            - UI가 사용자에게 표시
        - `onStop()`
            - 프래그먼트가 `CREATED` 상태로 전환됨
            - 객체가 인스턴스화되었지만 더 이상 화면에 표시되지 않음
        - `onDestroyView()`
            - 프래그먼트가 `DESTROYED` 상태로 전환되기 직전에 호출됨
            - 뷰는 메모리에서 삭제됨
            - 프래그먼트 객체는 여전히 존재
        - `onDestroy()`
            - 프래그먼트가 `DESTROYED` 상태로 전환
- LetterListFragment 구현
    1. 뷰 바인딩 구현
        
        `FragmentLetterListBinding` : null을 허용하는 바인딩 클래스 
        
        null을 허용하는 이유 
        
        - onCreateView()가 호출될 때 까지 레이아웃을 확장할 수 없음
        - `FragmentLetterListBinding` 의 인스턴스가 만들어지는 시점과 속성을 실제로 사용할 수 있는 시점 사이에 시간이 있음
        
        ```kotlin
        import com.example.wordsapp.databinding.FragmentLetterListBinding
        private var _binding: FragmentLetterListBinding? = null
        
        // 실제로 사용할 떄는 _binding이 null이 아님을 확신하므로 !! 붙임
        private val binding get() = _binding!!
        ```
        
    2. recyclerView 만들기
        
        속성 선언
        
        ```kotlin
        private lateinit var recyclerView: RecyclerView
        ```
        
        `onViewCreated` 에서 속성값 설정
        
        ```kotlin
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           recyclerView = binding.recyclerView
           chooseLayout()
        }
        ```
        
    3. `onDestroyView()` 에서 뷰가 더이상 없으므로 _binding = null 해주기 
        
        ```kotlin
        override fun onDestroyView() {
           super.onDestroyView()
           _binding = null
        }
        ```
        
    4. `chooseLayout()`, `setIcon()`, `onOptionsItemSelected()` ,`onCreateOptionMenu()`메서드 MainActivity.kt에서 옮겨오기
        
        Activity에서는 this로 Context를 전달했으나 프래그먼트는 this로 전달 불가
        
        ⇒ 프래그먼트에서는 대신 사용할 수 있는 context(이대로 사용) 를 제공한다
        
    5. `onCreateOptionMenu()` 메서드 수정
        
        ```kotlin
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
           inflater.inflate(R.menu.layout_menu, menu)
        
           val layoutButton = menu.findItem(R.id.action_switch_layout)
           setIcon(layoutButton)
        }
        ```
        
        Activity 클래스 에서는 menuInflater 라는 전역 속성이 있음
        
        그러나 프래그먼트는 없음
        
        따라서 menuInflater를 메서드 **매개변수**로 전달 받음
        
    6. MainActivity에서 프래그먼트가 뷰에 표시되도록 레이아웃을 확장함
        
        
- DetailActivity 를 WordListFragment로 변환
    
    아래의 순서로 진행 (다시 해 볼 것)
    
    1. `DetailActivity`의 컴패니언 객체를 `WordListFragment`로 복사합니다. `WordAdapter`의 `SEARCH_PREFIX` 참조가 `WordListFragment`를 참조하도록 업데이트되었는지 확인합니다.
    2. `_binding` 변수를 추가합니다. 변수는 null을 허용해야 하고 초깃값으로 `null`을 보유해야 합니다.
    3. `_binding` 변수와 동일한 바인딩이라는 get-only 변수를 추가합니다.
    4. `onCreateView()`에서 레이아웃을 확장하여 `_binding` 값을 설정하고 루트 뷰를 반환합니다.
    5. `onViewCreated()`에서 나머지 설정을 실행합니다. recycler 뷰 참조를 가져와 레이아웃 관리자 및 어댑터를 설정하고 항목 장식을 추가합니다. 인텐트에서 문자를 가져와야 합니다. 프래그먼트는 `intent` 속성이 없으므로 일반적으로 상위 활동의 인텐트에 액세스하면 안 됩니다. 현재로서는 `DetailActivity`의 `intent`가 아닌 `activity.intent`를 참조하여 extras를 가져옵니다.
    6. `onDestroyView`에서 `_binding`을 null로 재설정합니다.
    7. `DetailActivity`에서 나머지 코드를 삭제하여 `onCreate()` 메서드만 남깁니다.
- Jetpack 탐색 구성요소
    
    탐색 구성요소 : 앱에서 간단하거나 복잡한 탐색 구현 처리
    
    - NavGraph : 탐색 그래프
        - 앱에서 탐색을 시각적으로 보여주는 XML
        - 개별 Activity와 fragment에 상응하는 대상과 한 대상에서 다른 대상으로 이동하려고 코드에서 사용할 수 있는 대상 사이의 작업으로 구성
        - 시각적 편집기 제공
    - NavHost
        - Activity 내에서 탐색 그래프의 대상을 표시하는 데 사용
        - `MainActivity`에서 `NavHostFragment`라는 기본 제공 구현을 사용
    - NavController
        - `NavController` 객체를 통해 `NavHost`에 표시되는 대상 간 탐색을 제어할 수 있음
        - 인텐트 → startActivity()로 화면 이동
            
            NavController → navigate()로 fragment 교체
            
    - 코드 추가하기
        - build.gradle (project)
            
            ```kotlin
            buildscript {
                ext {
                    ...
                    nav_version = "2.3.1"
                }
            		...
            		dependencies{
            				...
            				classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
            		}
            ```
            
        - build.gradle (app)
            
            ```kotlin
            plugins {
                ...
                id 'androidx.navigation.safeargs.kotlin'
            }
            ...
            dependencies{
            	...
            	implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
            	implementation "androidx.navigation:navigation-ui-ktx:$nav_version"
            }
            ```
            
- 탐색 그래프 사용
    - 탐색 그래프
        - 앱 탐색의 가상 매핑
        - 각 화면은 이동할 수 있는 대상이 됨
        - NavGraph의 대상은 FragmentContainerView 로 사용자에게 표시됨
    - MainActivity에서 FragmentContainerView 사용하기
        1. MainActivity 의 용도 = Fragment의 NavHost 역할을 할 FragmentContainerView를 포함
            
            `activity_main.xml` 수정하기 : 
            
            RecyclerView → FragmentCotainerView
            
            ```kotlin
            <?xml version="1.0" encoding="utf-8"?>
            <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                tools:context=".MainActivity">
            
                <androidx.fragment.app.FragmentContainerView
                    android:id="@+id/nav_host_fragment"
                    android:name="androidx.navigation.fragment.NavHostFragment"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    app:defaultNavHost="true"
                    app:navGraph="@navigation/nav_graph"
                    />
            </FrameLayout>
            ```
            
        2. Nav Graph 설정
            
            `res` 폴더에서 `nav_graph.xml` 생성
            
            아래와 같이 프래그먼트 대상 추가
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/307d036fce790feb.gif?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/307d036fce790feb.gif?hl=ko)
            
        3. 탐색 작업 만들기
            
            프래그먼트 하나를 클릭 & 드래그 하여 다른 프래그먼트에 끌기
            
        4. WordListFragment의 인수 지정
            
            인텐트의 extra 지정과 같은 느낌
            
            `WordListFragment` 의 Argument창에서 추가
            
        5. 시작 대상 설정
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/99bb085e39dd7b4a.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/99bb085e39dd7b4a.png?hl=ko)
            
        6. 탐색 작업 실행
            
            LetterAdapter.kt 수정
            
            - onClickListener()
                
                ```kotlin
                // 버튼의 setOnClickListener
                val action = LetterListFragmentDirections.actionLetterListFragmentToWordListFragment(letter = holder.button.text.toString())
                holder.view.findNavController().navigate(action)
                ```
                
        7. MainActivity 구성하기
            
            navController 속성 만들기
            
            ```kotlin
            private lateinit var navController: NavController
            ```
            
            onCreate() 에 아래 코드 추가하기
            
            ```kotlin
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            setupActionBarWithNavController(navController)
            ```
            
            - FragmentContainerView의 ID로 뷰 참조하여 navController 속성에 할당
            - setupActionBarWithNavController()를 호출하여 navController를 전달
            
            onSupportNavigateUp() 구현하기
            
            ```kotlin
            override fun onSupportNavigateUp(): Boolean {
               return navController.navigateUp() || super.onSupportNavigateUp()
            }
            ```
            
- WordListFragment에서 인수 가져오기
    
    인텐트 : activity?.intent를 참조하여 letter extra에 액세스
    
    nav_graph를 사용하여 탐색 실행
    
    1. WordListFragment 에서 letterId 속성 생성
        
        ```kotlin
        private lateinit var letterId: String
        ```
        
    2. onCreate()를 재정의
        
        ```kotlin
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        
            arguments?.let {
                letterId = it.getString(LETTER).toString()
            }
        }
        ```
        
        arguments는 선택사항일 수 있으므로 let()호출 & 람다 전달
        
    3. onViewCreated() 수정
        
        ```kotlin
        recyclerView.adapter = WordAdapter(letterId, requireContext())
        ```
        
- 동영상
    
    NavHostFragment 
    
    - 애플리케이션을 탐색할 때 표시하거나 숨기는 콘텐츠를  만들고 호스팅하는 역할
    - 목적지 탐색 시 여기에 목적지가 추가됨
    - fragment 하나가 삭제되고 다른 fragment가 추가됨
    
    NavController 
    
    - 탐색 요소의 구성요소
    - 실제로 탐색 작업을 함
    
    NavigationView
    
    - 이건 메뉴바 같은거