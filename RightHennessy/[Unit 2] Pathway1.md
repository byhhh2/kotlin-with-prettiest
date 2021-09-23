# [Unit 2] Pathway1

## 클래스 계층 구조

: 클래스가 상위 요소와 하위 요소의 계층 구조로 구성된 배열

parent / superclass (상위 클래스) : 하위 클래스가 하나 이상 있는 모든 클래스

child / subclass (하위 클래스) : 계층 구조에서 다른 클래스 아래에 있는 모든 클래스

root (최상위 클래스) : 클래스 계층 구조의 최상위에 있는 클래스

상속 : 하위 클래스가 상위 클래스의 모든 속성과 메서드를 포함하거나 상속받는 경우

## Android 클래스의 상속

![Untitled](%5BUnit%202%5D%20Pathway1%2014ed030caaa74630aa5388e8bdfb2c95/Untitled.png)

## 주택 클래스 계층 만들기

- 추상클래스 만들기

인스턴스 : 객체 지향 프로그래밍에서 해당 클래스의 구조로 컴퓨터 저장공간에서 할당된 실체

추상클래스 : 완전히 구현되지 않아서 인스턴스화할 수 없는 클래스 (like 스케치)

 → 속성값과 함수 구현을 알 수 없으면 클래스를 추상으로 만듬.

- 서브클래스 만들기

클래스 헤더에 class SquareCabin (residents : Int) ...

실제로는 class SquareCabin construct (residents : Int) ...

`constructor`  클래스에서 객체 인스턴스를 만들 때 호출

전달된 인수를 비롯하여 클래스의 모든 정보에서 인스턴스를 빌드. 클래스가 상위 클래스에서 상속 받을때, 상위 클래스의 construct를 호출하여 객체 인스턴트 초기화를 완료함.

```kotlin
abstract class Dwelling(private var residents : Int){
	abstract val buildingMaterial : String
	abstract val capacity : Int
	
	fun hasRoom() : Boolean {
		return residents < capatity
	}

	abstract fun floorArea() : Double

	fun getRoom(){
		if(capacity > residents){
			residents++
			println("You gota room!")
		}else{
			println("Sorry, at capacity and no rooms left.")
		}
}

// residents val로 선언 금지 : 이미 상위 클래스 Dwelling에서 선언했기 때문
class SquareCabin(residents : Int, val length : Double) : Dwelling(residents) {
	override val buildingMaterial = "Wood"
	override val capacity = 6

	override fun floorArea() : Double{
		return length*length
	}
}
	
// RoundTower가 이 함수를 상속받기 때문에 open으로 설정
open class RoundHut(residents : Int, val radius : Double) : Dwelling(residents){
	override val buildMaterial = "Straw"
	override val capacity = 4

	override fun floorArea() : Double{
		return PI*radius*radius
	}

	fun calculateMaxCarpetSize() : Double{
		val diameter = 2*radius
		return sqrt(diameter*diameter/2)
	}
}
	
class RoundTower(
	residents : Int, radius : Double, 
	val floors : Int = 2) : RoundHut(residents){
	override val buldMaterial = "Stone"
	override val capacity = 4* floors

	override fun floorArea() : Double {
		return super.floorArea()*floors
	}
}
```

`private`  공개 상태 수정자, 속성이 이 클래스에만 표시되고 이 클래스 내부에서만 사용가능

`override`  이 속성이 상위 클래스에서 정의되었고, 이 클래스에서 재정의 될 것임을 의미

`super`  상위 클래스의 함수와 속성을 참조

`open`  서브클래스로 분류 가능하도록 클래스를 open으로 정의

참고) 기본적으로 kotlin에서 클래스는 최종클래스이므로 abstract 나 open 키워드로 표시된 클래스에서만 상속가능

