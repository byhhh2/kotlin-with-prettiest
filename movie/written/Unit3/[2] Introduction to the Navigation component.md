# Introduction to the Navigation component

<br>

- 대다수 안드로이드 앱에는 화면마다 별도의 activity가 필요하지 않다.
- 일반적인 UI 패턴 (ex 탭)이 fragment라는 section을 사용하는 단일 activity내에 존재

<br>

## Fragment

- 앱 UI의 재사용 가능한 부분
- 자체 수명주기 보유
- 독립적으로 존재할 수 없고 다른 fragment에서 호스팅되어야 한다.
- 만약 하단 탭에서 다른 탭을 클릭한다면 Intent가 트리거되지 않고 fragment가 다른 fragment로 교체된다.
- 태블릿같은 큰 화면은 여러 fragment 한번에 표시가능

<br>

### Fragment의 수명주기

`INITIALIZED`: 프래그먼트의 새 인스턴스가 인스턴스화되었습니다.  
`CREATED`: 첫 번째 프래그먼트 수명 주기 메서드가 호출됩니다. 이 상태에서 프래그먼트와 연결된 뷰도 만들어집니다.  
`STARTED`: 프래그먼트가 화면에 표시되지만 '포커스'가 없으므로 사용자 입력에 응답할 수 없습니다.  
`RESUMED`: 프래그먼트가 표시되고 포커스가 있습니다.  
`DESTROYED`: 프래그먼트 객체의 인스턴스화가 취소되었습니다.

<br>

**수명 주기 이벤트에 응답하기 위해 재정의할 수 있는 메서드들**

`onCreate()`: 프래그먼트가 인스턴스화되었고 CREATED 상태입니다. 그러나 이에 상응하는 뷰가 아직 만들어지지 않았습니다.  
`onCreateView()`: 이 메서드에서 **레이아웃을 확장**합니다. 프래그먼트가 `CREATED` 상태로 전환되었습니다.  
`onViewCreated()`: 뷰가 만들어진 후 호출됩니다. 이 메서드에서 일반적으로 findViewById()를 호출하여 특정 뷰를 속성에 바인딩합니다.  
`onStart()`: 프래그먼트가 `STARTED` 상태로 전환되었습니다.  
`onResume()`: 프래그먼트가 `RESUMED` 상태로 전환되었고 이제 포커스를 보유합니다(사용자 입력에 응답할 수 있음).  
`onPause()`: 프래그먼트가 `STARTED` 상태로 다시 전환되었습니다. UI가 사용자에게 표시됩니다.  
`onStop()`: 프래그먼트가 `CREATED` 상태로 다시 전환되었습니다. 객체가 인스턴스화되었지만 더 이상 화면에 표시되지 않습니다.  
`onDestroyView()`: 프래그먼트가 `DESTROYED` 상태로 전환되기 직전에 호출됩니다. 뷰는 메모리에서 이미 삭제되었지만 프래그먼트 객체는 여전히 있습니다.  
`onDestroy()`: 프래그먼트가 `DESTROYED` 상태로 전환됩니다.

> activity `onCreate()` : 레이아웃을 확장하고 뷰를 바인딩  
> fragment `onCreate()` : 뷰가 만들어지기 전에 호출되어 레이아웃 확장 불가능
>
> - `onCreateView()`에서 확장가능 (여기서 특정 뷰에 바인딩)

<br>

