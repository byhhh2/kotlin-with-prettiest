
## Fragment 프래그먼트
* 재사용 가능한 UI의 부분. 하나 이상의 활동에 재사용하고 삽입 가능
* 단일 화면에 여러 개의 프래그먼트 표시 가능
* 레이아웃용 XML파일과 데이터 처리 및 사용자 상호작용을 처리하는 kotlin 클래스로 구성

<br><br>

### 프래그먼트 상태(Lifecycle.State)
  - INITIALIZED : 프래그먼트의 새 인스턴스가 인스턴스화됨.
  - CREATED : 첫 번째 프래그먼트 수명 주기 메서드가 호출됨. 이 상태에서 프래그먼트와 연결된 뷰가 만들어짐.
  - STARTED : 프래그먼트가 화면에 표시되지만 '포커스'가 없어 사용자 입력에 응답할 수 없음.
  - RESUMED: 프래그먼트가 표시되고 포커스가 있음.
  - DESTROYED: 프래그먼트 객체의 인스턴스화가 취소됨.


<br><br><br>

### 프래그먼트 콜백메서드
 - onCreate() : 프래그먼트가 인스턴스화되었고 `CREATED` 상태. 그러나 이에 상응하는 뷰가 아직 만들어지지 않았음.
 - onCreateView() : 레이아웃을 확장함. 프래그먼트가 `CREATED` 상태로 전환됨.
 - **onViewCreated()** : 뷰가 만들어진 후 호출됨. 일반적으로 findViewById()를 호출하여 특정 뷰를 속성에 바인딩. 개별 뷰의 속성 설정 
 - onStart() : 프래그먼트가 `STARTED` 상태로 전환됨.
 - onResume() : 프래그먼트가 `RESUMED` 상태로 전환됨. 포커스 보유.(사용자 입력에 응답할 수 있음).
 - onPause() : 프래그먼트가 `STARTED` 상태로 다시 전환됨. UI가 사용자에게 표시됨.
 - onStop() : 프래그먼트가 `CREATED` 상태로 다시 전환됨. 객체가 인스턴스화되었지만 화면에 표시되지 않음.
 - onDestroyView() : 프래그먼트가 `DESTROYED` 상태로 전환되기 직전 호출됨. 뷰는 메모리에서 삭제되었지만 프래그먼트 객체는 존재함.
 - onDestroy() : 프래그먼트가 `DESTROYED` 상태로 전환됨.

![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component/img/74470aacefa170bd.png?hl=ko)


<br><br><br>

### 프래그먼트 생성
1. 프로젝트 탐색기에서 app을 선택
2. File > New > Fragment > Fragment (Blank) 추가 > 클래스와 레이아웃 파일 생성
3. 대화상자에서 Fragment Name 설정

<br><br><br>


### 프래그먼트 구현
1. 뷰 바인딩 클래스 생성 : build.gradle > buildFeatures > viewBinding = true 설정 시 각 레이아웃 파일에 자동 생성
2. 프래그먼트 클래스에서 바인딩 클래스 참조를 가져와 변수로 지정 <br>
   : onCreateView()메서드 호출 전까지 레이아웃 확장이 불가능하므로 null값을 주어야함 <br>
   (ex : private var _binding: FragmentLetterListBinding? = null)
3. binding 변수를 만들어 _binding!!으로 저장
   (ex : private val binding get() = _binding!!)
4. onCreate() 구현   
    ```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    ```
5. onCreateView()에서 레이아웃 확장
    ```kotlin
    override fun onCreateView( 
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }    
    ```
6. onViewCreate() 구현
7. onDestroy() 구현
    ```kotlin
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    ```
8. onCreateOptionMenu() ( Activity 클래스 > menuInflater 의 역할)
    ```kotlin
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }
    ```

<br><br><br>

## 탐색 구성요소
* 탐색 그래프 : 앱에서 탐색을 시각적으로 보여주는 XML 파일. 
* `NavHost` : 활동 내에서 탐색 그래프의 대상을 표시하는 데 사용. 프래그먼트 간에 이동하면 NavHost에 표시되는 대상이 업데이트됨. `MainActivity`에서 `NavHostFragment`라는 기본 제공 구현을 사용함.
* `NavController` : `NavHost`에 표시되는 대상 간 탐색 제어 가능. `navigate()` 메서드를 호출하여 표시되는 프래그먼트 교체 가능. 시스템의 '위로' 버튼에 응답하여 이전에 표시된 프래그먼트로 다시 이동하는 등 일반적인 작업 처리 가능.


<br><br>

### 탐색 종속 항목
1. 프로젝트 수준 build.gradle > buildscript > ext 에 nav_version 추가
    ``` gradle
    buildscript {
    ext {
        appcompat_version = "1.2.0"
        constraintlayout_version = "2.0.2"
        core_ktx_version = "1.3.2"
        kotlin_version = "1.3.72"
        material_version = "1.2.1"
        nav_version = "2.3.1"
    }
    ...
    ``` 