```kotlin
import kotlin.math.PI
import kotlin.math.sqrt

fun main() {
    val squareCabin = SquareCabin(6, 50.0)
		val roundHut = RoundHut(3, 10.0)
		val roundTower = RoundTower(4, 15.5)

		with(squareCabin){
		  println("\nSquare Cabin\n============")
		  println("Capacity: ${capacity}")
	    println("Material: ${buildingMaterial}")
	    println("Has room? ${hasRoom()}")
			println("Floor area: ${floorArea()}")
	}

	with(roundHut){
		  println("\nRound Hut\n============")
		  println("Capacity: ${capacity}")
	    println("Material: ${buildingMaterial}")
	    println("Has room? ${hasRoom()}")
			getRoom()
			println("Has room? ${hasRoom()}")
			getRoom()
			println("Floor area: ${floorArea()}")
			println("Carpet size: ${calculateMaxCarpetSize()}")
	}

	with(roundTower) {
      println("\nRound Tower\n==========")
      println("Material: ${buildingMaterial}")
      println("Capacity: ${capacity}")
      println("Has room? ${hasRoom()}")
      println("Floor area: ${floorArea()}")
			println("Carpet size: ${calculateMaxCarpetSize()}")
   }
}
```

`with`  이 인스턴스 객체로 다음 작업을 모두 실행.

ex) println("Capacity: ${squareCabin.capacity}") → println("Capacity: ${capacity}")

## Android용 XML 레이아웃 만들기

- XML

 : 확장성 마크업 언어 (eXtensible Markup Language), 텍스트 기반 문서를 사용하여 데이터를 설명

`xmlns`  네임스페이스, 각 줄은 스키마나 이러한 단어와 관련된 속성의 어휘를 정의

`android`  Android 시스템에서 정의한 속성을 표시

```kotlin
<EditText
    android:id="@+id/cost_of_service"
    android:layout_height="wrap_content"
    android:layout_width="160dp"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    android:inputType="numberDemical"
		android:hint="Cost of Servie"/>

<TextView
    android:id="@+id/service_question"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintStart_toStartOf="parent"
	  app:layout_constraintTop_toBottomOf="@id/cost_of_service"  // EditText 기준
    android:text="How was the service?"/>
```

`wrap_content`  height 기본설정, 높이가 내부 콘텐츠의 높이와 같음을 의미

`match_parent`  width 기본설정, ConstraintLayout의 하위요소에서는 설정 불가

`numberDemical` 소수점이 있는 숫자로 제한

- 뷰가 제한되지 않음 오류

 : ConstraintLayout의 하위요소에는 레이아웃이 정렬방법을 알 수 있도록 제약 조건이 필요

layout_constraint<Source>_to<Target>Of 

<Source> : 현재 뷰

<Target> : 현재 뷰가 제한되는 타겟 뷰(상위 컨테이너 또는 다른 뷰)의 가장자리

+) start' 및 'end' 제약 조건을 사용 → 왼에서 오른(LTR), 오른에서 왼(RTL) 방향 언어 모두 처리 가능

## Radio Button을 이용한 선택지

```kotlin
<RadioGroup
    android:id="@+id/tip_options"
		android:checkedButton="@id/option_twenty_percent"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@id/service_question">

    <RadioButton
        android:id="@+id/option_twenty_percent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Amaizing (20%)" />

    <RadioButton
        android:id="@+id/option_eighteen_percent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Good (18%)" />

    <RadioButton
        android:id="@+id/option_fifteen_percent"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="OK (15%)" />
    </RadioGroup>
```

<RadioGroup></RadioGroup> 내부에 다른 요소가 존재하므로 맺음 괄호 사용하기 

`android:orientation`  vertical : 수직으로 버튼 나열,  horizontal : 연속된 버튼 나열

`checkedButton` 선택하길 바라는 라디오 버튼의 리소스 ID (기본 설정)

## Switch 생성

```kotlin
<Switch
    android:id="@+id/round_up_switch"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:text="Round tip up?"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@id/tip_options"
    android:checked="true"/>

<Button
    android:id="@+id/calculate_button"
    android:text="Calculate"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    app:layout_constraintTop_toBottomOf="@id/round_up_switch"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"/>

<TextView
    android:id="@+id/tip_result"
    android:text="Tip Amount"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintTop_toBottomOf="@id/calculate_button"/>
```

`switch`  두 옵션 간에 전환 가능

`android:checked`  스위치의 기본 사용 설정 true or false

View의 너비를 ConstraintLayout의 너비와 같게 하려면 

1. 시작과 끝을 상위 요소의 시작과 끝으로 제한
2. 너비를 0dp로 설정. → layout_width="0dp" : 일치 제약 조건을 의미 

- 문자열 추출 (하드 코딩 문자열 경고)

리소스 파일로 문자열을 추출하면 앱을 다른 언어로 번역하고 문자열을 재사용하기가 더 쉬워짐

strings.xml 에 문자열 리소스 생성

