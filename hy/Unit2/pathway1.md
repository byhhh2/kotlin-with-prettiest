
### var  val 차이
`val` (value)
 * 불변타입변수.
 *  초기 값 할당 이후 변경 불가능. 초기화 가능
 * 초기화 시 값을 할당하지 않으면 반드시 타입 지정
 * 변수의 참조가 가리키는 객체의 내부 값은 변경 가능

> 초기화 예시
```kotlin
fun main(args: Array<String>){
    val myArray = arrayListOf("java") 
    myArray.add("python") 				//참조가 가르키는 객체 내부 변경 가능
    println("myArray : "$myArray")  	// 출력 : myArray : [java, python] 
}
```
<br>

`var` (variable)
* 가변타입변수. 초기화 후 같은 타입으로 값 변경 가능
* 다른 타입으로 변경 시 형변환 필요
* 필요시에만 사용
<br><br><br>


### 클래스 / 상속
* Child / subclass : 다른 클래스 아래 있는 모든 클래스 
* Parent / superclass / base class : 하위 클래스가 1개 이상 있는 모든 클래스 
* Root / top-level class : 계층 구조의 최상위 클래스 
* Inheritance : 하위 클래스가 상위 클래스의 모든 속성, 메서드를 포함하거나 상속받는 경우
 						abstract 클래스 또는 open키워드로 표시된 클래스만 상속가능 
* abstract 추상클래스 : 완전히 구현되지 않아 인스턴스화할 수 없는 클래스

- kotlin.Any : kotlin의 모든 클래스의 공통 슈퍼클래스 
- `override` 키워드 : 서브 클래스에서 속성과 함수를 재정의

하위 클래스 호출 시 constructor(생성자)가 호출되어 객체 인스턴스를 초기화함. 
: constructor는 전달된 인수 등 클래스의 모든 정보에서 인스턴스를 빌드함. 클래스가 상위 클래스에서 속성과 함수를 상속받을 때 constructor는 상위 클래스의 constructor를 호출하여 객체 인스턴스 초기화를 완료함.
<br><br><br>

### `with` 문
동일한 객체 인스턴스에서 여러 호출을 실행하여 코드를 단순화함
```kotlin
println("Capacity : ${squareCabin.capacity}")
with(squareCabin) {
	    println("squareCabin")
}
```

<br><br><br>

### 소수점 이하 자릿수 변경
```kotlin
println("Floor area: %.2f".format(floorArea()))
```

<br><br><br>


## xml
### ConstraintLayout 태그 
* androidx.constraintlayout.widget.ConstraintLayout으로 표시<br>
* 핵심 Android 플랫폼 외에도 추가 기능을 제공하는 코드 라이브러리가 포함된 Android Jetpack의 일부이기 때문<br>
* Jetpack에는 앱을 더 쉽게 빌드하는 데 활용할 수 있는 유용한 기능 포함

<br><br><br>

### xmlns 
: XML 네임스페이스를 나타내고 각 줄은 스키마나 속성의 어휘를 정의함<br>
ex ) `android` 네임스페이스는 Android 시스템에서 정의한 속성을 표시

<br><br><br>

### 주석
< !-- 어쩌구저쩌구 -->

<br><br><br>


### 속성
1. 리소스 id <br>
   * 모두 소문자. 여러 단어는 밑줄 `_` 로 구분<br>
   * 앱 코드에서 리소스 id를 참조할 때 `R.<type>.<name>` (ex : R.string.roll / R.id.button)
   * xml에서 추가시 `@+id/<id>` (ex : @+id/cost_of_service)
   * xml에서 사용시 `@id/id` (ex : @id/cost_of_service)

2. layout_height , layout_width
   - wrap_content : 높이/너비가 내부 콘텐츠의 높이/너비와 같다는 의미. ConstraintLayout의 하위 요소에서는 match_parent를 설정할 수 없음
   - 160dp : 고정 높이/너비
   - 너비 0dp : 레이아웃 시작과 끝을 상위 요소(parent)의 시작과 끝으로 제한하고 0으로 설정하면 constraintLayout의 너비와 같아짐
