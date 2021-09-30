# Navigate between screens

## Collectiosn

- 순서가 지정되거나. 안되거나
- 고유하거나 고유하지 않거나

### list

- 순서가 있음
- 항목이 고유할 필요 없음

```kotlin
fun main() {
    val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    println("list:   ${numbers}")
}
// list:   [0, 3, 8, 4, 0, 5, 5, 8, 9, 2]
```

> `list.sorted()` : sort

<br>

### set

- 순서가 없음
- 중복이 없음

```kotlin
val setOfNumbers = numbers.toSet() // list to set
println("set:    ${setOfNumbers}")
//set:    [0, 3, 8, 4, 5, 9, 2]

val set1 = setOf(1,2,3) //변경 불가능한 집합
val set2 = mutableSetOf(3,2,1) //변경 가능한 집합
```

> `set.contains()` : 특정 항목이 집합에 속하는지 `return true or false`  
> `union()`  
> `intersect()`

<br>

### map

- 키-값 쌍
- 키는 고유, 값은 중복 가능
- 특정 키가 부여된 값을 쉽게 찾을 수 있도록

```kotlin
fun main() {
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )
    println(peopleAges)
}
//{Fred=30, Ann=23}
```

> `map.put(<key>, <value>)` : 항목 추가  
> `map[<key>] : <value>` : 약식 항목 추가, 업데이트

<br>

### forEach

= `for (people in peopleAges) { ... }`

- 특수 식별자 `it` : 현재 항목의 변수 지정

```kotlin
peopleAges.forEach { print("${it.key} is ${it.value}, ") }
```

<br>

### `map()`

- 사전 컬렉션과 다름
- 각 항목에 변환을 적용

```kotlin
println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ") )
```

- `peopleAges` 의 각 항목에 변환을 적용하고 변환된 항목으로 이뤄진 새로운 collections 생성
- `joinToString()` : 변환된 각 항목에 문자열 추가, 마지막 항목에서는 추가하지 않음

<br>

### `filter()`

- 표현식을 기반으로 일치하는 항목 반환

```kotlin
fun main() {
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23,
        "movie" to 24,
    )
    val filteredNames = peopleAges.filter { it.key.length < 4 }
	println(filteredNames)
}
//{Ann=23}
//키 길이가 4글자 미만인 항목만 filter, 반환되는 유형은 LinkedHashMap
```

<br>

### lambda

```kotlin
peopleAges.forEach { print("${it.key} is ${it.value}") }
```

- 위와 같이 `{}` 중괄호 안에 작은 함수를 작성하는 것과 같지만 함수의 이름이 존재하지 않는 것을 람다 표현식이라고 한다.

```kotlin
val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
```

- 익명함수

<br>

### Function Type

```kotlin
(Int) -> Int
// 매개변수 Int 타입, 반환형식 Int 타입
```

<br>

```kotlin
fun main() {
    val triple: (Int) -> Int = { a: Int -> a * 3 }
    println(triple(5)) // 15
    //params : a , return : a * 3
}
```

- 단일 매개변수가 있는 lambda는 매개변수에 특수 식별자 `it` 사용가능

```kotlin
val triple: (Int) -> Int = { it * 3 }
```

<br>

### 고차 함수

- 함수의 인자로 함수를 넘기거나 함수를 리턴하는 함수
- 람다를 다른 함수로 전달하거나, 다른 함수에서 함수를 반환하는 것
- `map`, `filter`, `forEach` 는 모두 매개변수로 함수를 사용했으니 고차함수의 예

```kotlin
peopleAges.filter { it.key.length < 4 }
```

<br>

#### 고차 함수의 예시

#### `sortedWith()`

- 그냥 정렬이면 sorted() 메서드를 쓰면된다. 그런데 문자열 길이를 **기준**으로 목록을 정렬하려면?

> 정렬기준  
> 첫번째 객체가 두번째 객체보다 작으면 0보다 작은 값 반환, 반대면 0보다 큰 값 반환