- 들여쓰기 정리

  코드 전체선택 후 Code >Reformat Code

## 뷰 결합

- Gradle

 : Android 스튜디오에서 사용하는 자동화된 빌드 시스템.

 개발자가 코드를 변경하거나 리소스를 추가하거나 그 외의 방식으로 앱을 변경할 때마다 Gradle이 변경된 사항을 파악하여 앱을 다시 빌드하는 데 필요한 조치를 취한다.

- 뷰 결합 사용 설정
1. Gradle Scripts > build.gradle (Module:Tip_Time.app)
2. android 섹션에 buildFeatures { viewBinding = true } 추가
3. 'Gradle files have changed since last project sync' 알림
4.  Sync Now

- 결합 객체 초기화

![Untitled](%5BUnit%202%5D%20Pathway1%2014ed030caaa74630aa5388e8bdfb2c95/Untitled%201.png)

앱의 각 View 마다 findViewById()를 호출하는 대신, 결합 객체를 한번 만들고 초기화

MainActivity.kt의 기본 코드를 다음으로 대체 

```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
```

`lateinit`  코드가 변수를 사용하기 전에 초기화 할 것임을 확인, 변수 미초기화시 앱이 비정상 종료

`binding`  id가 있는 앱의 모든 View를 위한 참조를 자동으로 정의

setContentView 함수를 이용해 R.layout.activity_main을 전달하는 대신, 앱의 뷰 계층 구조 루트인 binding.root를 지정

```kotlin
// findViewById()을 사용한 예전 방법
val myButton: Button = findViewById(R.id.my_button)
myButton.text = "A button"

// view binding 사용
val myButton: Button = binding.myButton
myButton.text = "A button"

// 최선 : view binding 사용, 변수 미사용
binding.myButton.text = "A button"
```

// Main Activity 클래스 내 존재

```kotlin
fun calculateTip() {
    val stringInTextField = binding.costOfService.text.toString()
    val cost = stringInTextField.toDouble()

    val selectedId = binding.tipOptions.checkedRadioButtonId
		val tipPercentage = when (selectedId) {
        R.id.option_twenty_percent-> 0.20
        R.id.option_eighteen_percent-> 0.18
        else -> 0.15
    }

    var tip = tipPercentage * cost
    val roundUp = binding.roundUpSwitch.isChecked
if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }

    val formattedTip = NumberFormat.getCurrencyInstance().format(tip)

    binding.tipResult.text= getString(R.string.tip_amount, formattedTip)
}
```

`.text` 첫 번째 부분인 binding.costOfService는 서비스 비용의 UI 요소를 참조.

→ 끝부분에 .text를 추가하여 EditText 객체를 얻은 후 이 객체에서 text 속성을 불러옴 (체이닝)

`toDouble()`  텍스트를 십진수로 변환, String에서 호출

`toString()`  EditText의 text 속성은 변경할 수 있는 텍스트를 나타내기 때문에 Editable.

→ toString()을 호출하여 Editable을 String으로 변환

`ceil()`  위로만 반올림하거나 상한(ceiling)

`getCurrencyInstance().format()`  사용자가 선택한 언어 및 기타 설정에 따라 시스템이 자동으로 통화 형식을 지정, 숫자 형식 지정 클래스가 제공.

## 버그 처리

: Logcat을 사용하여 앱 비정상 종료와 같은 문제 해결 가능

- 아무 값도 입력하지 않았을 때 강제 종료

MainActivity.kt 내의 calculate.Tip() 함수

```kotlin
//val cost = stringInTextField.toDouble()에서 toDouble()대신 toDoubleOrNull()
val cost = stringInTextField.toDoubleOrNull()
if (cost == null) {
    return
}
```

`toDoubleOrNull()`  string이면 double로 변환 / null이면 null을 반환

- 첫번째 팁 계산 후 아무 값도 입력하지 않고 계산 했을 때 이전 계산 값이 남아 있는 경우

```kotlin
val cost = stringInTextField.toDoubleOrNull()
if (cost == null) {
		binding.tipResult.text = ""
    return
}
```

## 깔끔한 코드 만들기

해당 파일로 들어가 코드 검사를 실시

Analyze > Inspect Code...

대화상자가 뜨면 file로 시작하는 옵션 선택 > OK

Inspection Results 창이 하단에 표시 > 메시지 클릭하여 코드 정리

나머지 불필요한 변수 정리