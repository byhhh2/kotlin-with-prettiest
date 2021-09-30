# [Unit 3] Pathway1

## 컬렉션

단어 목록이나 직원 기록 모음과 같은 관련 항목 그룹. 

항목 : 순서가 지정되거나 지정되지 않을 수 있으며 고유하거나 고유하지 않을 수 있음.

집합 : 관련 항목의 그룹이지만, 목록과 달리 중복될 수 없으며 순서는 중요하지 않음.

```kotlin
fun main() {
		val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
		println("list:   ${numbers}")     
		println("sorted: ${numbers.sorted()}")

		val setOfNumbers = numbers.toSet()
		println("set:    ${setOfNumbers}")

		val set1 = setOf(1,2,3)
		val set2 = mutableSetOf(3,2,1)

		println("$set1 == $set2: ${set1 == set2}")
		println("contains 7: ${setOfNumbers.contains(7)}")
}
```

```
list:   [0, 3, 8, 4, 0, 5, 5, 8, 9, 2]
sorted: [0, 0, 2, 3, 4, 5, 5, 8, 8, 9]
set:    [0, 3, 8, 4, 5, 9, 2]
[1, 2, 3] == [3, 2, 1]: true
contains 7: false
```

`.toSet()` ,`setOf(집합원소)`  변경 가능한 집합

`mutableSetOf(집합원소)`   변경 불가능한 집합

`.contains(원소)`  해당 원소가 집합 내에 존재하는지 확인

## 맵

특정 키가 부여된 값을 쉽게 찾을 수 있도록 설계된 키-값 쌍의 집합

키는 고유하며 각 키는 정확히 하나의 값에 매핑, 값은 중복 가능

```kotlin
fun main() {
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )
    peopleAges.put("Barbara", 42)
		peopleAges["Joe"] = 51         // 원소 추가 가능 
		peopleAges["Fred"] = 31        //  값 변경 또한 가능
    println(peopleAges)
}
```

```
{Fred=30, Ann=23, Barbara=42, Joe=51}
```

`mustableMapOf<키 변수형, 값 변수형>`  변경가능한 맵, 변수형을 집어 넣음.

`.put(키, 값)`  원소 추가 함수

- 변경 가능한 컬렉션의 경우 가능 작업

```kotlin
peopleAges.forEach { print("${it.key} is ${it.value}, ") }
println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ") )

val filteredNames = peopleAges.filter { it.key.length < 4 }
println(filteredNames)
```

```
Fred is 31, Ann is 23, Barbara is 42, Joe is 51,
Fred is 31, Ann is 23, Barbara is 42, Joe is 51
{Ann=23, Joe=51}
```

`forEach`  루프문, 현재 항목의 변수를 지정하는 대신 특수 식별자 `it` 사용

`.map()` 컬렉션의 각 항목에 변환을 적용하고 변환된 항목으로 이루어진 새 컬렉션 생성

        키-값 쌍을 불러와 문자열로 변환. 예) <Fred, 31>→ Fred is 31

`.joinToString(", ")`  변환된 컬렉션의 각 항목을 문자열에 추가

`filter(조건)`  특정 조건과 일치하는 항목을 찾음

                         맵에 필터를 적용할 때 반환되는 유형은 새 맵 : `LinkedHashMap`

## 람다 및 고차함수

람다 : 이름이 없으며 곧바로 표현식으로 사용할 수 있는 함수

```kotlin
fun main() {
    val triple: (Int) -> Int = { a: Int -> a * 3 }
    println(triple(5))                    // 15
		val triple: (Int) -> Int = { it * 3 }
		println(triple(5))                    // 15
}
// 노랑: 변수이름, 파랑: 변수 유형, 초록: 변수 값(람다)
```

고차함수 : 함수를 다른 함수로 전달하거나 다름함수에서 함수를 반환

map, filter, forEach 함수는 모두 매개변수로 함수를 사용했으므로 고차 함수이다.

```kotlin
fun main() {
    val peopleNames = listOf("Fred", "Ann", "Barbara", "Joe")
    println(peopleNames.sorted())
		println(peopleNames.sortedWith { 
				str1: String, str2: String -> str1.length - str2.length })
}
```

```
[Ann, Barbara, Fred, Joe]
[Ann, Joe, Fred, Barbara]
```

활용

```kotlin
calculateButton.setOnClikListener(object: View.OnClickListener {
		override fun onClick(view: VIew?){
				calculateTip()
		}
}

// 숏코딩
calculateButton.setOnClickListener { view -> calculateTip() }
```

```kotlin
fun main() {
    val words = listOf("about", "acute", "awesome", "balloon", "best", "brief", "class", "coffee", "creative")
    val filteredWords = words.filter { it.startsWith("b", ignoreCase = true) }
        .shuffled()
        .take(2)
        .sorted()
    println(filteredWords)
}
```

