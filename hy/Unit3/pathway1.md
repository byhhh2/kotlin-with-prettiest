
## 컬렉션
### 리스트

<br><br><br>

### 집합 set
* 중복 불가능. 순서 정렬x
* 생성
    ```kotlin
    val set1 = setOf(1,2,3)             //변경 불가능
    val set2 = mutableSetOf(3,2,1)      //변경 가능

    val setOfNumbers = numbers.toSet()  //리스트를 집합으로 변경
    ```
* 메서드
    ```kotlin
    println("contains 7: ${ set1.contains(7) }")
    println("set1, set2 합집합 union : ${set1.union(set2)}")
    println("set1, set2 교집합 intersect : ${set1.intersect(set2)}")
    ```

<br><br><br>

### 맵 map
* key - value 쌍의 집합
* key는 고유하고 value에 매핑되지만 value는 중복 가능
* 생성 / 추가
    ```kotlin
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51
    println(peopleAges)         //{Fred=30, Ann=23, Barbara=42, Joe=51}
    ```

 
<br><br><br>


### 컬렉션 메서드
* `forEach{}` <br>
자동으로 모든 항목 탐색 후 항목별 작업 실행하는 메서드
    ```kotlin
    peopleAges.forEach { print("${it.key} is ${it.value}, ") }
    //Fred is 31, Ann is 23, Barbara is 42, Joe is 51,
    ```

* `map{}`<br>
컬렉션의 각 항목에 변환을 적용하여 새 컬렉션을 생성
    ```kotlin
    println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ") )
    //Fred is 31, Ann is 23, Barbara is 42, Joe is 51
    ```
* `filter{}` <br>
특정 조건과 일치하는 항목 탐색<br>
맵에 filter를 적용할 때 `LinkedHashMap`타입으로 반환됨
    ```kotlin
    val filteredNames = peopleAges.filter { it.key.length < 4 }
    println(filteredNames)      //{Ann=23, Joe=51}
    ```
* `sorted`


<br><br><br>

### 람다 lambda
: 함수이름 없이 표현식으로 함수 사용 가능
* 함수 유형 : 입력 매개변수와 반환값을 기반으로 특정 유형의 함수 정의 <br>
    ```kotlin
    val triple: (Int) -> Int = { a: Int -> a * 3 }
    val triple: (Int) -> Int = { it * 3 }       //약식
    ```
* `it` : 단일 매개변수의 암시적 이름
* OnClickListenr, OnKeyListener 등 리스너에서 축약으로 사용 가능 <br>
  ![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-collections/img/29760e0a3cac26a2.png)

<br><br><br>

### 고차함수
함수/람다를 다른 함수로 전달하거나 다른함수에서 함수를 반환
* forEach
* map
* filter
* sortedWith()  
    ```kotlin
    val peopleNames = listOf("Fred", "Ann", "Barbara", "Joe")
    peopleNames.sortedWith { str1: String, str2: String -> str1.length - str2.length }          //[Ann, Joe, Fred, Barbara]
    ```

<br><br><br>

## 인탠트 intent
: 실행할 작업을 나타내는 객체. 명령어 집합
* extra : 숫자, 문자 등 데이터(함수 호출 시 전달하는 인수와 비슷함)

