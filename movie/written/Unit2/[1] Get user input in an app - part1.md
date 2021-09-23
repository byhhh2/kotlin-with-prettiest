## Get user input in an app : part1

<br>

<span style="font-size:25px;background-color:#D9ECE4;font-weight:bold;">Class</span>

### with

- 특정 인스턴스 객체로 다음 작업을 모두 실행

```kotlin
with (instanceName) {
    // all operations to do with instanceName
}
```

<br>

### open

- kotlin에서는 클래스는 최종 클래스이기 때문에 서브 클래스로 분류 할 수 없다.
- 해당 클래스를 상속하기 위해서는 `abstract`나 `open`키워드로 표시해줘야한다.

<br>

### 슈퍼 클래스 - 서브 클래스

- Dwelling : abstract class
- SquareCabin, RoundHut(super)
- RoundTower(sub)

<br>

![image](https://user-images.githubusercontent.com/52737532/134334298-ba8ba40a-a5f9-466e-8b1b-f5115ddf89f0.png)

<br>

```kotlin
import kotlin.math.PI //원형 넓이를 구하기 위한 수학 라이브러리
import kotlin.math.sqrt

fun main() {
    val squareCabin = SquareCabin(6, 50.0)

    with(squareCabin) { //with을 사용해서 같은 인스턴스에서 작업하도록
        println("\nSquare Cabin\n============")
        println("Capacity: ${capacity}")
        println("Material: ${buildingMaterial}")
        println("Has room? ${hasRoom()}")
        println("Floor area: ${floorArea()}")
	}

    val roundHut = RoundHut(3, 10.0)

    with(roundHut) {
        println("\nRound Hut\n=========")
        //println("Material: ${buildingMaterial}")
        //println("Capacity: ${capacity}")
        //println("Has room? ${hasRoom()}")
        //println("Floor area: %.2f".format(floorArea()))
        //소수점 이하 두자리 포맷팅
        println("Has room? ${hasRoom()}")
        getRoom()
        println("Has room? ${hasRoom()}")
        getRoom()
	}

     val roundTower = RoundTower(4, 15.5)

    with(roundTower) {
        println("\nRound Tower\n==========")
        println("Material: ${buildingMaterial}")
        println("Capacity: ${capacity}")
        println("Has room? ${hasRoom()}")
        println("Floor area: %.2f".format(floorArea()))
        println("Carpet size: ${calculateMaxCarpetSize()}")
	}
}

//생성자로 전달되는 매개변수 residents
abstract class Dwelling(private var residents: Int) { //주택공통정보
    abstract val buildingMaterial: String //추상 속성
    abstract val capacity: Int
    abstract fun floorArea(): Double //추상 메서드

    fun hasRoom(): Boolean {//또 다른 거주자를 위한 공간이 있는지
        return residents < capacity
    }

    fun getRoom() {
        if (capacity > residents) {
            residents++
            println("You got a room!")
        } else {
            println("Sorry, at capacity and no rooms left.")
        }
	}
} //추상 클래스

//슈퍼 클래스의 필수 매개변수 residents 전달
//가변적임을 위해 residents를 SquareCabin 정의의 매개변수로 만듦
//건물의 길이 값인 length
class SquareCabin(residents: Int, val length: Double) : Dwelling (residents){//Dwelling의 서브클래스
    //override를 통해 상위 클래스의 추상 속성을 재정의
    override val buildingMaterial = "Wood"
    override val capacity = 6

    override fun floorArea(): Double {
		return length * length
	}
}

open class RoundHut(residents: Int, val radius: Double) : Dwelling(residents) {
    override val buildingMaterial = "Straw"
    override val capacity = 4

    override fun floorArea(): Double {
		return PI * radius * radius
	}

    fun calculateMaxCarpetSize(): Double {
        //카펫이 직사각형인데 원형집에 깔 때 최대 크기
        val diameter = 2 * radius
        return sqrt(diameter * diameter / 2)
	}
}

//floors에 기본값 2 할당
class RoundTower(residents: Int, radius: Double, var floors: Int = 2) : RoundHut(residents, radius) {
    override val buildingMaterial = "Stone"
    override val capacity = 4 * floors

    override fun floorArea(): Double {
    	return super.floorArea() * floors
        //super 키워드를 통해 상위 클래스의 floorArea()를 실행하고
        //그 return값을 사용하므로써 중복코드 방지
	}
}
```

<br>

<span style="font-size:25px;background-color:#D9ECE4;font-weight:bold;">XML</span>

### XML 레이아웃

- ViewGroup - ConstraintLayout - 기타 Views

- `xmlns:` : XML의 네임스페이스
- `android:` : Android 시스템에서 정의한 속성 네임스페이스

<br>

### 주석

- `<!-- comment -->`

<br>

### `EditText` XML로 추가하기

```xml
    <EditText
        android:id="@+id/cost_of_service"
        android:layout_height="wrap_content"
        android:layout_width="160dp"
        android:inputType="numberDecimal"
        android:hint="Cost of Service"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

- `id` 속성

  - 고유한 리소스 이름
  - 새로운 뷰 ID는 `@+id` 접두사로 정의
  - 이름은 모두 소문자, 여러 단어 밑줄 구분
  - 참조는 `R.<type>.<name>` (예: `R.id.button`)

- `layout_height` 속성
  - `wrap_content` : 높이가 내부 콘텐츠의 높이와 같다
- `layout_width` 속성

  - `match_parent` : `ConstraintLayout`의 하위 요소에서는 설정 불가
  - 대신 `width`를 `0p`로 설정하면 `match_parent`과 일치 제약 조건 (`start`랑 `end` 제한해야함)

  ```xml
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
  ```

- `layout_constraint<Source>_to<Target>Of` 속성
  - `<Source>` : 현재 뷰
  - `<Target>` : 현재 뷰가 제한되는 타겟 뷰(상위 컨테이너 또는 다른 뷰)의 가장자리

<br>

> px : 화면을 구성하는 최소 단위  
> dpi : 1인치에 들어가는 px을 나타내는 단위  
> dp : 픽셀 독립 단위 (dp = px \* 160 / dpi)  
> <br>
> dp를 사용하면 비율에 맞게 레이아웃 구성 가능

<br>

- `inputType` : 입력란에 사용자가 입력할 값의 타입을 뭘로 할지

- `hint` : 사용자가 입력해야하는 내용을 설명

<br>

### `TextView`를 `EditText` 바로 아래에 위치 시키기

<br>

```xml
    <TextView
        android:id="@+id/service_question"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="How was the service?"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/cost_of_service"
        />
```

- `app:layout_constraintTop_toBottomOf="@id/cost_of_service"` 를 사용하여 요소 바로 밑에 위치 시키기

<br>

### `RadioButton`

<br>

```xml
<RadioGroup>
    <RadioButton />
    <RadioButton />
    ..
</RadioGroup>

```

- `android:checkedButton="<id>"` : 기본 옵션 지정

<br>

### `Switch`

- toggle switch

<br>

<span style="font-size:25px;background-color:#D9ECE4;font-weight:bold;">View binding</span>

- 번거롭게 `findViewById()` 메서드를 사용해서 뷰에 대한 참조를 반환하는 대신 편의를 위한 뷰 결합

1.  `Gradle Scripts > build.gradle (Module)`

```
    android {
        //..

        buildFeatures {
            viewBinding = true
        }
    }
```

2.  `Sync`
3.  `MainActivity.kt` 설정

```kotlin
        class MainActivity : AppCompatActivity() {

            lateinit var binding: ActivityMainBinding
            //lateinit : 코드가 변수를 사용하기 전에 먼저 초기화

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityMainBinding.inflate(layoutInflater)
                //binding 객체 초기화
                setContentView(binding.root)
                //루트는 모든 뷰에 연결되어 있음
            }

        }
```

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

- 언더바는 삭제되고, 언더바 다음 글자가 대문자로 바뀐다.

<br>

## 디버깅

- run app 말고 디버깅을 한다.
- 하단의 Logcat에서 log를 확인할 수 있다.

<br>

### `toDouble()`에서의 `null`처리

- 비어 있는 문자열이거나 십진수를 나타내지 않는 문자열이라면 작동하지 않는다
- `toDoubleOrNull()` : 가능한 경우 십진수 반환, 문제가 있으면 `null` 반환

<br>

### 코드 검사

1. `MainActivity.kt`를 연채로 `Analyze > inspect Code ..`
2. File.. > `OK`
3. `inspection Result`
4. double 클릭하면 어디 문제인지 알 수 있음
5. 조명 눌러서 고쳐주기