```kotlin
[balloon, best, brief]
[brief, balloon, best]
[brief, balloon]
[balloon, brief]
```

## 인텐트

암시적 인텐트 : 좀 더 추상적, 시스템은 요청 처리 방법을 파악

명시적 인텐트 : 매우 구체적, 실행할 활동을 정확하게 알 수 있고 자체 앱의 화면인 경우가 많음.

## 단어 앱 만들기

LetterAdapter.kt

```kotlin
override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
    val item = list.get(position)
    holder.button.text= item.toString()

    holder.button.setOnClickListener{
				val context = holder.view.context
				val intent = Intent(context, DetailActivity::class.java)

        intent.putExtra("letter", holder.button.text.toString())
        context.startActivity(intent)
		}
}
```

DetailActivity.kt

```kotlin
val letterId = intent?.extras?.getString("letter").toString()
```

extras 속성은 `Bundle` 유형이고 인텐트에 전달된 모든 extras에 액세스하는 방법을 제공.

 `intent` 및 `extras` 속성은 null을 허용하므로 값이 있을 수도 있고 없을 수도 있음 → null로 지정

앱이 속성에 액세스하거나 `null` 객체에서 함수를 호출하려고 하면 다운 

→ 이 값에 안전하게 액세스하려면 이름 뒤에 `?`를 입력.

 `intent`가 `null`이면 앱은 extras 속성 액세스를 시도조차 하지 않으며 

`extras`가 null이면 코드에서 `getString()`을 호출하려고 시도조차 하지 않음.

"letter" 하드코딩 해결 → 컴패니언 객체

```kotlin
companion object {
		const val LETTER = "letter"
}

... OnCreat(){
		...
		val letterId = intent?.extras?.getString(LETTER).toString()
		...
}
```

WordAdapter.kt

```kotlin
holder.button.setOnClickListener{
val queryUrl: Uri = Uri.parse("${DetailActivity.SEARCH_PREFIX}${item}")
    val intent = Intent(Intent.ACTION_VIEW, queryUrl)
    context.startActivity(intent)
}
```

URI : Uniform Resource Identifier

→ URL : Uniform Resource Locator

→ URN : Uniform Resource Name

`ACTION_VIEW` URI를 사용하는 일반적인 인텐트

인텐트 유형) 

`CATEGORY_APP_MAPS`  지도 앱을 실행

`CATEGORY_APP_EMAIL`  이메일 앱을 실행

`CATEGORY_APP_GALLERY`  갤러리(사진) 앱을 실행

`ACTION_SET_ALARM`  백그라운드에서 알람을 설정

`ACTION_DIAL`  전화 연결

MainActivity,kt

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView

        chooseLayout()
    }

    private var isLinearLayoutManager = true

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager= LinearLayoutManager(this)
        } else {         
            recyclerView.layoutManager= GridLayoutManager(this, 4)
        }
        recyclerView.adapter= LetterAdapter()
    }

    private fun setIcon(menuItem: MenuItem?) {     // 아이콘 전환
        if (menuItem == null)
            return

        menuItem.icon=
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
				menuInflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu?.findItem(R.id.action_switch_layout)

        setIcon(layoutButton)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout-> {
                isLinearLayoutManager = !isLinearLayoutManager

                chooseLayout()
                setIcon(item)

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
```

`GridLayoutManager(대상, 숫자)`  단일 행에서 여러항목을 허용.

 

## 디저트 클릭 앱 만들기

```kotlin
import android.util.Log
```

`Log` 클래스는 Logcat(메시지를 기록하는 콘솔)에 메시지 작성. 

`Log.d()` 로그에 명시적으로 전송하는 메시지를 비롯하여 앱에 관한 Android의 메시지가 표시됨.

로그 메시지의 우선순위)

`Log.d()` 디버그 메시지

`Log.i()`  정보메시지

`Log.e()`  오류 메시지 

`Log.w()`  경고 메시지

`Log.v()`   자세한 메시지

주기활동

`onCreate()`  앱을 만듬.

`onStart()`  활동을 시작하고 화면에 표시

`onResume()`  활동 포커스를 제공하고 사용자가 상호작용할 수 있도록 활동을 준비.

`onDestroy()`  실행이 완전 종료되었으며 가비지 컬렉션이 될 수 있음을 의미. 

                 이러한 리소스 삭제될 수 있음을 인식하고 메모리 정리를 시작

`onSaveInstanceState()` Activity가 소멸되면 필요할 수 있는 데이터를 저장하는 데 사용하는 콜백

                                 활동이 중지된 후 호출, 앱이 백그라운드로 전환될 때마다 호출