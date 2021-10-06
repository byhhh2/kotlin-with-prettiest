# Coroutines

- 인터넷에서 이미지 다운로드 등 장기 실행 작업이 올바르게 수행되지 않으면 이를 수행하는 동안 스크롤이 버벅거려 UI가 응답하지 않는 것처럼 보일 수 있다.
- 이를 방지하기 위해서 스레드를 사용해야한다.
- 스레드는 앱에서 코드를 실행하는 단일 경로
- 앱에는 기봉 스레드가 있다. (= UI 스레드) : 각 명령어는 다음 줄이 실행되기 전에 이전줄이 완료되기를 기다린다.
- 기본 스레드 외에 스레드 : 프로세서는 별도의 스레드를 작동하지 않다가 어떤 명령어 간에 전환하여 멀티태스킹의 모양을 제공한다. 스레드는 코드를 작성할 때 각 명령어가 선택해야하는 실행경로를 결정하는데 사용할 수 있다.
- **기본 스레드가 아닌 스레드로 작업하면 UI에 응답성을 유지하면서 이미지 다운로드와 같은 복잡한 작업을 백그라운드에서 실행할 수 있다 이를 concurrency(동시실행) 이라고 한다.**

<br>
<br>
<br>

## thread

![image](https://user-images.githubusercontent.com/52737532/136191038-ae5ffdf5-0f3c-4213-8cfb-c9cfa7a0357a.png)

- 이전까지는 단일 실행경로로 작업
- 앱이 커짐에 따라 동시 실행을 고려해야한다.

<br>

> 동시 실행
>
> 여러 코드를 순서에 맞지 않거나 병렬로 실행할 수 있어서 리소스 사용의 효율이 높아진다.
>
> - 앱이 복잡해짐에 따라 코드가 차단되지 않는 것이 중요하다.
> - 네트워크와 같은 장기 실행 작업을 실행하더도 앱에서의 다른 작업이 실행이 중지되면 안된다.
> - 동시 실행을 올바르게 구현하지 않으면 앱이 사용자에게 응답하지 않는 것으로 보인다.

<br>

### thread 사용하기

```kotlin
fun main() {
   val states = arrayOf("Starting", "Doing Task 1", "Doing Task 2", "Ending")
   repeat(3) {
       Thread {
           println("${Thread.currentThread()} has started")
           for (i in states) {
               println("${Thread.currentThread()} - $i")
               Thread.sleep(50) //지정된 시간동안 현재 스레드 일시 중단
           }
       }.start()
   }
}
```

- `start()` : 스레드 실행
- `currentThread()` : 스레드의 이름, 우선순위, 스레드 구룹을 반환

```
Thread[Thread-0,5,main] has started
Thread[Thread-1,5,main] has started
Thread[Thread-2,5,main] has started
Thread[Thread-1,5,main] - Starting
Thread[Thread-0,5,main] - Starting
Thread[Thread-2,5,main] - Starting
Thread[Thread-1,5,main] - Doing Task 1
Thread[Thread-0,5,main] - Doing Task 1
Thread[Thread-2,5,main] - Doing Task 1
Thread[Thread-0,5,main] - Doing Task 2
Thread[Thread-1,5,main] - Doing Task 2
Thread[Thread-2,5,main] - Doing Task 2
Thread[Thread-0,5,main] - Ending
Thread[Thread-2,5,main] - Ending
Thread[Thread-1,5,main] - Ending
```

- 무작위로 수행됨
- **스케쥴러는 각 스레드에게 일정 시간을 제공하고 스레드는 그 시간내에 완료되거나 다른 시간을 받을 때까지 정지 된다.**

<br>
<br>
<br>

## thread를 사용해야하나?

- thread를 만들고 전환하고 관리하는데에도 비용이 사용된다.
- UI 스레드 = 기본 스레드 (경우에 따라 다를 수 있음)
- 기본 스레드가 UI 실행을 담당하므로 **기본 스레드**에 성능 기준을 맞추는 것이 중요하다.

<br>

### race condition & 예측할 수 없는 thread의 동작

- 프로세서가 여러 스레드의 명령어 집합 간에 전환할 때 스레드가 실행되는 정확한 시간과 스레드가 일시중지되는 시점은 개발자가 제어할 수 없다.
- 스레드를 직접 사용할 때 예측 가능한 출력을 기대할 수 없다.
- 여러 스레드로 작업할 때는 race condition도 발생할 수 있다. -> 비정상 종료 유발

<br>

> 성능문제, race condition, 버그 등으로 스레드를 직접 사용하라고 권장하지 않는다. 이런 이유로 coroutine이라는 kotlin의 기능을 사용한다.

<br>
<br>
<br>

## coroutine

- 코루틴의 주요 기능 중 하나는 상태를 저장하여 중단했다가 재개할 수 있다.

|      name       |                                                                   explanation                                                                   |
| :-------------: | :---------------------------------------------------------------------------------------------------------------------------------------------: |
|       job       |                                  수명 주기가 있는 취소 가능한 작업단위 (예 : `launch()` 함수로 만든 작업 단위)                                  |
| CoroutineScope  |                  `launch()`나 `async()`와 같은 새 코루틴을 만드는데 사용되는 함수가 확장하는 것 (컨텍스트임)<br> 코루틴의 범위                  |
|   Dispatcher    |                                               코루틴이 사용할 스레드를 결정 , CoroutineScope 만듦                                               |
| launch 와 async | CoroutineScope의 확장함수이며, 넘겨 받은 코드 블록으로 **코루틴을 만들고** 실행 <br> `launch` : job 객체 반환 <br> `async` : Deferred 객체 반환 |

<br>

1. Dispatcher 를 이용해서 CoroutineScope 만듦
2. CoroutineScope의 launch 또는 async 에 수행할 코드 블록을 넘기기

<br>
<br>

---

```kotlin
import kotlinx.coroutines.*

fun main() {
    repeat(3) {
        GlobalScope.launch { // Global Scope에서 코루틴 세 개를 만듦
            println("Hi from ${Thread.currentThread()}")
        }
    }
}
```

### `Global Scope`

- 앱이 실행되는 동안 내부의 코루틴이 실행되도록 허용

### `launch()`

- job 객체에 래핑된 코드에서 코루틴을 만든다.
- launch는 반환값이 코루틴 범위 밖에서 필요하지 않을 때 사용

### `suspend`

- launch()에 개발자가 실행을 위해 전달된 코드는 `suspend` 키워드로 표시됨
- 코드 또는 함수 블록이 일시중지되거나 재개될 수 있음을 나타낸다.
- `suspend` 함수를 호출하면 그 함수는 `suspend` 함수여야 한다.

### `runBlocking()`

- 새 코루틴을 시작하고 완료될 때까지 현재 스레드를 차단한다.
- 테스트에서 사용

---

<br>
<br>

### 예시

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

### `async()`

- `Deferred` 타입 값을 반환 : **미래 값 참조를 보유할 수 있는 최소 가능한 job**
- `Deferred`를 사용하면 즉시 사용가능한 값을 반환하는 것처럼 함수를 호출할 수 있다.
- 다른 언어에서 `promise`나 `future`와 같은 의미
- 나중에는 이 객체의 값이 반환된다고 보장
- 비동기 작업에서는 실행을 기다리지 않기 때문에 `Deferred`의 출력을 기다리도록 하려면 `await()`를 호출하면 된다. (`Deferred`객체가 아닌 원시값이 반환됨)

---

**main에서 getValue() [suspend] 를 호출하는데에도 main이 suspend 키워드를 갖지 않는 이유는?**

runBlocking() : suspend 함수 아님  
runBlocking()에 전달된 함수 : suspend 함수

suspend함수를 직접 호출하지 않으면 그 자체가 suspend 함수가 아니여도 된다.

---
