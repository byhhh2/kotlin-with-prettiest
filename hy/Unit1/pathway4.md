### 버튼 하드코딩 문자열을 문자열 리소스로 변경

1. component tree 경고 클릭<br>
2. 경고메세지 하단의 Suggested Fix > Fix 버튼 클릭<br>
3. Extract Resource 대화상자 확인 후 ok 버튼<br>
4. 텍스트 속성이 @string/roll 로 변경됨    
<br><br><br> 
### 개발자 텍스트 툴 

![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-create-dice-roller-app-with-button/img/948d74fe1557f26.png)
Common Attributes > text 밑 도구text 속성  
: Design Editor에만 표시되고 앱 실행시 표시 x 
<br><br><br> 
### auto imports 설정
 
1. File > Settings > Editor > General > Auto Import > Java and Kotlin sections 
2. Add unambiguous imports on the fly :heavy_check_mark: 
: import 문 자동 추가 
3. Optimize imports on the fly (for current project) :heavy_check_mark: 
: 사용되지 않는 import문 삭제
<br><br><br>  
### 버튼 클릭시 동작 추가 리스너 사용

```kotlin
    val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener {
            //toast 사용
            //val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
            //toast.show()

            //TextView 업데이트
            rollDice()
        }
```
<br><br><br> 
### Toast
 
: 사용자에게 표시되는 간략한 메세지 

```kotlin
val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
            toast.show()
```
<br><br><br>  
### 앱에 이미지 추가 
1. View > Tool Windows > Resource Manager를 클릭하거나 Project 창 왼쪽에 있는 Resource Manager 탭을 클릭합니다. 
2. Resource Manager 아래의 +를 클릭하고 Import Drawables를 선택합니다. 파일 브라우저가 열립니다. 
3. 파일 가져오기가 완료되면 이미지가 앱의 Resource Manager(app>res>drawable)에 표시됩니다. 
4. ImageView의 속성에서 Declared Attributes 섹션 > srcCompat 속성 
: Design 뷰 내에서만 제공된 이미지를 사용 

> 이미지 설명 설정

```kotlin
diceImage.contentDescription = diceRoll.toString()
```
<br><br><br>   
--------------------------------------------------
<br>
### RandomDice

```kotlin
fun main() {

    // val diceRange = 1..6
    val diceRange : IntRange = 1..6
    println("diceRange: ${diceRange}")

    val randomNumber = diceRange.random()
	println("Random number1: ${randomNumber}")
    println("Random number2: ${(1..6).random()}")
    
    
    val firstDice = Dice()
    val firstRoll = firstDice.roll()
    println("firstDice side: ${firstDice.sides} and roll: ${firstRoll}")
	firstDice.sides=20
    println("firstDice side: ${firstDice.sides} and roll: ${firstDice.roll()}")
    
    
    val secondDice = Dice2(6)
    val secondRoll = secondDice.roll()
    println("secondDice side: ${secondDice.numSides} and roll: ${secondRoll}")
    
    
    val thirdDice = Dice()
}


class Dice {
    var sides = 6
    fun roll(): Int {
        return (1..sides).random()
    }
}


class Dice2(val numSides: Int) {
     fun roll(): Int {
        return (1..numSides).random()
    }
}
```
<br><br><br>  
### Lucky Dice

```kotlin
fun main() {
    val myFirstDice = Dice(6)
    val rollResult = myFirstDice.roll()
    println("Your ${myFirstDice.numSides} sided dice rolled ${rollResult}!")
    
    val luckyNumber = 4
    
    //if - else 사용
//     if (rollResult == luckyNumber) {
// 		println("you win!")
//     }
//     else if(rollResult < luckyNumber) {
//         println("you lose! because your dice is lower than mine")
//     }
//     else {
//         println("you lose! because your dice is higher than mine")
//     }
    
    //when 사용
    when(rollResult) {
        luckyNumber -> println("you win!") 
        1 -> println("you rolled a 1.")
        2 -> println("you rolled a 2.")
        3 -> println("you rolled a 3.")
        5 -> println("you rolled a 5.")
        6 -> println("you rolled a 6.")
    }
    
    val objlong : Long = 55
    println(whenAssign("Hello"))
    println(whenAssign(3.4))
    println(whenAssign(objlong))
    println(whenAssign(Dice(5)))
}

class Dice (val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}

fun whenAssign(obj: Any): Any {
    val result = when (obj) {                   // 1
        1 -> "one"                              // 2
        "Hello" -> 1                            // 3
        is Long -> false                        // 4
        in 1..10 -> "1~10"
        else -> 42                              // 5
    }
    return result   
}
```


