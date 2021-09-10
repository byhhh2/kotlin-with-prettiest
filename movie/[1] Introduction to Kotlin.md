## Introduction to Kotiln

<br>

- <a href="https://developer.android.com/training/kotlinplayground?hl=ko">코틀린 대화형 코드편집기 </a>

<br>

### 변수와 출력

<br>

```kotlin
fun main() {
    val age = 24

    println("Happy Birthday, Rover!")
    println("You are already ${age}!")
    println("${age} is the very best age to celebrate!")

    println("   ,,,,,   ")
    println("   |||||   ")
    println(" =========")
    println("@@@@@@@@@@@")
    println("{~@~@~@~@~}")
    println("@@@@@@@@@@@")
    println("")
}
```

- `val` : 변경 불가
- `var` : 변경 가능

<br>

### 반복문

<br>

```kotlin
repeat(23) { //23번 반복
    print("=")
}

```

### 함수 & 매개변수

<br>

```kotlin
fun printBorder(border: String) {
    repeat(23) {
        print(border)
    }
    println()
} //인수의 타입지정
```

<br>

### 정리

<br>

- `${}`를 사용하여 `print` 문 텍스트의 변수와 계산 값을 둘러쌉니다. 예를 들어 `${age}`에서 `age`가 변수입니다.
- `val` 키워드와 이름을 사용하여 변수를 만듭니다. 설정이 완료되면 변경할 수 없습니다. 등호를 사용하여 변수에 값을 할당합니다. 값의 예로는 텍스트와 숫자가 있습니다.
- `String`은 `"Hello"`와 같이 따옴표로 묶인 텍스트입니다.
- Int는 양의 정수 또는 음의 정수(예: 0, 23, -1024)입니다.
- 함수에서 사용할 인수 한 개 이상을 함수에 전달할 수 있습니다. 예 : `fun printCakeBottom(age:Int, layers:Int) {}`
- `repeat() {}` 문을 사용하여 일련의 명령어를 여러 번 반복합니다. 예를 들면 `repeat (23) { print("%") }` 또는 `repeat (layers) { print("@@@@@@@@@@") }`이 있습니다.
- 루프는 명령어를 여러 번 반복하는 명령어입니다. `repeat()` 문은 루프의 예입니다.
- 루프를 중첩할 수 있습니다. 즉, 루프 내에 루프를 배치할 수 있습니다. 예를 들어 `repeat()` 문 내에 `repeat()` 문을 만들어 케이크 층을 만들 때처럼 여러 행에 걸쳐 기호를 여러 번 출력할 수 있습니다.

- 함수 인수 사용 요약: 함수에 인수를 사용하려면 다음 세 가지 작업을 실행해야 합니다.
  - 함수 정의에 인수와 유형을 추가합니다. `printBorder(border: String)`
  - 함수 내에서 인수를 사용합니다. `println(border)`
  - 함수 호출 시 인수를 제공합니다. `printBorder(border)`