3. inputType
   - text : 모든 텍스트 문자(알파벳, 기호 등)
   - number : 숫자
   - numberDecimal : 소수점이 있는 숫자로 제한
4. hint <br>
사용자가 입력란에 입력해야 하는 내용 설명
5. 레이아웃제약조건 `layout_constraint<Source>_to<Target>Of` <br>
	&#60;Source	&#62; : 현재 뷰 <br>
	&#60;Target&#62; : 현재 뷰가 제한되는 타겟 뷰(상위 컨테이너 or 다른 뷰)<br>
    (ex : app:layout_constraintTop_toBottomOf = "@id/cost_of_service" )


<br><br><br>

### 라디오그룹 - 라디오버튼

<br><br><br>

### 뷰 결합 사용  설정 View binding
앱에 뷰가 추가되고 UI가 복잡해지며 findViewById()보다 뷰에 대한 참조를 편리하게 해줌
1. Gradle Scripts > build.gradle (Module: Tip_Time.app) > android 섹션에 다음 코드 추가
``` kotlin
buildFeatures {
    viewBinding = true
}
```
2. 'Gradle files have changed since last project sync.'라는 메시지 확인 >  '
Sync Now' 클릭

<br><br><br>
### 결합 객체 초기화
![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-tip-calculator/img/a3f060e1765e049a.png)

- 앱의 각 View마다 findViewById()를 호출하는 대신, 결합 객체를 한 번 만들고 초기화
- MainActivity.kt의 MainActivity 클래스를 다음 코드로 변경
```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding                   //1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)   //2
        setContentView(binding.root)                            //3
    }
}
```
1. lateinit var binding: ActivityMainBinding <br>
클래스에서 결합 객체의 최상위 변수를 선언<br>
`lateinit` 키워드 : 코드가 변수를 사용하기 전 먼저 초기화할 것임을 확인. 변수를 초기화하지 않으면 앱 비정상 종료 <br>
2. binding = ActivityMainBinding.inflate(layoutInflater) <br>
activity_main.xml 레이아웃에서 Views에 액세스하는 데 사용할 binding 객체를 초기화 <br>
3. setContentView(binding.root) <Br>
콘텐츠 뷰 설정. 이아웃의 리소스 ID인 `R.layout.activity_main`을 전달하는 대신, 앱의 뷰 계층 구조 루트인 `binding.root`를 지정

<br><br><br>

### `binding` 객체 
: ID가 있는 앱의 모든 View를 위한 참조를 자동으로 정의
- View를 위한 참조를 유지할 변수를 만들 필요조차 없으며 결합 객체에서 직접 뷰 참조를 사용하기만 하면 됨
- 결합 클래스의 이름은 XML 파일의 이름을 카멜 표기법으로 변환하고 이름 끝에 'Binding'을 추가하여 생성됨
- 각 뷰를 위한 참조는 밑줄을 삭제하고 뷰 이름을 카멜 표기법으로 변환하여 생성됨
> activity_main.xml -> ActivityMainBinding <br>
@id/text_view -> binding.textView

```kotlin
// Old way with findViewById()
val myButton: Button = findViewById(R.id.my_button)
myButton.text = "A button"

// Better way with view binding
val myButton: Button = binding.myButton
myButton.text = "A button"

// Best way with view binding and no extra variable
binding.myButton.text = "A button"
```

<br><br><br>

### chaining 체이닝
객체에 입력한 텍스트를 가져옴
```kotlin
var stringInTextField = binding.costOfService.text
```

<br><br><br>

### 디버깅
1. Run > Debug 'app'
2. 하단의 `logcat` 버튼 or View > Tool Windows > Logcat 에러 확인

