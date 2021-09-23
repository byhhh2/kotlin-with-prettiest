# Pathway1

### **Android 클래스의 상속**

![https://developer.android.com/codelabs/basic-android-kotlin-training-classes-and-inheritance/img/2fbc991a6afe5299.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-classes-and-inheritance/img/2fbc991a6afe5299.png?hl=ko)

- **Parent / SuperClass**

    > `View` 클래스

    화면의 직사각형 영역을 나타냄

    그리기와 이벤트 처리 담당

    - **Child** **/** `**View` 의 SubClass / `EditText` 와 `Button` 의 SuperClass**

        `View` 클래스의 모든 속성과 기능을 상속 받음

        고유한 특정 로직을 추가함

        > `TextView` 클래스

        사용자에게 텍스트를 표시하는 특정 로직 추가

        - **Children / `TextView` 의 SubClass**

            `TextView` 의 하위 클래스

            `View` 및 `TextView` 클래스의 모든 로직을 복사

            > `Edit Text` 클래스

            화면에서 텍스트를 수정할 수 있는 자체 기능
            `Button` 클래스

### 주택 클래스 계층 만들기

![https://developer.android.com/codelabs/basic-android-kotlin-training-classes-and-inheritance/img/ceeee5c347df33dd.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-classes-and-inheritance/img/ceeee5c347df33dd.png?hl=ko)

- 추상 클래스 만들기 : `Dwelling`
    - 추상 클래스 생성시 클래스 앞에 **abstract** 붙이기
    - 추상 메서드 생성시 마찬가지로 **abstract** 붙이기

    ```kotlin
    abstract class Dwelling(private var residents: Int) {

       abstract val buildingMaterial: String
       abstract val capacity: Int

    	 // 거주자가 있는지 확인하는 함수
       fun hasRoom(): Boolean {
           return residents < capacity
       }

    	 // 추상 메서드 : 서브 클래스에서 구현
    	 abstract fun floorArea(): Double
    }

    val dwelling = Dwelling() // 이 줄의 경우 에러 발생 //추상 클래스의 인스턴스를 만들 수 없음
    ```

- 서브 클래스 만들기 : `SquareBin`
    - 서브 클래스 생성시 부모 클래스와의 연관 표시 : `:`
    - 상위 클래스의 필수 매개변수들을 서브 클래스 정의의 매개변수로 만들 수 있다.
    - 상위 클래스의 속성 앞에 **override** 표시

    ```kotlin
    class SquareCabin(residents: Int, val length: Double) : Dwelling(residents) { // :로 상속 표시 //residents를 다시 매개변수로 만듦
        override val buildingMaterial = "Wood"
        override val capacity = 6
    		override fun floorArea(): Double {
    		    return length * length
    		}
    }
    ```

    **with**

    인스턴스 객체를 생략할 수 있음 (squareCabin. 을 생략함)

    ```kotlin
    with(squareCabin) {
        println("\nSquare Cabin\n============")
        println("Capacity: ${capacity}")
        println("Material: ${buildingMaterial}")
        println("Has room? ${hasRoom()}")
    }

    		//with가 없다면
    		println("\nSquare Cabin\n============")
        println("Capacity: ${squareCabin.capacity}")
        println("Material: ${squareCabin.buildingMaterial}")
        println("Has room? ${squareCabin.hasRoom()}")
    ```

- 서브 클래스 만들기 : `RoundHut`

    ```kotlin
    import kotlin.math.PI
    class RoundHut(residents: Int, val radius: Double) : Dwelling(residents) {
        override val buildingMaterial = "Straw"
        override val capacity = 4
    		override fun floorArea(): Double {
    		    return PI * radius * radius
    				// return kotlin.math.PI * radius * radius // PI import하지 않을 경우 
    		}
    }
    ```

- `Roundhut` 의 서브 클래스 만들기 : `RoundTower`

    상위 클래스를 상속받기 위해선 상위 클래스에 **abstract** 또는 **open** 키워드가 있어야한다.

    **super** 키워드를 사용하여 상위 클래스의 함수와 속성을 참조

    ```kotlin
    class RoundTower(residents: Int, radius: Double, val floors: Int = 2) : RoundHut(residents, radius) {
        override val buildingMaterial = "Stone"
        override val capacity = 4 * floors
    		override fun floorArea(): Double {
    		    return super.floorArea() * floors
    		}
    }

    // RoundHut 클래스 수정 // open 키워드 추가
    open class RoundHut(residents: Int) : Dwelling(residents) {
       override val buildingMaterial = "Straw"
       override val capacity = 4
    }
    ```

println("Floor area: ${floorArea()}")
**println("Floor area: %.2f".format(floorArea()))**
아래의 표현으로 소수점 둘째 자리까지 표현 가능

---

### **XML의 이해**

- xmlns

    XML 네임스페이스

    각 줄은 스키마나 이러한 단어와 관련된 속성의 어휘를 정의

    ```kotlin
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    ```

    - android : Android 시스템에서 정의한 속성
- 주석

    **<!--**로 시작하고 **-->**로 끝남

### XML로 레이아웃 빌드

- `ConstraintLayout`의 하위 요소에는 제약 조건 필요

    ```
    app:layout_constraintStart_toStartOf="parent" // 가로 제약 조건 : 상위 요소의 시작 가장자리
    app:layout_constraintTop_toTopOf="parent" // 반대는 end

    app:layout_constraintTop_toBottomOf="@id/cost_of_service" // 세로 제약 조건 : cost_of_service의 하단 가장자리
    ```

    제약 조건의 이름 : **layout_constraint<Source>_to<Target>Of** 형식
    **<Source> :** 현재 뷰
    **<Target> :** 뷰가 제한되는 타겟 뷰(상위 컨테이너 또는 다른 뷰)의 가장자리
    현재 뷰의 Source 자리를 Target 자리에 맞추겠다.

- 속성
    - id

        접두사 : @+id

        앱 코드에서 리소스 ID 참조 : R.<type>.<name>
        View ID의 경우 <type>이 id (예: R.id.button)

    - layout_height / layout_width

        wrap_content : 높이가 내부 콘텐츠의 높이와 같다

        match_parent : 

        ConstraintLayout의 하위 요소에서는 match_parent를 설정할 수 없음.

        따라서 뷰의 시작 및 끝 가장자리를 제한하고 너비를 0dp로 설정

    - inputType

        속성값 : "text(문자)", "number"(숫자),"numberDecimal"(소수점이 있는 숫자)..

    - hint

        사용자가 입력란에 입력해야 하는 내용을 설명

        ```kotlin
        android:hint="Cost of Service"
        ```

        ![https://developer.android.com/codelabs/basic-android-kotlin-training-xml-layouts/img/e57f60d70089e5c4.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-xml-layouts/img/e57f60d70089e5c4.png?hl=ko)

- 라디오 버튼 및 그룹

    기본 선택 : 아래 코드 추가하기

    ```kotlin
    android:checkedButton="@id/option_twenty_percent"
    ```

- Switch

    사용자가 두 옵션 간에 전환할 수 있음

---

### 뷰 결합

**Binding 클래스를 사용하여 레이아웃 ID가 있는 뷰에 직접 참조 할 수 있다.**

- 사용법
    1. `Gradle Scripts` > `build.gradle (Module: Tip_Time.app)` 로 이동한다.
    2. `android` 섹션에 아래 코드를 삽입한다.

        ```kotlin
        buildFeatures {
            viewBinding = true
        }
        ```

    3. 상단에 '**Gradle files have changed since last project sync.**' 메시지 옆 '**Sync now**'를 누른다.
    4. `app` > `java` > `com.example.tiptime` > `MainActivity` 로 이동한다.
    5. `MainActivity` 클래스의 코드를 아래의 코드로 대체한다

        ```kotlin
        class MainActivity : AppCompatActivity() {

            lateinit var binding: ActivityMainBinding

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

        				// **activity_main.xml** 레이아웃에서 **Views**에 액세스하는 데 사용할 **binding** 객체 초기화
                binding = ActivityMainBinding.inflate(layoutInflater)
                
        				// 레이아웃의 리소스 ID : R.layout.activity_main 
        				// 뷰 결합 없는 상태 -> setContentView(R.layout.activity_main) 
        				// 앱의 뷰 계층 구조 루트 : binding.root
        				setContentView(binding.root)
            }
        }
        ```

        **lateinit** 
        코드가 변수를 사용하기 전에 먼저 초기화할 것임을 확인함

- 사용 예시

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

### 팁 계산

1. 서비스 비용 텍스트 가져오기

    ```kotlin
    val stringInTextField = binding.costOfService.text
    ```

2. 가져온 텍스트 숫자로 변환하기

    `toDouble()` 은 `String` 에서 호출 되어야 함

    `EditText` 의 text 속성은 변경할 수 있는 텍스트  `~~String~~`⇒ `Editable` 

    ```kotlin
    val cost = stringInTextField.toDouble() // 작동하지 않는 코드

    // stringInTextField 를 String으로 바꿔주고 실행해야 함
    val stringInTextField = binding.costOfService.text.toString()
    val cost = stringInTextField.toDouble()
    ```

3. 팁 비율 가져오기

    `topOptions` `RadioGroup` 의 `checkedRadioButtonId` 속성을 가져온다

    ```kotlin
    // checked된 버튼의 id를 가져오므로 어떤 버튼이 선택되어있는지 알 수 있음
    val selectedId = binding.tipOptions.checkedRadioButtonId

    val tipPercentage = when (selectedId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
    ```

4. 팁 계산 및 반올림하기

    ```kotlin
    var tip = tipPercentage * cost

    val roundUp = binding.roundUpSwitch.isChecked // 반올림 스위치 속성 가져오기

    if (roundUp) { // 반올림 스위치 속성 값이 true라면
        tip = kotlin.math.ceil(tip) // 반올림하기
    }
    ```

5. 숫자 형식 지정하기

    ```kotlin
    val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
    ```

6. 팁 표시
    - `strings.xml` 의 `tip_amount` 문자열을 아래와 같이 변경

        ```kotlin
        // %s는 형식이 지정된 통화가 들어갈 위치
        <string name="tip_amount">Tip Amount: %s</string>
        ```

    - `TextView` 의 text 속성에 팁 텍스트 설정

        ```kotlin
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
        ```