
## Multithreading and concurrency 멀티 스레드와 동시실행

* 동기 프로그래밍 : 시간 관계없이 요청에 대한 응답이 올 때까지 프로그램 수행을 멈추고 기다리는 방식
* 비동기 프로그래밍 : 연산을 요청한 후에 결과를 기다리지 않고 다른 작업을 수행하는 방식

<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-introduction-coroutines/img/fe71122b40bdb5e3.png?hl=ko" style="width:80%; margin-top: 20px;">
 

<br><br>

### Thread
* 프로그램 범위 내에서 예약하고 실행할 수 있는 코드의 최소 단위
* 람다로 스레드 생성 가능
* Main Thread / UI Thread : android app의 전용 스레드. app의 UI를 담당
* 스레드가 실행되는 정확한 시간과 일시중지되는 시간은 제어x
* 스레드 직접 사용을 권장하지 않는 이유 
  * 예측 불가능한 출력
  * 성능문제
  * UI 응답 x
  * race condition : 여러 스레드가 메모리의 같은 value에 동시에 접근할 때 발생
  * 재현하기 어려운 버그 등

```kotlin
fun main() {
    val thread = Thread {
        println("${Thread.currentThread()} has run.")
    }
    thread.start()
}
```
* `start()` : 호출 전까지 스레드 실행x 
* `currentThread()` : 스레드 이름, 우선순위, 스레드 그룹을 표현하는 문자열로 변환되는 Thread 인스턴스 반환


<br><br><br>

### Coroutine 코루틴 


* `CoroutineScope` 
  * 코루틴의 범위
  * 코루틴 블럭을 묶음으로 제어할 수 있는 단위

<br>

* `GlobalScope` 
  * CoroutineScope의 한 종류
  * 미리 정의된 방식으로 프로그램 전반에 걸쳐 백그라운드에서 작동

<br>

* `CoroutineContext` 
  * 코루틴을 처리할 방식에 대한 여러 정보 집합
  * 주요요소 `Job`, `Dispatcher`

<br>


* `Dispatcher` 
  * 코루틴이 실행에 사용할 지원 스레드를 관리
  * `CoroutineContext`를 상속받아 어떤 스레드에서 어떻게 동작할 것인지 미리 정의 해 둠
  * `Dispatchers.Default` : CPU 사용량이 많은 작업에 사용. main스레드에서 작업하기에 너무 긴 작업에 알맞음.
  * `Dispatchers.IO` : 네트워크, 디스크를 사용할 때 사용. 파일 읽기/쓰기, 소켓 읽기/쓰기, 작업 중지에 최적화됨.
  * `Dispatchers.Main` : main스레드에서 코루틴 실행. 안드로이드의 경우 UI 스레드 사용.
  * `Dispatchers.Unconfined` : 다른 Dispatcher와 달리 특정 스레드, 스레드 풀 지정x. 일반적으로 사용하지 않으며 특정 목적을 위해서만 사용됨.

<br>

* `suspend`
  * 코드, 함수 블록이 일시 중지되거나 재개될 수 있음을 나타냄
  * 함수가 `suspend`함수를 호출하면 그 함수는 `suspend`여야 함
  * ex : `delay()`


<br>


* `launch` 
  * CoroutineScope의 확장함수 
  * 넘겨 받은 코드 블록으로 코루틴을 만들고 실행해주는 코루틴 빌더 
  * `Job`객체 반환. 반환받은 `Job`객체로 코루틴 블럭을 취소하거나 완료되기를 기다릴 수 있음
  * 반환 값이 코루틴 범위 밖에서 필요하지 않을 때 사용 
  * `join()`으로 대기

<br>

* `async` 
  * CoroutineScope의 확장함수
  * 넘겨 받은 코드 블록으로 코루틴을 만들고 실행해주는 코루틴 빌더
  * `Deffered`객체로 제어 가능하며 `Deffered`객체 반환
  * `await()`로 대기

<br>

* `Job` 
  * 취소 가능한 작업 단위

<br>

* `Deffered` 
  * 미래 값 참조를 보유할 수 있는 취소 가능한 `Job`
  * 즉시 값을 반환하는 것처럼 계속 함수 호출 가능
  * 비동기 작업이 언제 반환될 지 모르기 때문에 자리표시자 역할만 수행
  * 다른 언어의 Promise, Future

<br>

* `runBlocking` 
  * 내부 작업이 종료될 때까지 현재 스레드를 일시중지
  * 추가 함수 호출 없이 해당 블록이 완료될때까지 대기 가능
  * `Deffered`객체 반환
 

<br><br><br>

### 코루틴 사용 방법
1. 사용할 Dispatcher 결정
2. Dispatcher를 이용해 CoroutineScope 생성
3. CoroutineScope의 launch 또는 async 에 수행할 코드 블록 작성 


 
<br><br><br>
<hr>
<br>

### Thread / Coroutine
```kotlin
fun main() {
   val states = arrayOf("Starting", "Doing Task 1", "Doing Task 2", "Ending")
   repeat(3) {
       Thread {
           println("${Thread.currentThread()} has started")
           for (i in states) {
               println("${Thread.currentThread()} - $i")
               Thread.sleep(50)
           }
       }.start()
   }
}
```
```kotlin
import kotlinx.coroutines.*

fun main() {
   val states = arrayOf("Starting", "Doing Task 1", "Doing Task 2", "Ending")
   repeat(3) {
       GlobalScope.launch {
           println("${Thread.currentThread()} has started")
           for (i in states) {
               println("${Thread.currentThread()} - $i")
               delay(5000)
           }
       }
   }
}
```

<br><br><br>
<hr>
<br>


### <퀴즈>

* `suspend` functions <br>
a. `async()`에 전달된 람다 <br>
a. `runBlocking()`에 전달된 람다 <br>
 



<br><br><br>
