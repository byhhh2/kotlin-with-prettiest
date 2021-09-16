# [Unit 1] Pathway1

`fun`  함수

함수 이름 : 첫단어: 소문자, 동사 / 두번쨰부터: 대문자

`val`  변수를 한번만 설정 가능

`var`  변수를 변경 가능

> ${변수명} 으로 사용가능

`repeat() { 반복할 내용 }`  함수) 괄호 안에 횟수와 {}안에 반복할 내용 적음

- 테두리 함수 만들기

```kotlin
fun main() {
    val border = "`-._,-'"
    val timesToRepeat = 4
    printBorder(border, timesToRepeat)
    println("  Happy Birthday, Jhansi!")
    printBorder(border, timesToRepeat)
}

fun printBorder(border: String, timesToRepeat: Int) {
    repeat(timesToRepeat) {
        print(border)
    }
    println()
}
```

함수의 인자를 입력 받을 때 (인자 : 변수) 형태로 만든다.

→ 결과

```
`-._,-'`-._,-'`-._,-'`-._,-'
  Happy Birthday, Jhansi!
`-._,-'`-._,-'`-._,-'`-._,-'
```