# [Unit 4] Pathway2

## 웹서비스

오늘날 대부분의 웹 서버는 REST(REpresentational State Transfer)라는 일반적인 스테이트리스(Stateless) 웹 아키텍처를 사용해 웹 서비스를 실행한다. 이 아키텍처를 제공하는 웹 서비스를 RESTful 서비스라고 한다.

표준화된 방법으로 URI를 통해 RESTful 웹 서비스에 요청이 전송된다. URI(Uniform Resource Identifier)는 리소스 위치나 리소스에 액세스하는 방법을 암시하지 않고 서버의 리소스를 이름으로 식별한다.

URL(Uniform Resource Locator)은 리소스 표현을 획득하거나 표현에 관해 조치를 취하는 수단을 지정하는 URI. 즉, 기본 액세스 메커니즘과 네트워크 위치를 모두 지정한다.

## 일반적인 HTTP 작업

- 서버 데이터를 검색하는 GET
- 서버에 새로운 데이터를 추가/생성/업데이트하는 POST 또는 PUT
- 서버에서 데이터를 삭제하는 DELETE

## 인터넷 연결

- Retrofit 라이브러리

Mars 웹 서비스와 통신하고 원시 JSON 응답을 String으로 표시 가능.

웹 서비스의 콘텐츠를 기반으로 앱의 네트워크 API를 생성. 

XML 및 JSON과 같이 많이 사용되는 데이터 형식을 위한 지원이 내장되어 있음. 

1. 네트워크 계층인 MarsApiService 클래스 생성.
2. 기본 URL 및 변환기 팩토리가 포함된 Retrofit 객체 생성.
3. Retrofit이 웹 서버와 통신하는 방법을 설명하는 인터페이스 생성.
4. Retrofit 서비스를 만들고 앱의 나머지 부분에 관해 인스턴스를 API 서비스에 노출.

앱이 인터넷에 연결하기 위해 Android 매니페스트에 "android.permission.INTERNET" 권한을 추가.

## 서버 연결 시 발생 문제

- API에 사용된 URL 또는 URI가 잘못됨
- 서버를 사용할 수 없어 앱을 서버에 연결할 수 없음
- 네트워크 지연 문제
- 기기의 인터넷 연결이 불안정하거나 기기가 인터넷에 연결되지 않음

→ 이러한 예외는 컴파일 시간에 포착 불가능

→  try-catch 블록을 사용하여 런타임에 예외를 처리 가능.

```kotlin
try {
    // 예외가 발생한 것으로 예상되는 코드
}
catch (e: SomeException) {
    // 앱이 갑자기 종료되는 것을 방지하는 코드
}
```

## JSON 응답구조

JSON 응답은 대괄호로 표시된 배열이다. 이 배열에는 JSON 객체가 포함된다.

JSON 객체는 중괄호로 묶여 있다. → { 중괄호 하나가 하나의 객체} 그 안에는 키:값 쌍의 집합이 존재.

각 JSON 객체에는 이름-값 쌍의 집합이 포함된다. 이름과 값은 콜론으로 구분.

[

{ "id" : "424905",

 "image_src" : "http:mars.jpl.nasa.gov/msl-raw-images/msss/004463130005227E03_DXXX.jpg"}

]

## Coil

- 준비물

로드하고 표시할 이미지의 URL

이미지를 실제로 표시하는 ImageView 객체

## 결합 어댑터

뷰의 맞춤 속성을 위한 맞춤 setter를 만드는 데 사용되는 주석 처리된 메서드

예시)

```kotlin
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        // Load the image in the background using Coil.
        }
    }
}
```

`@BindingAdapter` 속성 이름을 매개변수로 사용

`bindImage` 첫 번째 메서드 매개변수는 타겟 뷰의 유형, 두 번째 매개변수는 속성에 설정되는 값.

메서드 내부에서 Coil 라이브러리는 UI 스레드에서 이미지를 로드하여 ImageView로 설정합.

## enum

열거의 단축형으로, 컬렉션의 모든 항목을 순서가 지정된 목록으로 나열한다는 의미

상수 집합을 보유할 수 있는 데이터 유형

```kotlin
// 정의
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

// 사용
var direction = Direction.NORTH;
```