```kotlin
fun main() {
    val peopleNames = listOf("Fred", "Ann", "Barbara", "Joe")
    println(peopleNames.sorted()) // 사전순
    println(peopleNames.sortedWith { str1: String, str2: String -> str1.length - str2.length })
}
```

- 동일한 유형의 매개변수를 사용
- `Int` 타입을 반환해야 한다.
- `str1`이 `str2`보다 짧으면 0보다 작은 값을 반환. `str1`과 `str2`의 길이가 같은 경우 0을 반환. `str1`이 `str2`보다 긴 경우 0보다 큰 값을 반환. (이름 길이 비교 기준으로 오름차순 정렬)

<br>

### Android의 lambda

#### `onClickListener()`

```kotlin
calculateButton.setOnClickListener{ calculateTip() }
// 버튼 클릭시 calculateTip() 함수 실행
```

<br>

#### `onKeyListener()`

```kotlin
costOfServiceEditText.setOnKeyListener { view, keyCode, event -> handleKeyEvent(view, keyCode) }
```

- 팁 계산기에서 Enter누르면 키보드 숨기기때 썼던 것 (키보드 리스너)

<br>
<br>

### 단어 목록 만들기

```kotlin
fun main() {
    val words = listOf("about", "acute", "awesome", "balloon", "best", "brief", "class", "coffee", "creative")
    //단어 리스트
	val filteredWords = words.filter { it.startsWith("b", ignoreCase = true) }
	println(filteredWords)
}
```

- `startsWith()` : 지정된 문자열로 시작하는 문자열인 경우 `true` 반환
- `ignoreCase = true` : 대소문자를 구분하지 않음

1. 단어 순서를 무작위로 지정하고 싶다면 ?

- `collections.shuffled()` : 항목이 무작위로 섞인 컬렉션 사본을 만듦

2. 몇개의 단어만 필요하다면 ?

- `val filteredWords = words.filter { it.startsWith("b", ignoreCase = true) }.shuffled().take(2)` : 필터링된 단어의 처음 2개만 가져옴

3. 이 마저도 정렬하고 싶다면 ?

- `.sorted()`

<br>

## Words 앱 만들기

