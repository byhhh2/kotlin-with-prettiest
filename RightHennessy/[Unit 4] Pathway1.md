# [Unit 4] Pathway1

- 참고자료
    
    [https://kotlinworld.com/149](https://kotlinworld.com/149)
    

## 멀티스레딩

운영체제는 시스템, 프로그래밍 언어, 동시 실행 단위의 특성을 사용하여 멀티태스킹을 관리 가능.

동시 실행을 사용해야 하는 이유

: 네트워크 요청과 같은 장기 실행 작업을 실행해도 앱에서 다른 작업의 실행이 중지되지 않기 위해.

`currentThread()` 스레드의 이름, 우선순위, 스레드 그룹을 반환. Thread 인스턴스를 문자열로 반환

## 기본 스레드 (UI 스레드)

(경우에 따라 UI 스레드와 기본 스레드가 다를 수 있음.)

실행 중인 앱에는 여러 스레드가 있지만 각 앱에는 전용 스레드가 하나 있고 특히 앱의 UI를 담당함.

앱의 UI 실행을 담당하므로 기본 스레드가 앱이 원활하게 실행되도록 성능 기준에 맞는 것이 중요.

프로세서가 스레드 명령어 집합 간 전환 시 스레드가 실행과 중지되는 시점은 개발자가 제어 불가능

→ 성능 문제, 경합 상태, 재현하기 어려운 버그는 스레드를 직접 사용하라고 권장하지 않는 이유

→ 동시 실행 코드 작성에 도움이 되는 코루틴이라는 Kotlin의 기능

## 코루틴

멀티태스킹을 지원하지만 단순히 스레드로 작업하는 것보다 다른 수준의 추상화를 제공

`Job`  취소 가능한 작업 단위  예) launch() 함수로 만든 작업 단위

`CoroutineScope`  하위 요소와 그 하위 요소에 취소 및 기타 규칙을 반복적으로 적용하는 컨텍스트

→ launch() 및 async()와 같은 새 코루틴을 만드는 데 사용되는 함수는 CoroutineScope를 확장

`Dispatcher` 코루틴이 실행에 사용할 지원 스레드를 관리, 새 스레드 초기화하는 성능 비용이 발생 X.

→ 개발자가 새 스레드를 사용할 시기와 위치를 파악하지 않아도 됨.

Main 디스패처는 항상 기본 스레드에서 코루틴을 실행

Default나 IO, Unconfined와 같은 디스패처는 다른 스레드를 사용

```kotlin
import kotlinx.coroutines.*

fun main() {
    repeat(3) {
        GlobalScope.launch {
            println("Hi from ${Thread.currentThread()}")
        }
    }
}
```

`GlobalScope` 앱이 실행되는 한 내부의 코루틴이 실행되도록 허용

`launch()` 취소 가능한 Job 객체에 래핑된 닫힌 코드에서 코루틴을 만듬.

                 반환 값이 코루틴의 범위 밖에서 필요하지 않을 때 사용

```kotlin
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ISO_LOCAL_TIME
val time = { formatter.format(LocalDateTime.now()) }

suspend fun getValue(): Double {
    println("entering getValue() at ${time()}")
    delay(3000)
    println("leaving getValue() at ${time()}")
    return Math.random()
}

fun main() {
    runBlocking {
        val num1 = async { getValue() }
        val num2 = async { getValue() }
        println("result of num1 + num2 is ${num1.await() + num2.await()}")
    }
}
```

`runBlocking()`  새 코루틴을 시작하고 완료될 때까지 현재 스레드를 차단.

                          기본 함수와 테스트에서 차단 코드와 비차단 코드 사이를 연결하는 데 사용

`getValue()` 설정된 시간 후에 랜덤 숫자를 반환 후 DateTimeFormatter를 사용 → 출입 시간 출력

`async()`  Deferred 유형의 값을 반환.

`Deferred` 결과값을 수신하는 비동기 작업, 자리표시자 역할

                 결과가 있는 비동기 작업을 수행하기 위해 결과 값이 없는 Job을 확장하는 인터페이스

                 즉시 값을 반환하는 것처럼 함수를 계속 호출 가능

`suspend` 실제로 개발자가 실행을 위해 전달한 코드 블록

`await()`  Deferred에서 결과값을 수신하기 위해 사용하는 Deferred인터페이스 상의 메서드

→ 호출 시 main 함수가 수행되는 코루틴은 IO Thread로부터 Deferred의 결과가 수신될 때까지 중단.