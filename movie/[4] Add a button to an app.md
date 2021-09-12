## Add a button to an app

### class & instance

```kotlin
class Dice (val numSides: Int) { //면 수를 입력받는 주사위 class
    fun roll() : Int { //주사위 굴리는 메서드, 반환 Int형
        return (1..numSides).random() //IntRange
    }
}

fun main() {
    //주사위 굴리기
    val myFirstDice = Dice(6) //면이 6개인 Dice 객체

    val diceRoll = myFirstDice.roll()
    println("Your ${myFirstDice.numSides} sided dice rolled ${diceRoll}!")
}
```

<br>

### 정리

- `IntRange`에서 `random()` 함수를 호출하여 랜덤 숫자를 생성합니다. `(1..6).random()`
- 클래스는 객체의 청사진과 같습니다. 변수와 함수로 구현된 속성과 동작을 포함할 수 있습니다.
- 클래스 인스턴스는 주사위와 같은 실제 객체를 나타내는 경우가 많습니다. 객체에서 작업을 호출하고 속성을 변경할 수 있습니다.
- 인스턴스를 만들 때 값을 클래스에 제공할 수 있습니다. 예를 들어 `class Dice(val numSides: Int)` 다음에 `Dice(6)`로 인스턴스를 만듭니다.
- 함수에서 무언가를 반환할 수 있습니다. 함수 정의에서 반환할 데이터 유형을 지정하고 함수 본문에서 `return` 문을 사용하여 무언가를 반환합니다. 예: `fun example(): Int { return 5 }`

<br>

## 주사위 굴리기 어플 만들기

<br>

### TextView

- `Text` : 보여질 text
- 도구 `text` : 개발자만 볼 수 있는 text, 사용자에겐 보여지지 않음

<br>

### `Activity`

- 실행되는 앱의 전체화면
- `Mainactivity` : 최상위 수준 or 첫번째 활동
- `Activity`는 여러개가 가능하다. (사진의 목록 activity, 개별 사진 activity, 사진 편집 activity 등..)

```kotlin
//MainActivity
package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity //프레임워크
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //main함수가 존재하지 않고 앱이 처음 열릴때 onCreate함수 실행
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //시작 레이아웃 설정
    }
}
```

<br>

### `Button`을 상호작용적으로 만들기

```kotlin
import androidx.appcompat.app.AppCompatActivity //프레임워크
import android.os.Bundle
import android.widget.Button //Alt + enter로 추가, 자동가져오기 설정하면 자동으로 가져와짐
import android.widget.TextView

//import android.widget.Toast
//import java.util.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //main함수가 존재하지 않고 앱이 처음 열릴때 onCreate함수 실행
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //시작 레이아웃 설정

        val rollButton: Button = findViewById(R.id.button) //id == button
        //객체 참조 저장

        rollButton.setOnClickListener { //클릭 리스너
            //Toast : 사용자에게 표시되는 간략한 메시지
            //Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show()
            //val resultTextView: TextView = findViewById(R.id.textView)
            //resultTextView.text = "6" //버튼이 클릭될 때 text의 문자 6으로 바꾸기
            rollDice()
        }
    }

    private fun rollDice() {
        val dice = Dice(6) //6면 주사위 만들기
        val diceRoll = dice.roll() //주사위 굴리기
        val resultTextView: TextView = findViewById(R.id.textView)
        resultTextView.text = diceRoll.toString()
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}
```

<br>

### 코드 서식

- 다시 지정
  - `ctrl + Alt + L`
- 주석
  - `/** **/` , `//`

<br>

### 정리

- Layout Editor를 사용하여 Android 앱에 `Button`을 추가합니다.
- `MainActivity.kt` 클래스를 수정하여 앱에 상호작용 동작을 추가합니다.
- `Toast` 메시지를 임시 솔루션으로 표시하여 제대로 작업하고 있는지 확인합니다.
- `setOnClickListener()`를 사용하여 `Button`의 클릭 시 리스너를 설정해 `Button`을 클릭할 때의 동작을 추가합니다.
- 앱이 실행 중일 때 레이아웃의 `TextView`, `Button` 또는 다른 UI 요소에서 메서드를 호출하여 화면을 업데이트할 수 있습니다.
- 코드에 주석을 달아 다른 개발자가 코드를 읽을 때 잘 이해할 수 있도록 합니다.
- 코드 서식을 다시 지정하고 코드를 정리합니다.

<br>

### `when` 문

```kotlin
class Dice (val numSides: Int) {
    fun roll() : Int {
        return (1..numSides).random()
    }
}

fun main() {
    val myFirstDice = Dice(6)
    val rollResult = myFirstDice.roll()
    val luckyNumber = 4

    when (rollResult) { //switch와 비슷한 when문
		luckyNumber -> println("You win!")
        //rollResult와 luckyNumber를 비교하고
        //일치하면 -> 뒤의 문장 실행
        1 -> println("So sorry! You rolled a 1. Try again!")
        2 -> println("Sadly, you rolled a 2. Try again!")
        3 -> println("Unfortunately, you rolled a 3. Try again!")
        5 -> println("Don't cry! You rolled a 5. Try again!")
        6 -> println("Apologies! you rolled a 6. Try again!")
    }
}

```

### `imageView`

- import 후에 id로 접근하려면 `R.drawable.(id)`
- 크기 변경 : `Constraints Widget` > `layout_width, height` (dp)
- `dp` (밀도 독립형 픽셀) : dp를 사용하면 픽셀 해상도가 다른 기기에서 이미지 크기가 적절하게 조정됨.
- `imageView`에서 도구 `srcCompat`에서 이미지 설정을 하면 개발자에게만 보여지게 됨

```kotlin
// 주사위 roll버튼을 눌렀을 때 주사위 이미지 바뀌게 하기
        val diceRoll = dice.roll() //주사위 굴리기
        val diceImage: ImageView = findViewById(R.id.imageView)
        val drawableResource = when (diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_6
        }

        diceImage.setImageResource(drawableResource)
        diceImage.contentDescription = diceRoll.toString() //콘텐츠 설명 업데이트
```

<br>

### 정리

- `setImageResource()`를 사용하여 `ImageView`에 표시되는 이미지를 변경합니다.
- `if / else` 표현식이나 `when` 표현식과 같은 제어 흐름 문을 사용하여 앱에서 다양한 사례를 처리합니다(예: 여러 상황에서 다양한 이미지 표시).

<br>

## lemonade 앱 만들기

<br>

- `lemonadeState` 는 레몬에이드의 현재 상태
- 가능한 상태는 4가지 : `SELECT`, `SQUEEZE`, `DRINK`, `RESTART`
- 클릭 시에 상태를 변경해야하고, 길게 클릭 시에 squeeze한 횟수를 알려준다.
- 클릭 시에 바뀐 상태에 맞게 text와 image를 변경해야한다.

1. `setOnClickListener()`을 사용하여 클릭시에 상태 변경, 텍스트 변경, 이미지 변경
2. `setOnLongClickListener()` 을 사용하여 길게 누르면 squeeze횟수를 보여주기

<br>

### 문자열 리소스 사용

- `res > values > strings.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="hello">Hello!</string>
    <string name="submit_label">Submit</string>
</resources>
```

```kotlin
val submitText = getResources().getString(R.string.submit_label)
```

- 형식 지정

```xml
<string name="ingredient_tablespoon">%1$d tbsp of %2$s</string>
```

```kotlin
getResources().getString(R.string.ingredient_tablespoon, 2, "cocoa powder")
```