![image](https://user-images.githubusercontent.com/52737532/135260883-5aaa0586-0f67-49f7-b9df-4dc71a5b337a.png)

- `LetterAdapter` - `RecycleView`
  - Letter 클릭시 `onClickListener` --> `DetailActivity` 로 이동
- `WordAdapter` - `RecycleView`

  - `DetailActivity` 에서 단어 목록을 표시하는데에 사용

- 첫번째 화면에서 사용자가 문자(알파벳)를 탭하면 단어 목록이 있는 두번째 화면으로 넘어가야 한다.

<br>
<br>
<br>

### 인텐트 (`intent`)

- 실행할 작업을 나타내는 객체
- 화면 전환(activity 전환)할 때 쓰임

종류

- 명시적 인텐트 : 구체적, 실행할 활동을 정확하게 알 수 있고 자체 앱 화면인 경우가 많다.
- 암시적 인텐트 : 추상적, 시스템에서 링크 열기, 이메일 작성, 전화 걸기 등 작업 유형을 알려줌

![image](https://user-images.githubusercontent.com/52737532/135255006-52aaca90-c56d-4118-8ada-08ef6e673087.png)

ex) 암시적 인텐트 - 페이지를 공유하는 데 사용할 앱 묻는 메뉴

<br>

### `putExtra()`

- intent를 사용해서 데이터를 전달
- intent에 값을 넣는데에 사용
- 첫번째 인자에는 `key`, 두번째 인자에는 `data`를 넣으면 됨

<br>
<br>
<br>

### 명시적 인텐트 설정 예시

- 첫번째 화면에서 문자를 탭하면 단어 목록이 있는 두번째 화면으로 이동하도록

Letter -> word

```kotlin
//LetterAdapter.kt
//onBindViewHolder()

holder.button.setOnClickListener {
    val context = holder.view.context //1
    val intent = Intent(context, DetailActivity::class.java) //2
    intent.putExtra("letter", holder.button.text.toString()) //3
    context.startActivity(intent) //4
}

```

1. `context` 참조 가져오기
2. `Intent` 를 만들어서 컨텍스트와 대상 활동 클래스 이름 전달
   - 괄호 안에 들어가야 할 것
   - `Intent(<출발할 activity>, <도착할 activity>)`
   - 만약에 `this` 를 쓴다면 현재 activity
3. `putExtra` 를 사용해서 데이터 전달 (버튼에 쓰여있는 letter)
   - 버튼의 text는 `CharSequence` 타입으로 인터페이스, `String`으로 바꾸어 주기
4. `startActivity()` : Intent를 전달
   - 얘를 사용해줘야 화면이 넘어간다.

<br>

> **content**
>
> context : 앱이나 객체의 현재 상태
>
> - 자원에 접근하게 해준다.
> - context를 가지면 자원에 접근할 수 있다.

<br>

> - <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit2/%5B3%5D%20Display%20a%20scrollable%20list.md#%EC%A0%95%EB%A6%AC">adater</a>

<br>

### DetailActivity 설정

```kotlin
//DetailActivity.kt
//onCreate()

val letterId = intent?.extras?.getString("letter").toString()
```

- `intent` : 모든 활동의 속성
- `extras` : `bunble` 타입
  - intent에 전달된 모든 extras에 access하는 방법을 제공
- `?` : `null`을 허용하기 때문에 값이 없을 때에는 속성에 access하지 않도록

<br>

### 중간 정리

```kotlin
//LetterAdapter.kt
//onBindViewHolder()

..

intent.putExtra("letter", holder.button.text.toString()) //1

//DetailActivity.kt
//onCreate()

..

val letterId = intent?.extras?.getString("letter").toString() //2
```

1. 버튼에서 text 값을 가져와서 name `letter` 로 intent에 전달한다.
2. name letter로 전달된 letter를 가져와서 `letterId`로 설정한다.

<br>

### `companion` 객체를 사용해서 문자열 하드코딩 없애기

- 하드코딩하면 extras가 많으면 비효율적이다.

`companion`

- 특정 인스턴스 없이 상수(const)를 구분하여 사용할 수 있다.
- companion 객체는 싱글톤 패턴

```kotlin
//DetailActivity.kt
//onCreate() 바로 위

companion object {
    const val LETTER = "letter"
}

//DetailActivity.kt
val letterId = intent?.extras?.getString(LETTER).toString()

//LetterAdapter.kt
intent.putExtra(DetailActivity.LETTER, holder.button.text.toString())
```

<br>

### 암시적 인텐트 설정

- 앱이 실행하려는 활동이나 앱을 알 수 없을 때가 있다.
- 예시에서 Google에서 제공하는 사전 브라우저로 넘어가고 싶을 때
- 그렇다면 꼭 Google Chrome 브라우저를 로드하는 intent가 필요한가? (명시적 인텐트) 그렇지 않고 사용자가 선호하는 브라우저로 넘어갈 수도 있어야한다.
- 여기서 개발자는 사용자가 어떤 앱을 설치했는지 알 수 없다. 이때 암시적 인텐트 사용

```kotlin
//DetailActivity.kt

companion object {
    const val LETTER = "letter"
    const val SEARCH_PREFIX = "https://www.google.com/search?q="
    // 모든 검색에 동일한 url이 사용되므로 상수로 만들기
}

//WordAdapter.kt
holder.button.setOnClickListener {
    val queryUrl: Uri = Uri.parse("${DetailActivity.SEARCH_PREFIX}${item}")
    // 검색어의 uri 만들기
    val intent = Intent(Intent.ACTION_VIEW, queryUrl) //1
    context.startActivity(intent)
}
```

1. `ACTION.VIEW` : URL를 사용하는 일반적인 인텐트 - 웹 주소 실행

> `CATEGORY_APP_MAPS` - 지도 앱을 실행합니다.  
> `CATEGORY_APP_EMAIL` - 이메일 앱을 실행합니다.  
> `CATEGORY_APP_GALLERY` - 갤러리(사진) 앱을 실행합니다.  
> `ACTION_SET_ALARM` - 백그라운드에서 알람을 설정합니다.  
> `ACTION_DIAL` - 전화를 겁니다.

<br>

### 메뉴 옵션 만들어서 목록 or 그리드 레이아웃 전환하게

- <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit2/%5B2%5D%20Get%20user%20input%20in%20an%20app%20-%20part2.md#%EC%95%84%EC%9D%B4%EC%BD%98-%EC%B6%94%EA%B0%80%ED%95%98%EA%B8%B0">아이콘 추가하기</a>

- 아이콘을 시스템에 알리기
  1.  res 우클릭 > New > Android Resource File >
  2.  Resource Type : `Menu`, Fil Name : `layout_menu`
  3.  `res/Menu/layout_menu` > `layout_menu.xml` 내용 바꾸기

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto">
   <item android:id="@+id/action_switch_layout"
       android:title="@string/action_switch_layout"
       android:icon="@drawable/ic_linear_layout"
       app:showAsAction="always" />
</menu>
```

- `id`
- `title` : 표시되지 않음, 스크린 리더용
- `icon` : 기본값은 `ic_linear_layout`
- `showAsAction="always"` : 시스템에 버튼을 항상 표시

<br>

```kotlin
//MainActivity.kt

private var isLinearLayoutManager = true
// 레이아웃 상태 추적

private fun chooseLayout() {
    if (isLinearLayoutManager) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        //레이아웃 관리자 할당
    } else {
        recyclerView.layoutManager = GridLayoutManager(this, 4)
    }
    recyclerView.adapter = LetterAdapter()
    //어댑터 할당
}
```

- <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit2/%5B3%5D%20Display%20a%20scrollable%20list.md#recyclerview">레이아웃 관리자</a>
  - `RecyclerView` 의 레이아웃은 layout manager에서 처리한다.

<br>

> **inflate**
>
> xml에 씌워져있는 view정의를 실제 view 객체로 만드는 역할

<br>

이제 `MainActivity.kt`수정해서 돌아가게 하면 된다.

```kotlin
//MainActivity.kt
    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        recyclerView.adapter = LetterAdapter()
        //어댑터 할당
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 옵션 메뉴를 확장하여 추가 설정 실행
        // 레이아웃에 따라 아이콘이 올바른지 확인 = setIcon

        menuInflater.inflate(R.menu.layout_menu, menu)
        //실제 뷰로 만듦

        val layoutButton = menu?.findItem(R.id.action_switch_layout)
        //레이아웃 변경하는 아이템을 찾아서 걔를 setIcon에 넣어줘서 icon 변경
        setIcon(layoutButton)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //레이아웃 변경 아이템 버튼이 클릭될 때 chooseLayout()호출
        //id가 action_switch_layout인 항목을 클릭하면
        //isLinearLayoutManager 값을 바꿔줌


        //메뉴가 클릭될 때마다 호출되는데 그 메뉴중에 어떤 항목을 누르는지 확인해야함
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout() //레이아웃 바꾸고
                setIcon(item) //그 레이아웃에 맞게 아이콘도 바꿈

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

