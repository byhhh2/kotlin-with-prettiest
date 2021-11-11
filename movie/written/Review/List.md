- **읽기 전용 목록**: List는 만든 후 수정할 수 없습니다.
- **변경 가능한 목록**: MutableList는 만든 후 수정할 수 있습니다.

<br>

### List

```kotlin
val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)
val numbers = listOf(1, 2, 3, 4, 5, 6) // 타입 추론
```

<br>

### MutableList

```kotlin
val entrees: MutableList<String> = mutableListOf()
val entrees = mutableListOf<String>()

entrees.add("noodles") // 하나의 항목 추가

val moreItems = listOf("ravioli", "lasagna", "fettuccine")

entrees.addAll(moreItems) // 여러 항목 추가

entrees.remove("noodles") // 항목 삭제, 삭제할 항목이 없으면 false 반환
entrees.removeAt(0) // index로 삭제
```

<br>

### for

```kotlin
for (n in numberList) {
    // each element
}
```

<br>

### forEach

```kotlin
list.forEach { print("${it.key} is ${it.value}, ") }
```

<br>

### filter

```kotlin
val filteredNames = peopleAges.filter { it.key.length < 4 }
println(filteredNames)
```
