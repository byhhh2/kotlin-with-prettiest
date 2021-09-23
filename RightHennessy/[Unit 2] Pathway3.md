# [Unit 2] Pathway3

## 리스트

`List`  읽기 전용 목록, 만든 후 수정 불가

`MutableList`  변경 가능한 목록, 수정 가능

→ 둘다 포함 요소 유형을 지정해야 함. (int, string, 클래스...)

```kotlin
val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)
// 변수 유형을 추측 가능하면 데이터 유형 생략 가능
val numbers = listOf(1, 2, 3, 4, 5, 6)

println("List: $numbers")    // List: [1, 2, 3, 4, 5, 6]
println("List: " + numbers)
println("Size: ${numbers.size}")   // 6
println("First element: ${numbers[0]}")
println("Last index: ${numbers.size - 1}")   // 5
println("Last element: ${numbers[numbers.size - 1]}")   // 5번째 요소, 6
println("First: ${numbers.first()}")   // 첫번째 요소, 1
println("Last: ${numbers.last()}")    // 마지막 요소, 6
println("Contains 4? ${numbers.contains(4)}")   // true
println("Contains 7? ${numbers.contains(7)}")   //false  
```

`listOf()`  Kotlin 표준 라이브러리 함수 , 리스트 생성

```kotlin

val colors = listOf("green", "orange", "blue")
colors[0] = "yellow"   // 오류, 변경 불가
println("Reversed list: ${colors.reversed()}")   // Reversed list: [blue, orange, green]
println("List: $colors")      // List: [green, orange, blue]
println("Sorted list: ${colors.sorted()}")    // Sorted list: [blue, green, orange]

val oddNumbers = listOf(5, 3, 7, 1)
println("List: $oddNumbers")    // List: [5, 3, 7, 1]
println("Sorted list: ${oddNumbers.sorted()}")    // Sorted list: [1, 3, 5, 7]
```

`reversed()`  리스트 역순

 `sorted()`  알파벳순

## 변경 가능한 목록

```kotlin
// 다음 두가지 방법으로 선언 가능
val entrees = mutableListOf<String>(
val entrees: MutableList<String> = mutableListOf()

println("Entrees: $entrees")    // Entrees: []

println("Add noodles: ${entrees.add("noodles")}")    // Add noodles: true
println("Entrees: $entrees")    // Entrees: [noodles]

println("Add spaghetti: ${entrees.add("spaghetti")}")    // Add spaghetti: true
println("Entrees: $entrees")    // Entrees: [noodles, spaghetti]

val moreItems = listOf("ravioli", "lasagna", "fettuccine")
println("Add list: ${entrees.addAll(moreItems)}")    // Add list: true
println("Entrees: $entrees")   // Entrees: [noodles, spaghetti, ravioli, lasagna, fettuccine]

println("Remove spaghetti: ${entrees.remove("spaghetti")}")  // Remove spaghetti: true
println("Entrees: $entrees")   // Entrees: [noodles, ravioli, lasagna, fettuccine]

println("없는 요소 삭제: ${entrees.remove("rice")}")  // 없는 요소 삭제 : false
println("Entrees: $entrees")  // Entrees: [noodles, ravioli, lasagna, fettuccine]

println("Remove first element: ${entrees.removeAt(0)}")  // Remove first element: noodles
println("Entrees: $entrees")  // Entrees: [ravioli, lasagna, fettuccine]

entrees.clear()
println("Entrees: $entrees")  // Entrees: []

println("Empty? ${entrees.isEmpty()}")   // Empty? true
```

`add()`  요소를 하나씩 추가

`addAll()`  한 번에 여러 요소를 추가하고 목록을 전달 가능

`remove()`  요소를 삭제, 성공 시 true, 실패시 false 를 반환

`removeAt()`  삭제할 요소의 색인 지정 가능

`clear()`  전체 요소 삭제

`isEmpty()`  목록이 비어있는지 확인 가능

- 리스트를 이용한 while문

```kotlin
val guestsPerFamily = listOf(2, 4, 1, 3)
var totalGuests = 0
var index = 0
while (index < guestsPerFamily.size) {
    totalGuests += guestsPerFamily[index]
    index++
}
println("Total Guest Count: $totalGuests")
```

- 리스트를 이용한 for문

```kotlin
val names = listOf("Jessica", "Henry", "Alicia", "Jose")
for (name in names) {
    println("$name - Number of characters: ${name.length}")
}
```

출력)

Jessica - Number of characters: 7

Henry - Number of characters: 5

Alicia - Number of characters: 6

Jose - Number of characters: 4

```kotlin
open class Item(val name: String, val price: Int)

class Noodles : Item("Noodles", 10) {
    override fun toString(): String {
        return name
    }
}

class Vegetables(vararg val toppings: String) : Item("Vegetables", 5) {
    override fun toString(): String {
        if (toppings.isEmpty()) {
            return "$name Chef's Choice"
        } else {
            return name + " " + toppings.joinToString()
        }
    }
}

class Order(val orderNumber: Int) {
    private val itemList = mutableListOf<Item>()

    fun addItem(newItem: Item): Order {
        itemList.add(newItem)
        return this
    }

    fun addAll(newItems: List<Item>): Order {
        itemList.addAll(newItems)
        return this
    }

    fun print() {
        println("Order #${orderNumber}")
        var total = 0
        for (item in itemList) {
            println("${item}: $${item.price}")
            total += item.price
        }
        println("Total: $${total}")
    }
}

fun main() {
    val ordersList = mutableListOf<Order>()

    val order1 = Order(1)
    order1.addItem(Noodles())
    ordersList.add(order1)

    val order2 = Order(2)
    order2.addItem(Noodles())
    order2.addItem(Vegetables())
    ordersList.add(order2)

    val order3 = Order(3)
    val items = listOf(Noodles(), Vegetables("Carrots", "Beans", "Celery"))
    order3.addAll(items)
    ordersList.add(order3)

    val order4 = Order(4).addItem(Noodles()).addItem(Vegetables("Cabbage", "Onion"))
    ordersList.add(order4)

    ordersList.add(
        Order(5)
            .addItem(Noodles())
            .addItem(Noodles())
            .addItem(Vegetables("Spinach"))
    )

    for (order in ordersList) {
        order.print()
        println()
    }
}
```

`vararg`  동일한 유형의 가변적인 인수 수를 함수나 생성자에 전달 가능

`joinToString()` 요소들을 단일 문자열로 결합. 

`this`  현재 객체 인스턴스를 참조

출력

```
Order #1
Noodles: $10
Total: $10

Order #2
Noodles: $10
Vegetables Chef's Choice: $5
Total: $15

Order #3
Noodles: $10
Vegetables Carrots, Beans, Celery: $5
Total: $15

Order #4
Noodles: $10
Vegetables Cabbage, Onion: $5
Total: $15

Order #5
Noodles: $10
Noodles: $10
Vegetables Spinach: $5
Total: $25
```

## RecyclerView

![Untitled](%5BUnit%202%5D%20Pathway3%2039903ede9f6b45e392314ab4a09ee7d3/Untitled.png)

- **항목** : 표시할 목록의 단일 데이터 항목. 앱의 Affirmation 객체 하나를 나타냄

- **어댑터** : RecyclerView에서 표시할 수 있도록 데이터를 가져와 준비

- **ViewHolder** : RecyclerView 뷰를 만들고 보유

- **RecyclerView** : 화면에 표시되는 뷰, 데이터 목록 표시 가능