```

1. `chooseLayout()` : `isLinearLayoutManager`에 따라 레이아웃 manager 할당
2. `setIcon()` : `isLinearLayoutManager`에 따라 버튼 아이콘 변경
3. `onCreateOptionsMenu()` : xml에 있는 menu를 진짜 view로 만들고 아이콘 변경
4. `onOptionsItemSelected()` : 메뉴가 클릭될 때 호출되는 데, 클릭된 것이 id가 `action_switch_layout`인 항목인지 확인하고 레이아웃 바꾸고, 아이콘도 바꿈

<br>
<br>
<br>

## life cycle

- 수명주기 : activity가 생성되는 시점부터 없어져서 자원이 회수될 때까지

![image](https://user-images.githubusercontent.com/52737532/135298921-674c31c6-38ad-4213-90cb-2569a935cd73.png)

- android 로깅 기능을 사용하면 쉽게 문제 방생 위치를 파악 가능

<br>

### `onCreate()`

- activity의 일화성 초기화 실행
- ex) 레이아웃 확장, 리스너 정의, 뷰 결합
- activity가 초기화된 직후 한 번 호출 (activity 객체가 메모리에 만들어질 때)
- 재정의하여 사용할 때 슈퍼클래스 구현해서 activity 생성을 완료해야 하기때문에 즉시 `super.onCreate()`를 호출해 주어야한다.
- `setContentView()`에서는 화면에 무엇을 보여줄 것인지 설정하면 됨

<br>
<br>

---

> **Log**
>
> Logcat에 메시지를 씀 : Logcat은 메시지를 기록하는 콘솔
>
> `Log.d()` : 메서드가 메시지 작성  
> `Log.i()` : 정보 메시지  
> `Log.e()` : 오류 메시지  
> `Log.w()` : 경고 메시지  
> `Log.v()` : 자세한 메시지
>
> `Log.d(<보통 클래스 이름 문자열>, <실제 로그 메시지>)`
>
> **Logcat**
>
> - 필터링 가능  
>   `D/` : `Log.d()`로 만든 메시지만 표시  
>   ex | `D/MainActivity`

---

<br>
<br>

### `onStart()`

- `onCreate()` 직후에 호출
- `onStart()`가 실행되면 activity가 화면에 표시됨
- `onCreate()`와 달리 수명주기에서 여러번 호출 가능
- `onStop()` 메서드와 짝임
- 사용자가 앱을 시작 후, 기기의 홈화면으로 돌아가면 stop
- 다시 앱을 누르면 start

<br>
<br>

---

> **overide 가능한 메서드**
>
> 코드에 마우스 찍고 `ctrl + o`

---

<br>

```kotlin
const val TAG = "MainActivity"

