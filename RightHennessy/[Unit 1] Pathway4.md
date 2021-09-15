# [Unit 1] Pathway4

- 난수 발생기 만들기

```kotlin
fun main() {
    val diceRange = 1..6
    val randomNumber = diceRange.random()
    println("Random number: ${randomNumber}")
}
```

`random()`  함수를 이용해 난수 발생

`IntRange`  시작점부터 끝점까지 정수의 범위를 나타냄. 범위는 모든 정수 사이에서 가능. -10..4

- 주사위 클래스를 이용한 주사위 만들기

```kotlin
fun main() {
    val myFirstDice = Dice()
    val diceRoll = myFirstDice.roll()
    println("Your ${myFirstDice.sides} sided dice rolled ${diceRoll}!")
}

class Dice {
    var sides = 6

    fun roll(): Int {
        val randomNumber = (1..6).random()
        return randomNumber
    }
}
```

함수의 반환값을 설정할 때는 fun 함수명() : 변수형{ } 으로 생성한다.

가장 간결한 코드는 다음과 같다.

```kotlin
fun main() {
    val myFirstDice = Dice(6)
    println("Your ${myFirstDice.numSides} sided dice rolled ${myFirstDice.roll()}!")

    val mySecondDice = Dice(20)
    println("Your ${mySecondDice.numSides} sided dice rolled ${mySecondDice.roll()}!")
}

class Dice (val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}
```

- 주사위 앱 만들기

TextView에서 도구 텍스트는 스튜디오 내 Design Editor에만 표시되고 실제 기기에서 앱을 실행할 때는 표시되지 않음

`Activity` 앱이 UI를 그리는 창을 제공, 일반적으로 Activity는 실행되는 앱의 전체 화면을 차지함.

`MainActivity.kt`   app > java > com.example.diceroller > MainActivity.kt

**버튼 클릭 시 메시지 표기**

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton : Button = findViewById(R.id.button)
        rollButton.setOnClickListener{
						val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
            toast.show()
				}
		}
}
```

`findViewById()` 메서드, 레이아웃에서 Button을 찾음 

`R.id.button` Button의 리소스 ID, Button의 고유한 식별자

**버튼 클릭 시 textView 업데이트**

```kotlin
rollButton.setOnClickListener {
           val resultTextView: TextView = findViewById(R.id.textView)
           resultTextView.text = "6"
       }
```

**주사위 굴리기 로직 추가**

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton : Button = findViewById(R.id.button)
        rollButton.setOnClickListener{rollDice()}
		}

    private fun rollDice(){
        val dice = Dice(6)
        val diceRoll = dice.roll()
        val resultTextView: TextView = findViewById(R.id.textView)
        resultTextView.text= diceRoll.toString()
    }
}

class Dice(val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}
```

**던진 주사위 숫자와 그림 매칭**

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton : Button = findViewById(R.id.button)
        rollButton.setOnClickListener{rollDice()}
		}

    private fun rollDice(){
        val dice = Dice(6)
        val diceRoll = dice.roll()
        val diceImage: ImageView = findViewById(R.id.imageView)

				/*when (diceRoll) {
		       1 -> diceImage.setImageResource(R.drawable.dice_1)
		       2 -> diceImage.setImageResource(R.drawable.dice_2)
		       3 -> diceImage.setImageResource(R.drawable.dice_3)
		       4 -> diceImage.setImageResource(R.drawable.dice_4)
		       5 -> diceImage.setImageResource(R.drawable.dice_5)
		       6 -> diceImage.setImageResource(R.drawable.dice_6)
			   }*/
        val drawableResource = when(diceRoll) {
            1 -> R.drawable.dice_1
						2 -> R.drawable.dice_2
						3 -> R.drawable.dice_3
						4 -> R.drawable.dice_4
						5 -> R.drawable.dice_5
						else -> R.drawable.dice_6
				}

        diceImage.setImageResource(drawableResource)

        diceImage.contentDescription= diceRoll.toString()
    }
}

class Dice(val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }
}
```

when 함수를 사용할 때 곡 else 문을 포함해야 한다. → 6 대신 else 사용

- 자동 import 설정

File > Other Setting > Setting for New Project

Other Settings > Auto Import

Java 및 Kotlin 섹션에서 Add unambiguous imports on the fly 및 Optimize imports on the fly (for current project)가 선택되어 있는지 확인

- 사진 불러오는 법

Project 창 왼쪽에 있는 Resource Manager 탭을 클릭

Resource Manager 아래의 +를 클릭하고 Import Drawables를 선택

`srcCompat` 앱을 빌드할 때 개발자에게만 표시, 실제로 기기에서 앱을 실행할 때는 표시안됨.

- kotlin에서 조건문(when) 사용하기

```kotlin
fun main() {
    val myFirstDice = Dice(6)
    val rollResult = myFirstDice.roll()
    val luckyNumber = 4

    when (rollResult) { //각각 일치하는 상황에 맞춰 출력가능
        luckyNumber -> println("You won!")
        1 -> println("So sorry! You rolled a 1. Try again!")
        2 -> println("Sadly, you rolled a 2. Try again!")
        3 -> println("Unfortunately, you rolled a 3. Try again!")
        5 -> println("Don't cry! You rolled a 5. Try again!")
        6 -> println("Apologies! you rolled a 6. Try again!")
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}
```