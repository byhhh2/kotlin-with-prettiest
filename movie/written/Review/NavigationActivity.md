### 인텐트

- 실행할 작업을 알려줌

1. 암시적 인텐트 : 추상적, 다른 앱에서 할 활동이라던가.
2. 명시적 인텐트 : 내 앱에서 할 활동을 명확히

<br>

#### extra

- 전달해줄 데이터
- Bundle 유형

<br>

버튼을 누르면 화면을 넘어가게 하자

```kotlin
// 1. 이동하기 전 화면
holder.button.setOnClickListener {
    val context = holder.view.context
    val intent = Intent(context, DetailActivity::class.java) // 대상 activity 전달
    // 해당 Activity로 이동하고 싶다.

    intent.putExtra("letter", holder.button.text.toString()) // 전달하고 싶은 데이터

    context.startActivity(intent) // intent 전달
}

// 2. 이동한 후 화면 (onCreate)
val letterId = intent?.extras?.getString("letter").toString() // intent에서 전달된 extra 빼옴
```

<br>

#### 암시적 인텐트

```kotlin
//DetailActivity

companion object {
   const val SEARCH_PREFIX = "https://www.google.com/search?q="
}

holder.button.setOnClickListener {
    val queryUrl: Uri = Uri.parse("${DetailActivity.SEARCH_PREFIX}${item}")
    val intent = Intent(Intent.ACTION_VIEW, queryUrl) // ACTION_VIEW => URI를 쓰겠다는 인텐트
    context.startActivity(intent)
}
```