![image](https://user-images.githubusercontent.com/52737532/135326131-c276108d-4c52-4ae9-9e8f-48cd60007eac.png)

<br>
<br>

### Fragment 만들기

- fragment도 activity와 마찬가지로 `kt + xml`로 구성
- `app 우클릭 > New > Fragment > Fragment(blank)` : 클래스와 레이아웃 모두 생성됨

<br>

---

#### View binding

- `build.gradle`에서 `viewBinding` 속성이 `true`이면 바인딩 객체가 각 레이아웃 파일에 생성된다.
- 각 뷰의 fragment class의 속성에 바인딩 객체를 할당하기만 하면 된다. (참조를 가져오면 됨)

---

<br>

```kotlin
private var _binding: FragmentLetterListBinding? = null
```

- 초기값은 `null`이어야 한다.
  - `onCreateView()`가 호출되기 전까지 레이아웃을 확장할 수 없기 때문
  - 인스턴스가 만들어지는 시점 (`onCreate()`가 호출될 때)와 이 속성을 실제로 사용할 수 있는 시점이 차이가 있기 때문
- 이런 이유로 `_binding`속성에 access할 때마다 `?`를 포함해야하는데 `?`를 다수 포함하고 싶지 않으면 `!!` 추가 가능하다.
  - `!!` : `null`이 아님을 확신하는 경우
    - `_binding`이 `onCreateView()`에서 할당된 후 값을 보유하는 것을 아는 것처럼 확실한 경우에만 사용
- 속성 앞에 `_`을 붙이면 엑세스하지 말라는 말

<br>

```kotlin
private val binding get() = _binding!!
// get() : get-only, 변경 불가
```

<br>

### 차근 차근 해보자 😂

<br>

---

> **binding**
>
> - 각 레이아웃 파일의 binding 클래스 생성
> - activity_main.xml -> ActivityMainBinding (생성된 클래스)
>
> **inflate**
>
> `inflate()` = 바인딩 객체 생성 (= 뷰 객체로 인스턴스화)
>
> **setContentView()**
>
> setContentView(binding.root) = 바인딩 객체를 뷰 계층 구조에 결합
>
> **host**
>
> fragment는 host activity에 포함

---

<br>

1. 뷰를 만들자. (binding 참조하고, inflate)

```kotlin
    private var _binding: FragmentLetterListBinding? = null
    //FragmentLetterListBinding 참조 가져오기
    //onCreateView() 전에 레이아웃을 확장할 수 없기 때문에 초기값은 null

    private val binding get() = _binding!!
    //onCreateView() 에서 할당된 후 값을 보유하는 것을 알기 때문에 !!를 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //fragment에도 menu가 존재하게
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        //뷰를 inflate해서 바인딩값을 설정

        val view = binding.root
        return view
        //루트뷰 반환
    }
```

- view binding과 inflate

<br>

2. 뷰가 만들어진 이후

```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   recyclerView = binding.recyclerView
   //recyclerView(@+id/recycler_view)가져와서 속성값 설정

   chooseLayout()
}
```

<br>

3. 메뉴 만들기

```kotlin
override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
   inflater.inflate(R.menu.layout_menu, menu)
   //menu를 인스턴스화
   //Activity에는 menuInflater라는 전역 속성이 있지만 fragment에 없기 때문에 MenuInflater를 전달 받음

   val layoutButton = menu.findItem(R.id.action_switch_layout)
   setIcon(layoutButton)
}
```

<br>

4. 기능 옮기기

- fragment는 `context`가 아니다. 즉 `context`를 넘겨줘야할 때 `this`를 쓰지 못한다.
- 대신 사용할수 있는 `context` **속성**을 제공한다. 그니깐 그냥 context쓰면 됨

```kotlin
private fun chooseLayout() {
   when (isLinearLayoutManager) {
       true -> {
           recyclerView.layoutManager = LinearLayoutManager(context)
           recyclerView.adapter = LetterAdapter()
       }
       false -> {
           recyclerView.layoutManager = GridLayoutManager(context, 4)
           recyclerView.adapter = LetterAdapter()
       }
   }
}

private fun setIcon(menuItem: MenuItem?) {
   if (menuItem == null)
       return

   menuItem.icon =
       if (isLinearLayoutManager)
           ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
       else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
   return when (item.itemId) {
       R.id.action_switch_layout -> {
           isLinearLayoutManager = !isLinearLayoutManager
           chooseLayout()
           setIcon(item)

           return true
       }

       else -> super.onOptionsItemSelected(item)
   }
}
```

- `ContextCompat` : resource를 가져올때 sdk버전을 고려하지 않아도 되도록 한 클래스
- `requireContext` : context반환 (host Activity의 Context)

<br>
<br>
<br>

## Jetpack

### 탐색 그래프

- 앱에서 탐색을 시각적으로 보여주는 xml파일

### `NavHost`

- activity 내에서 탐색 그래프의 대상을 표시하는데에 사용
- fragment간에 이동하면 `NavHost`에 표시되는 대상이 업데이트 됨

### `NavController`

- `NavHost`에 표시되는 대상 간 탐색을 제어할 수 있다.
- `NavController`의 `navigate()` 메서드를 호출하면 표시되는 fragment를 교체할 수 있다.

<br>
<br>

---

### 종속

1. navigation

```
//build.gradle (project)

buildscript {
    ext {
        appcompat_version = "1.2.0"
        constraintlayout_version = "2.0.2"
        core_ktx_version = "1.3.2"
        kotlin_version = "1.3.72"
        material_version = "1.2.1"
        nav_version = "2.3.1" //
    }

    ...

```

<br>

```
//build.gradle (app)

implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

```

<br>

2. safe args 플러그인

- fragment간에 데이터를 전달할 때 유형 안전성 지원
- 인텐트 간에 `putExtra()`를 사용해서 문자를 전달 했던 것처럼

```
//build.gradle (project)

classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"

...

```

<br>

```
//build.gradle (app)

plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'androidx.navigation.safeargs.kotlin'
}

```

---

<br>

## 탐색 그래프 (`NavGraph`) 사용

- 앱에서 탐색을 시각적으로 보여주는 xml파일

<br>

### `MainActivity`에서 `FragmentContainerView` 사용

<br>

1. mainActivity 설정

- 레이아웃이 letter와 word xml에 있으니깐 activity_main에 없어도 됨
- `FragmentContainerView` 를 포함하자 = 앱의 모든 탐색은 여기서 실행

```kotlin
<androidx.fragment.app.FragmentContainerView
   android:id="@+id/nav_host_fragment"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   android:name="androidx.navigation.fragment.NavHostFragment" [1]
   app:defaultNavHost="true" [2]
   app:navGraph="@navigation/nav_graph" [3]
   />
```

- [1] : `NavHostFragment`로 설정해서 FragmentContainerView가 fragment간에 이동할 수 있도록
- [2] : fragment container가 탐색 계층 구조와 상호 작용할 수 있다.
  - ex | 뒤로가기를 누르면 이전 표시된 fragment로 이동
- [3] : 앱의 fragment가 서로 이동할 수 있는 방법을 정의하는 xml파일을 가르킴

<br>

2. 탐색 그래프 파일 만들기

   1. app 우클릭 > file > new > android resource file
   2. 파일이름은 `nav_graph.xml`. app:navGraph 속성에 설정한 이름과 동일하게
   3. 새로운 대상 추가 fragment_letter_list, fragment_word_list

   <br>

   ![image](https://user-images.githubusercontent.com/52737532/135389041-8846fe55-2fac-4f8b-bb4a-9b8d5ff83a07.png)

   4. 대상에 마우스 가져가서 원을 드래그해서 둘이 연결 (대상 간 탐색 작업 만들기) = `action_letterListFragment_to_wordListFragment`
   5. 탐색에서 `extra` 지정같이 매개변수 전달 지원 가능
      - `wordListFragment` 선택하고 arguments (+)
      - `name`은 `letter`, `string` type
   6. 시작 대상 설정 (가장 먼저 보여줄 화면 선택 - fragement 선택하고 집 모양 버튼 누르면 됨)

<br>

3. 탐색 작업 실행

- `LetterAdapter.kt`

```kotlin
        holder.button.setOnClickListener {
            val action = LetterListFragmentDirections.actionLetterListFragmentToWordListFragment(letter = holder.button.text.toString())
            holder.view.findNavController().navigate(action)
        }
```

- `LetterListFragmentDirections` : `letterListFragment` 로 부터 가능한 모든 탐색 경로 참조가능
- `actionLetterListFragmentToWordListFragment()` : letter -> word
- `NavController` : 탐색 작업 실행을 허용하는 객체를 가져와야함 (`findNavController()`) 그리고 `navigate()`호출

<br>

- `MainActivity.kt`

```kotlin
private lateinit var navController: NavController //[1]


//onCreate
val navHostFragment = supportFragmentManager
    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
navController = navHostFragment.navController //[2]

setupActionBarWithNavController(navController) //[3]

...

override fun onSupportNavigateUp(): Boolean {
   return navController.navigateUp() || super.onSupportNavigateUp()
} //[4]
```

- [1] : navController 속성 만들기 , onCreate에서 설정되므로 lateinit
- [2] : `FragmentContainerView`의 id인 `nav_host_fragment`의 참조를 가져와서 `navController` 에 할당
- [3] : `setupActionBarWithNavController(navController)` : 앱 바 버튼이 표시되도록 (메뉴)
- [4] : 위로 버튼 처리? (아마 뒤로가기 했을 때 이전 fragment로 가는 일 말하는 듯)

<br>

4. `WordListFragment`에서 인수 가져오기

- 기존에는 intent를 참조해서 extra에 엑세스 했음
- 이번에는 nav_graph를 사용하여 탐색을 실행하고 안전 인수를 사용
- 안전 인수에 접근할 때 `onViewCreated()` 때까지 기다리지 않아도 된다.

```kotlin
//WordListFragment
private lateinit var letterId: String

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let { //[1]
        letterId = it.getString(LETTER).toString()
    }
}
```

- [1] : argument는 선택사항
  - `let()`으로 람다 전달 (`argument`가 not null일때)
  - `it` == `argument`
  - `bundle` : 키-값 쌍

<br>

5. 앱 바 수정 (위에 메뉴 제목)

<br>
<br>
<br>

## 동영상 정리

> `navigation drawer activity - templete`
>
> - 간결한 navigation 메커니즘이 들어있음
> - layout inspector로 가서 어떻게 구성되어 있는지 볼 수 있음

<br>

### 구성요소

- `NavHostFragment` : 하나의 컨테이너로 여기서 컨텐츠를 교환, 목적지간 navigation을 할 때 그 목적지를 여기에 추가하면 됨
  - navigation 할 때 컨텐츠를 만들고 호스팅하는 역할
  - navigation 할 때 대체되는 fragment를 담는 container

![image](https://user-images.githubusercontent.com/52737532/135396933-89d00089-c0c8-48f9-959b-a4eaa93d2800.png)

- `NavController` : 실제로 탐색 작업을 한다. 직행 중인 작업을 보여주는 안내자
- `NavigationView` : 네비게이션 메뉴 (네비게이션 구성요소 아님)

![image](https://user-images.githubusercontent.com/52737532/135397251-8e3889c4-b865-4cff-979a-49f562641513.png)

- `NavigationUI` : navHostFragment 외부 컨텐츠 업데이트를 책임 (e.g., NavigationView, BottomNavBar)
  - fragment를 바꿨을때 외부 컨텐츠들도 이를 알 수 있게끔 책임
