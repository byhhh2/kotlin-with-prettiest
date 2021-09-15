# Pathway1

- **val**

    변수 선언

    이 키워드 사용시 변수 설정은 딱 한 번 가능

- **${variable}**

    문자열 내에서 변수 사용시 표현 방법

- **프린트 문 : println**
- **함수 선언 : fun (== def)**
- **반복문 : repeat(반복할 횟수)**
- **함수 매개변수**

    매개변수 이름 : 매개변수 타입 (첫 글자는 대문자)

```kotlin
fun myage(age:Int, name:String) {
		println("${name} is ${age}!")
}
repeat(30){//30번 반복
		myage(25,"선영")
}
```