### 암시적 인텐트 implicit intent
* 추상적이며 시스템에 작업 유형(링크열기, 이메일 작성, 전화 걸기 등)을 알려주고 시스템에서 작업처리방법 파악
* 설정
  1. [컴패니언 객체](#컴패니언-객체) 추가
  2. view의 context 참조를 가져옴
  3. 인텐트를 구현할 위치에 리스너 설정
  4. intent를 만들어 뷰(Intent.ACTION_VIEW)와 [URI](#uri) 전달
   > 인텐트 유형 <br>
      * CATEGORY_APP_MAPS - 지도 앱 실행 <br>
      * CATEGORY_APP_EMAIL - 이메일 앱 실행<br>
      * CATEGORY_APP_GALLERY - 갤러리(사진) 앱 실행<br>
      * ACTION_SET_ALARM - 백그라운드에서 알람 설정<br>
      * ACTION_DIAL - 전화 걸기<br>
  5. context 객체에서 `startActivity()`메서드 호출 intent 전달



<br><br><br>

### 명시적 인텐트 explicit intent
* 구체적이며 실행할 작업을 명확히 알 수 있고 자체 앱의 화면인 경우가 많음
* 설정
  1. 인텐트를 구현할 위치에 리스너 설정
  2. view의 context 참조를 가져옴
  3. intent를 만들어 context와 클래스 이름 전달
  4. intent의 putExtra 메서드 호출
  5. context 객체에서 `startActivity()`메서드 호출 intent 전달
   ```kotlin
    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val item = list.get(position)
        holder.button.text = item.toString()

        holder.button.setOnClickListener {
            val context = holder.view.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.LETTER, holder.button.text.toString())
            context.startActivity(intent)
        }
    }
    ```


<br><br><br>

### 컴패니언 객체  
다른 객체(클래스의 인스턴스)와 비슷
프로그램 중에는 컴패니언 객체의 인스턴스는 하나만 존재함(싱글톤 패턴)
```kotlin
companion object {
    const val LETTER ="letter"
    const val SEARCH_PREFIX ="https://www.google.com/search?q="
}
```


<br><br><br>

### URI 
* URI(Uniform Resource Identifier) : URL과 URN 포함
* URL(Uniform Resource Locator) : 웹페이지를 가리키는 문자열
* URN(Uniform Resource Name) : 전화번호 등



<br><br><br>

## Activity Lifecycle 활동 생명주기
* Android : 활동이 한 상태에서 다른 상태로 이동할 때 콜백 메서드을 호출
* 개발자 : 콜백 메서드를 재정의하여 수명 주기 상태 변경에 응답해 작업 실행
  ![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-activity-lifecycle/img/c259ab6beca0ca88.png)

<br><br>

### 콜백 메서드
* onCreate()
  - 모든 활동에서 구현해야함
  - 활동의 일회성 초기화 실행(ex : 레이아웃 확장, 클릭 리스너 정의, 뷰 결합 설정)
  - 활동이 초기화 된 직후 한 번 호출
  - onCreate()메서드 실행 시 활동이 생성됨으로 간주

* onStart()
  - onCreate()메서드 직후 호출
  - 실행시 활동이 화면에 표시
  - 여러 번 호출 가능
  - onStop()메서드와 페어링
   
* onResume()
  - 활동 포커스 제공
  - 사용자가 상호작용할 수 있도록 활동 준비
  - 다시 시작할 대상이 없어도 시작 시 호출됨
  
* onPause()
  - 앱에 포커스가 없을 경우 호출됨
    
* onStop()
  - 앱이 화면에 표시 되지 않을 경우 호출됨
  - 활동이 중지되었지만 객체는 백그라운드 메모리에 존재 

* onDestory()
  - 호출 시 활동이 완전히 종료되었으며 `Garbage Collection`될 수 있음을 의미 <br>
    Garbage Collection : 더이상 사용하지 않을 객체의 자동 정리
  - 앱에서 사용하는 리소스를 삭제하고 메모리를 정리함
  - onCreate()메서드처럼 단 한 번만 호출됨

* onRestart()
  - 활동이 백그라운드에서 포그라운드로 돌아올 경우 호출

* onSaveInstanceState()
  - 활동 소멸시 데이터(번들)를 저장. 저장된 번들은 onCreate(), onRestoreInstanceState에 전달됨<br>
    : onCreate()메서드는 활동이 다시 시작되면 저장되어있던 번들이 메서드에 전달되어 활동을 다시 생성하고 활동이 새로 시작되었다면 번들은 null 값을 가짐
  - 활동이 중지된 이후 호출
  - 앱이 백그라운드로 전환될 때마다 호출
  - onPause(), onStop() 이후 발생
  
   
* onRestoreInstanceState()
  - 활동이 다시 생성되는 경우 onStart()메서드 이후 호촐


<br><br><br>

### Log 클래스
: Logcat에 메세지 작성 > Log.[우선순위](로그 태그, "로그 메세지")
* 우선순위
  * Log.d() : 디버그 메세지
  * Log.i() : 정보 메세지
  * Log.e() : 오류 메세지
  * Log.w() : 경고 메세지
  * Log.v() : 자세한 메세지
* 로그 태그 : Logcat에서 메세지를 쉽게 찾을 수 있는 문자열. 보통 클래스 이름
* 로그 메세지 : 짧은 문자열



<br><br><br>

### 컴파일 시간 상수
* 값이 컴파일 시간에 결정되는 상수 (<-> `val` : 런타임에 할당)
* `const val`로 표현. 변수명은 '대문자'와 '_' 사용
* 다음 조건 만족
  1. 최상위 속성(클래스 생성자에 할당 불가능) or 함수/클래스 내부에서 사용 시 `companion object`선언
  2. String 등 기본 자료형으로만 선언 가능
  3. 사용자 지정 getter 불가능


<br><br><br>

### Bundle 번들
* key(문자열) - value(int, boolean 등) 쌍 모음
* 시스템은 번들을 메모리에 유지함
* 메서드 : `putInt()`, `putFloat()`, `putString()` 등 







 
<br><br><br>
<hr>
<br>

### 컬렉션, 람다, 고차함수
```kotlin
fun main() {

	val numbers = listOf(0, 3, 8, 4, 0, 5, 5, 8, 9, 2)
    println("list:   ${numbers}")

    
    // 집합
    val setOfNumbers = numbers.toSet()		//리스트를 집합으로 변경
	println("set:    ${setOfNumbers}")
    println("Set Of Numbers contains 7: ${setOfNumbers.contains(7)}")
    
    val set1 = setOf(1,2,3)
	val set2 = mutableSetOf(3,2,1)
	val set3 = mutableSetOf(4,3,2,1)

    println("set1 $set1 == set2 $set2 : ${set1 == set2}")
    println("set1, set3 합집합 union : ${set1.union(set3)}")
    println("set1, set3 교집합 intersect : ${set1.intersect(set3)}")
    
    
	// 맵
    val peopleAges = mutableMapOf<String, Int>(
        "Fred" to 30,
        "Ann" to 23
    )
    println(peopleAges)		//{Fred=30, Ann=23}
    
    // 맵에 항목 추가
    peopleAges.put("Barbara", 42)
    peopleAges["Joe"] = 51
    peopleAges["Fred"] = 100
    println(peopleAges)		//{Fred=100, Ann=23, Barbara=42, Joe=51}
    
    // 컬렉션 메서드
    print("------------------------------")
    print("\nforEach : ")
    peopleAges.forEach { print("${it.key} is ${it.value}, ") }
    
    print("\nmap : ")
    println(peopleAges.map { "${it.key} is ${it.value}" }.joinToString(", ") )
	println(numbers.map { it * 3 })
    
    print("filter : ")
    println(peopleAges.filter { it.key.length < 4 })
    
    
    // 람다 (매개변수1, 매개변수2..) -> 반환값
    val triple1: (Int) -> Int = { a: Int -> a * 3 }
    println(triple1(5))
    val triple2: (Int) -> Int = { it * 3 }
    println(triple2(5))
}
```
<br><br><br>


### 단어 목록 만들기
```kotlin
fun main() {
    val words = listOf("about", "acute", "awesome", "balloon", "best", "brief", "class", "coffee", "creative")

    val filteredWords = words.filter{it.startsWith("b", ignoreCase=true)}
        .shuffled()
        .take(2)
        .sorted()
    println(filteredWords)

    val filteredWords2 = words.filter { it.startsWith("c", ignoreCase = true) }
        .shuffled()
        .take(1)
        .sorted()
    println(filteredWords2)

}
```


<br><br><br>
<hr>
<br>


### <퀴즈>

* extra<br>
a. intent를 시작할 때 activity 사이 전달되는 데이터 조각 <br>


* onCreate() 메서드의 intent가 null 인 경우 결과 <br>
    ```kotlin
    val message = intent.extras?.getString("message").toString()
    ```
    a. null 객체의 extras 속성에 접근하려했기 때문에 충돌 발생 <br>