<br><br><br>

### 코드검사
1. MainActivity.kt를 연 채로 Analyze > Inspect Code... 선택 > Specify Inspection Scope 대화상자 표시
2. File로 시작하는 옵션 선택 > ok 클릭
3. 다음 두 가지 모두 확인
   1. 하단의 Inspection Results 창 > Kotlin > Style issues > Class member can have ‘private' visibility 메세지 > Property ‘binding' could be private 메시지 > 오른쪽 Make ‘binding' ‘private' 버튼 클릭
   2. 하단의 Inspection Results 창 > Kotlin > Style issues > Variable declaration could be inlined 메시지 > 오른쪽 Inline variable 버튼 클릭
 

<br><br><br>
<hr>
<br>

### 주택 클래스 계층 구조
 
```kotlin
import kotlin.math.PI
import kotlin.math.sqrt 

fun main() {
	//val dwelling = Dwelling()
    val squareCabin = SquareCabin(6, 50.0)
    println("squareCabin")
    println("Capacity : ${squareCabin.capacity}")
    println("Material : ${squareCabin.buildingMaterial}")
    println("Has room? : ${squareCabin.hasRoom()}")
    println("Floor Area : ${squareCabin.floorArea()}\n")
    
    with(squareCabin) {
	    println("squareCabin")
        println("Capacity : ${capacity}")
        println("Material : ${buildingMaterial}")
    	println("Has room? : ${hasRoom()}")   
    	println("Floor Area : ${floorArea()}\n")
    }
    
    val roundHut = RoundHut(3, 10.0)
    with(roundHut) {
    	println("roundHut")
        println("Capacity : ${capacity}")
        println("Material : ${buildingMaterial}")
    	println("Has room? : ${hasRoom()}")   
        getRoom()
    	println("Has room? : ${hasRoom()}")   
        getRoom()
        println("Carpet Size : ${calculateMaxCarpetSize()}")
    	println("Floor Area : ${floorArea()}\n")
    }
    
 	val roundTower = RoundTower(4, 15.5)
//  	val roundTower = RoundTower(4,2)
    with(roundTower) {
    	println("roundTower")
    	println("Capacity: ${capacity}")
    	println("Material: ${buildingMaterial}")
    	println("Has room? ${hasRoom()}")
        println("Carpet Size : ${calculateMaxCarpetSize()}")
    	println("Floor Area : %.2f".format(floorArea()))
    	println("Floor Area : ${floorArea()}\n")
	}
}

abstract class Dwelling(private var residents: Int){
    abstract val buildingMaterial : String 
    abstract val capacity : Int
    
    fun hasRoom() : Boolean {
        return residents < capacity
    }
	
    abstract fun floorArea() : Double
	
    fun getRoom() {
        if (capacity > residents ){
            residents++
            println("You Got a ROOM!")
        }
        else {
            println("Sorry")
        }
    }
}

class SquareCabin(residents: Int, val length :Double ) : Dwelling(residents) {
    override val buildingMaterial = "wood"
    override val capacity = 6
    
    override fun floorArea() :Double {
        return length * length
    }
}

open class RoundHut(residents : Int, val radius : Double) : Dwelling(residents){
    override val buildingMaterial = "Straw"
    override val capacity = 4
    
    override fun floorArea() :Double {
        return PI * radius * radius
    }
    
    fun calculateMaxCarpetSize() : Double {
        val diameter = 2 * radius  //지름
    	return sqrt(diameter * diameter / 2)
    }   
}

class RoundTower(residents : Int, radius : Double, val floors : Int = 2) : RoundHut(residents, radius){
// class RoundTower(residents : Int, val floors : Int) : RoundHut(residents){
    override val buildingMaterial = "Stone"
    override val capacity = 4 * floors
    override fun floorArea() : Double{
//         return PI * radius * radius * floors
        return super.floorArea() * floors
    }
}
```




