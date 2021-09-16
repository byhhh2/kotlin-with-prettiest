# Pathway4

- IntRange 유형

    시작점 ~ 끝점 까지의 정수의 범위

- random 함수

    (랜덤으로 돌릴 배열).random

```kotlin
val rangeofDice = 1..6 //시스템이 자동적으로 IntRange로 인식
val randomNumber = rangeofDice.random()

//랜덤 바로 쓰기 //1~30 사이의 랜덤 뽑기
(1..30).random()
```

- class 키워드 & class 내의 함수

```kotlin
fun main() {
    val myFirstDice = Dice()
    println("Your ${myFirstDice.sides} sided dice rolled ${myFirstDice.roll()}!")
}

class Dice {
    var sides = 6
		fun roll() {
        val randomNumber = (1..sides).random()
        println(randomNumber)
    }
}
```

- 함수 반환

```kotlin
fun roll(): Int { //반환값 타입 적기
         val randomNumber = (1..6).random()
        return randomNumber
}
```

- class 생성자

```kotlin
class Dice (val numSides: Int) {
    fun roll(): Int {
        val randomNumber = (1..numSides).random()
        return randomNumber
    }
}
```

---

- 버튼 추가
    - Constraint layout

        Start → StartOf parent (0dp), End → EndOf parent (0dp) : 가로로 가운데에 위치

        ![https://developer.android.com/codelabs/basic-android-kotlin-training-create-dice-roller-app-with-button/img/dc70d6dc03ca919a.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-create-dice-roller-app-with-button/img/dc70d6dc03ca919a.png?hl=ko)

    - Text

        일반 : 앱이 실행 중일 때 사용자에게 표시되는 내용

        도구 모양 : 개발자만을 위한 '도구 텍스트' 속성 (실제 기기 또는 에뮬레이터에서 앱을 실행할 때는 표시되지 않음)

        ![https://developer.android.com/codelabs/basic-android-kotlin-training-create-dice-roller-app-with-button/img/948d74fe1557f26.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-create-dice-roller-app-with-button/img/948d74fe1557f26.png?hl=ko)

- Activity

    앱이 UI를 그리는 창 (쉽게 화면이라고 생각하면 될 듯)

- **안드로이드 실행 순서**

    `MainActivity` 의 `onCreate()` 실행 (코틀린의 main함수 대신 실행함)

    ```kotlin
    package com.example.diceroller

    // import문을 통해 사용할 프레임워크 클래스 지정
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle

    class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main) //시작 레이아웃 지정
        }
    }
    ```

- findViewById()

    리소스 ID로 찾기 (객체 지정) 

    ```kotlin
    val rollButton: Button = findViewById(R.id.button)
    ```

    Alt + Enter : import 자동으로 가져오기 단축키

- 버튼에 리스너 달기

    ```kotlin
    rollButton.setOnClickListener {
    					 ///ver.1.1 //토스트 버전
               val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
               toast.show()
    					
    					 //ver.1.2
    					 Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show()
    	
    					 //ver.2 //TextView에 문자 띄우기
    					 val resultTextView: TextView = findViewById(R.id.textView)
               resultTextView.text = "6"
           }
    ```

    Toast : 간단한 메시지가 포함된 보기 클래스

---

- When 문

    rollResult와 luckyNumber가 같을 경우 → (실행할 내용)

    ```kotlin
    when (rollResult) {
            luckyNumber -> println("You win!")
        }
    ```

---

- srcCompat

    Android 스튜디오의 Design 뷰 내에서만 제공된 이미지를 사용

    이미지는 앱을 빌드할 때 개발자에게만 표시되고 실제로 에뮬레이터나 기기에서 앱을 실행할 때는 표시되지 않음

- contentDescription

    스크린 리더로 읽는 것

    ```kotlin
    diceImage.contentDescription = diceRoll.toString()
    ```

- 문자열 리소스 사용

    `values` > `strings.xml` 파일에서 텍스트 정의 

    ![https://developer.android.com/codelabs/basic-android-kotlin-training-project-lemonade/img/65588c6102e5139.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-project-lemonade/img/65588c6102e5139.png?hl=ko)

    - `strings.xml` 에서 정의 방법

        ```kotlin
        <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <string name="hello">Hello!</string>
            <string name="submit_label">Submit</string>
        </resources>
        ```

    - 정의한 리소스 사용 방법

        ```kotlin
        val submitText = getResources().getString(R.string.**submit_label**)
        ```

        ```kotlin
        val infoTextView: TextView = findViewById(R.id.info_textview)

        infoTextView.setText(R.string.info_text)
        ```

    - 문자열에 다른 텍스트 삽입
        - 정의

            ```kotlin
            <string name="ingredient_tablespoon">%1$d tbsp of %2$s</string>
            ```

        - 사용

            ```kotlin
            getResources().getString(R.string.ingredient_tablespoon, 2, "cocoa powder")
            ```

        - 출력

            ```kotlin
            2 tbsp of cocoa powder
            ```