//onCreate()
...
Log.d(TAG, "onCreate Called") //1

override fun onStart() {
   super.onStart()
   Log.d(TAG, "onStart Called") //2
}
```

```
com.example.android.dessertclicker D/MainActivity: onCreate Called //앱 가장 처음 실행
com.example.android.dessertclicker D/MainActivity: onStart Called //그 다음 실행
com.example.android.dessertclicker D/MainActivity: onStart Called //기기 홈화면으로 간다음에 다시 앱으로 돌아왔을 때
```

<br>

### `onResume()`

- activity 포커스를 제공하고 사용자가 상호작용할 수 있도록 활동을 준비

<br>

> 앱시작
>
> `onCreate` -> `onStart` -> `onResume`
>
> 앱에서 뒤로가기 눌렀을 때
>
> `onPause` -> `onStop` -> `onDestroy`

- `onDestroy()` : activity가 완전히 종료되었으며 가비지 컬렉션 될 수 있음, 앱에서 사용하는 리소스 정리
  - 이 친구도 한번만 호출

<br>
<br>
<br>

### 앱 간 전환

- 기기 홈으로 들어가서 새 앱을 시작하거나, 전화가 와서 중단될 떄에는?
- 사용자가 앱으로 돌아오면 동일한 활동이 다시 시작되어 다시 표시된다. (표시 수명주기)

<br>

> activity가 화면에 표시되지 않으면 activity는 **백그라운드**에 배치  
> 화면에 표시되면 **포그라운드**에 있음  
> 사용자가 앱으로 돌아오면 동일한 활동이 시작되어 화면에 다시 표시

<br>

- Activity 수명 주기와 그 콜백을 사용하여 앱이 백그라운드로 이동하는 시점을 알 수 있어 진행 중인 작업을 일시중지가능. 그런 다음 앱이 포그라운드로 전환될 때 작업을 다시 시작.

<br>

1. 앱을 실행하고
   - **`onCreate` -> `onStart` -> `onResume`**
2. 컵케이크롤 몇 번 클릭
3. 홈 버튼을 누르고 Logcat 확인
   - **`onPause` -> `onStop`**
   - `onPause`가 실행되어 앱에 포커스가 없다.
   - `onStop`이 되어 앱이 화면에 표시되지 않는다.
   - `onDestroy`는 호출되지 않는다. 활동이 중지되었지만 백그라운드에서 메모리에 있기 때문에 활동 리소스 유지 (사용자가 앱으로 돌아올 수 있으므로)
4. 다시 앱으로 돌아가기
   - **`onRestart` -> `onStart` -> `onResume`**
   - `onCreate()`는 실행되지 않는다. activity 객체가 소멸되지 않았으므로 다시 안 만들어도 되기 때문
   - `onRestart()`가 `onCreate()`대신 실행

<br>

### `onRestart()`

- `onCreate()`와 유사
- `onRestart()`는 활동이 처음으로 시작되지 않은 경우에만 호출하려는 코드를 배치

<br>

**포커스와 가시성**

> `onStart()`로 앱이 화면이 표시되고 `onResume()`으로 앱이 사용자의 포커스를 확보하면 사용자와 앱이 상호작용할 수 있게 되고 이를 **대화형 수명주기**라고 한다.

<br>

> `onPause()`후에 포커스가 상실되고, `onStop()`이후에 앱이 표시되지 않는다.

<br>

### 부분적으로 활동 숨기기

- 화면이 부분적으로 표시는 되지만 포커스가 없는 경우 (포커스는 없는데 가시성은 있는 경우)

![image](https://user-images.githubusercontent.com/52737532/135311557-dd65c298-4b4e-4dcd-8b0d-4d5b36b51860.png)

- 화면 절반이 표시되지만 activity 조작 불가 상태
- `onPause()`만 호출됨

<br>

### `onPause()`

- 앱이 멈춘 것처럼 보이지 않도록
- 예를 들어 전화가 걸려오면 `onPause()`의 코드는 수신 전화 알림을 지연시킬 수 있음

<br>
<br>

### 구성 변경

- 구성 변경 : 기기 상태가 급격하게 변해서 시스템이 변경사항을 확인하는 가장 쉬운 방법이 활동을 완전히 종료하고 다시 빌드하는 것일 때
- 예시) 기기 언어 변경, 화면 회전

**화면 회전시**

1. onPause Called
2. onStop Called
3. onDestroy Called
4. onCreate Called
5. onStart Called
6. onResume Called

- 모든 수명주기를 종료하고 다시 활동 시작 (가격이 0으로 돌아감)

<br>

### `onSaveInstanceState()`를 사용하여 번들 데이터 저장

- `onSaveInstanceState()`는 activity가 소멸되면 필요할 수 있는 데이터 저장
- 활동이 중지된 후 호출, 앱이 백그라운드로 전활될 때 마다 호출

![image](https://user-images.githubusercontent.com/52737532/135313729-86f2fa06-5731-40f1-acab-f9cc91e4cbd3.png)

- `onStop` -> `onSaveInstanceState`

<br>

> Bundle
>
> 키-값쌍 모음으로 키는 항상 문자열

<br>

- 번들에 데이터 저장

```kotlin
//onSaveInstanceState()

outState.putInt(<키>, <값>) //Int
// putFloat(), putString() 등등
```

- 번들 데이터 복원

```kotlin
override fun onCreate(savedInstanceState: Bundle) {
    //onCreate()에서 번들을 가져와서 복원
    //활동이 새로 시작되었다면 bundle은 null
    //null이 아니라면 다시 생성하고 있음을 알 수 있다.

    ...

    if (savedInstanceState != null) {
        revenue = savedInstanceState.getInt(KEY_REVENUE, 0)
        dessertsSold = savedInstanceState.getInt(KEY_DESSERT_SOLD, 0)
    }

    ...
}
```

- `savedInstanceState.getInt(<키>, <번들의 키에 값이 없는 경우의 기본값>)`