2. app 수준 build.gradle > dependencies 에 다음 항목 추가
    ``` gradle
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"
    ```

<br><br><br>

### Safe Args 플러그인
* 프래그먼트 간에 데이터를 전달할 때 유형 안전성을 지원하는 Gradle 플러그인
* 탐색 작업의 클래스와 메서드를 자동 생성하여 인수를 통해 유형 안정성 보장
1. 프로젝트 수준 build.gradle > buildscript > dependencies 에 다음 경로 추가
    ``` gradle
    classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
    ``` 
2. app 수준 build.gradle > plugins 에 다음 항목 추가
    ``` gradle
    plugins {
        ...
        id 'androidx.navigation.safeargs.kotlin'
    }
    ```
3. 상단 배너의 'Sync Now' 버튼 클릭


<br><br><br>

### 탐색 그래프 NavGraph
: 앱 탐색의 가상 매핑. 화면/프래그먼트는 이동 가능한 대상으로 NavGraph는 각 대상이 관련되는 방식을 xml 파일로 나타낼 수 있음
<br>

* MainActivity에서 FragmentContainerView 사용
  1. activity_main.xml에서 `FragmentContainerView` 추가 <br>
  2. name 속성 : 특정 프래그먼트 지정이 가능하지만 NavHostFragment로 설정 시 FragmentContainerView가 프래그먼트 간 이동 가능
  3. app:defaultNavHost 속성 : 프래그먼트 컨테이너가 탐색계층구조와 상호작용 가능
  4. app:navGraph="@navigation/nav_graph" 속성 : 프래그먼트의 이동방법을 정의하는 xml 파일을 가리킴 
  5. FrameLayout에 xmlns:app 속성 추가
    ``` xml
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
            app:navGraph="@navigation/nav_graph" />
    </FrameLayout>
    ```
<br>

* 탐색 그래프 설정
  1. File > New > Android Resource File 추가 <br>
     File name : nav_graph.xml <br>
     Resource type : Navigation <br>
     Directory name : navigation 
  2. nav_graph.xml 파일에서 프래그먼트 대상 추가    
   
<br>

* 탐색 작업 생성
  1. 프래그먼트 대상 위에 표시되는 원에서 다음 대상으로 드래그
  2. 생성된 화살표 클릭시 작업 확인 가능

<br>

* 프래그먼트 인수 지정
  1. nav_graph.xml 파일에서 프래그먼트 선택
  2. attribute > Arguments > + 버튼
  3. Name : letter <br>
     Type : String  -> 탐색 작업이 실행될 때 String이 예상됨

<br>

* 시작 대상 설정
  1. nav_graph.xml 파일에서 시작 대상 클릭
  2. 상단 바에서 'Assign start destination'버튼 클릭

<br><br><br>

### 탐색 Action 추가
1. 탐색 작업과 관련된 클래스에 onClickListener 추가 후 탐색 action 추가
2. 탐색 작업 참조가 있을 경우 NavController(탐색 작업 실행을 허용하는 객체) 참조를 가져와서 작업을 전달하는 navigate() 호출
    ```kotlin
        holder.button.setOnClickListener {
            val action = LetterListFragmentDirections.actionLetterListFragmentToWordListFragment( letter = holder.button.text.toString())
            holder.view.findNavController().navigate(action)

        }
    ```
 

<br><br><br>

### MainActivity 구성
1. navController 생성
2. onCreate() 에서 setContentView() 호출 후 nav_host_fragment 참조를 navController 에 할당
3. onCreate() 에서 setupActionBarWithNavController() 호출 후 navController를 인자로 전달
4. onSupportNavigateUp() 구현 : 위로 버튼 처리

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true

    private lateinit var navController: NavController       //1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)    

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController       //2
        setupActionBarWithNavController(navController)      //3
    }// onCreate


    override fun onSupportNavigateUp(): Boolean {           //4
        return navController.navigateUp() || super.onSupportNavigateUp()
    }   
}
```

<br><br><br>

### 프래그먼트에서 인수 가져오기
1. letterId 변수 생성
2. onCreate() 재정의
3. 어댑터의 **onViewCreated()**의 activity?.intent?.extras?.getString(LETTER).toString()를 letterId 로 변경

```kotlin
class WordListFragment : Fragment() {

    private var _binding: FragmentWordListBinding? = null
    private val binding get() = _binding!!

    private lateinit var letterId : String      //1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {                        //2
            letterId = it.getString(LETTER).toString()
        }
    }
    
    ...

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = WordAdapter(letterId, requireContext())      //3

        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }
    ...
}
```

<br><br><br>

### 프래그먼트 라벨 업데이트
* label 속성 : 상위 활동이 알고 앱 바에서 사용할 label 
1. strings.xml 에서 속성 추가
2. nav_graph.xml > 각 프래그먼트의 Attributes > label 설정 : @String/[label]
   (ex : @String/app_name)

 