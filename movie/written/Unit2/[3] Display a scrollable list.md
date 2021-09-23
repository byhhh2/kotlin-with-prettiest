## Display a scrollable list

<br>

## List

`List` : 만든 후 수정 불가  
`MutableList` : 만든 후 수정 가능

```kotlin
fun main() {
    val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)
    //listOf() : List를 만들어주는 표준 라이브러리 함수
    // == val numbers = listOf(1, 2, 3, 4, 5, 6) 로도 가능 (타입 추측)

    println("List: $numbers")
    // == println("List: " + numbers)

    // size
    println("Size: ${numbers.size}")

    println("First element: ${numbers[0]}")
    println("Second element: ${numbers[1]}")
    println("Last index: ${numbers.size - 1}")
	println("Last element: ${numbers[numbers.size - 1]}")

    // first() , last()
    println("First: ${numbers.first()}")
	println("Last: ${numbers.last()}")

    // contains() : true, false
    println("Contains 4? ${numbers.contains(4)}")
	println("Contains 7? ${numbers.contains(7)}")
}
```

<br>

```kotlin
fun main() {
    val colors = listOf("green", "orange", "blue")

    colors.add("purple") // error
	colors[0] = "yellow" // error
    // 읽기 전용은 수정 불가
}
```

추가 기능

```kotlin
fun main() {
    val colors = listOf("green", "orange", "blue")

    println("Reversed list: ${colors.reversed()}")
    //Reversed list: [blue, orange, green]

	println("List: $colors")
    //List: [green, orange, blue]

    println("Sorted list: ${colors.sorted()}")
    //Sorted list: [blue, green, orange]

    val oddNumbers = listOf(5, 3, 7, 1)

    println("Sorted list: ${oddNumbers.sorted()}")
    //Sorted list: [1, 3, 5, 7]
}
```

## MutableLists

- 수정이 가능한 리스트

<br>

```kotlin
fun main() {
    val entrees = mutableListOf<String>()
    // 수정가능한 List
    // == val entrees: MutableList<String> = mutableListOf()

    // add() : 추가 성공 true, 실패 false
    println("Add noodles: ${entrees.add("noodles")}")
	println("Entrees: $entrees\n")
    println("Add spaghetti: ${entrees.add("spaghetti")}")
	println("Entrees: $entrees\n")

    // addAll() : 한번에 여러 요소 추가
    val moreItems = listOf("ravioli", "lasagna", "fettuccine")
    println("Add list: ${entrees.addAll(moreItems)}")
	println("Entrees: $entrees\n")

    // remove() : 삭제 성공 true, 실패 false
    println("Remove spaghetti: ${entrees.remove("spaghetti")}")
	println("Entrees: $entrees\n")

    // removeAt() : index에 맞춰 삭제, 반환값은 삭제되는 요소
    println("Remove first element: ${entrees.removeAt(0)}")
	println("Entrees: $entrees\n")

    // clear() : 전체 목록 삭제
    entrees.clear()
	println("Entrees: $entrees\n")

    // isEmpty() : 목록이 비어 있는지
    println("Empty? ${entrees.isEmpty()}")
}
```

<br>

## `while` & `for`

```kotlin
//while
while(exp) {

}

//for
for (item in list) print(item) // Iterate over items in a list

for (item in 'b'..'g') print(item) // Range of characters in an alphabet

for (item in 1..5) print(item) // Range of numbers

for (item in 5 downTo 1) print(item) // Going backward

for (item in 3..6 step 2) print(item) // Prints: 35

```

<br>

### 리스트 활용하기

```kotlin
open class Item(val name: String, val price: Int)

class Noodles : Item("Noodles", 10) {
    // kotlin의 모든 클래스는 toString() 메서드를 상속
    override fun toString(): String {
       return name
   }
}

class Vegetables(vararg val toppings: String) : Item("Vegetables", 5) {
    //vararg = 가변인수

    override fun toString(): String {
        if (toppings.isEmpty()) {//토핑이 비어있으면 chef's choice
            return "$name Chef's Choice"
        } else {
            return name + " " + toppings.joinToString()
        }// joinToString() : 모든 토핑을 단일 문자열로 결합
	}
}

class Order(val orderNumber: Int) {
    private val itemList = mutableListOf<Item>()

   fun addItem(newItem: Item): Order {
       itemList.add(newItem)
       return this // this를 반환함으로써 Order 객체 반환
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
    val ordersList = mutableListOf<Order>() //주문 목록 추적

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

    //빌더 패턴
    val order4 = Order(4).addItem(Noodles()).addItem(Vegetables("Cabbage", "Onion"))
	ordersList.add(order4)

    ordersList.add(
    Order(5)
        .addItem(Noodles())
        .addItem(Noodles())
        .addItem(Vegetables("Spinach")))

    for (order in ordersList) {
        order.print()
        println()
    }
}
```

<br>
<br>
<br>

## 패키지

<br>

### 패키지 이름 지정

- 소문자로 표기하고 마침표로 구분
- 마침표는 이름의 일부일뿐 계층 구조를 나타내지 않음
  - 실제 코드에 계층구조가 없다

사용하는 이유

- 코드를 여러 부분 별로 다른 패키지로 나눔
- 코드에 있는 다른 패키지의 코드를 사용 (`import`)

<br>

### 패키지 만들기

`(android) app > java > com.example.affirmations > 마우스 우클릭 > New > Package`

<br>

1. 모델 `com.example.affirmations.model`

   - 데이터를 모델링하거나, 표현하는 클래스의 패키지 이름을 보통 model이라고 함
   - model 패키지 안에 `Affirmation` 데이터 클래스 생성

2. 데이터 소스 `com.example.affirmations.data`
   - data 패키지 안에 `Datasource` 클래스 생성
     - `Datasource` 클래스는 앱의 데이터를 준비함 (불필요한 형식 -> 필요한 형식)

<br>

### `RecyclerView`

- `ConstraintLayout`은 뷰를 여러개 배치할 때 사용, `RecyclerView` 하나만 배치할 경우 더 간단한 `ViewGroup`인 `FrameLayout`으로 전환
- 항목 정렬은 `LayoutManager`에서 처리
  - 세로로 항목 표시 `LinearLayoutManager`
- 세로 스크롤바 `android:scrollbars="vertical"`

<br>

### `RecyclerView`용 어댑터

- Datasource에서 Affirmation가져와서 RecyclerView의 항목으로 표시해야 하는데 Affirmation 항목 뷰로 전환하는 어댑터가 필요하다.
- 항목의 레이아웃 : `list_item.xml` -> `<TextView>`

<br>

Adapter class 만들기

```kotlin
package com.example.affirmations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

// 추상클래스 RecyclerView.Adapter, 뷰 홀더 유형 ItemAdapter.ItemViewHolder
// 추상클래스의 구현해야 하는 클래스 알려면 ctrl + i (itemAdapter에 마우스)
class ItemAdapter(private val context: Context, private val dataset: List<Affirmation>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    // context : 문자열 리소스 확인 정보, 기타 앱 관련 정보
    // ViewHolder : 항목 레이아웃 안 개별 뷰 참조, 뷰를 효율적으로 이동하기 위한 정보

    //중첩클래스, ItemAdapter만 ItemViewHolder를 사용
    //ItemViewHolder는 ViewHolder의 서브클래스
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        // list_item.xml에서의 item
    }

    //RecyclerView의 새 뷰 홀더를 만들기 위해 레이아웃 관리자가 호출 (기존 뷰 홀더가 없는 경우)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // onCreateViewHolder() 메서드는 두 매개변수를 사용하며 새 ViewHolder를 반환
        // parent : (상위 요소 = RecyclerView) 새 목록 항목 뷰가 하위요소로 사용되어 연결되는 뷰 그룹
        // viewType : RecyclerView에 항목 뷰 유형이 여러개 있을 때 (같은 뷰로 이뤄지지 않은)
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        //LayoutInflater : xml 레이아웃을 뷰 객체의 계층 구조로 확장하는 방법을 앎..
        //inflage() 메서드를 호출하면, apapterLayout은 목록 항목 뷰의 참조를 보유

        return ItemViewHolder(adapterLayout)
    }

    // 목록 항목 뷰의 콘텐츠를 바꾸기 위해 레이아웃 관리자가 호출
    // 위치를 기반으로 데이터 세트에서 올바른 Affirmation 객체를 찾는다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //holder : onCreateViewHolder()에서 생성된 ItemViewHolder
        //position : 목록에서 현재 항목의 position을 나타냄

        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.stringResourceId)
        //context.resources.getString(<문자열 리소스 ID>)
    }

    override fun getItemCount() = dataset.size
    // 데이터 세트의 크기 반환
    // = (==) return

}

```

<br>
<br>
<br>

## 정리

![image](https://user-images.githubusercontent.com/52737532/134493224-54f6ed65-0821-4e0d-beed-e539e38b31a0.png)

- `item` : `Affirmation` 객체
- `adapter` : `RecyclerView`에 표시할 수 있도록 데이터를 가져와 준비
  - `Affirmation` 인스턴스를 가져와 목록 항목 뷰로 전환하는 어댑터
- `ViewHolder` : `RecyclerView` 용 뷰 풀
- `RecyclerView` : 화면에 표시되는 뷰
  - `LayoutManager` : `RecyclerView`의 item 정렬

<br>
<br>

- `RecyclerView` 위젯을 사용하여 데이터 목록을 표시할 수 있습니다.
- `RecyclerView`는 어댑터 패턴을 사용하여 데이터를 조정하고 표시합니다.
- `ViewHolder`는 `RecyclerView`의 뷰를 만들고 보유합니다.
- `RecyclerView`는 내장된 `LayoutManagers`와 함께 제공됩니다. `RecyclerView`는 항목을 배치하는 방식을 `LayoutManagers`에 위임합니다.

어댑터 구현

- 어댑터의 새 클래스(예: `ItemAdapter`)를 만듭니다.
- 단일 목록 항목 뷰를 나타내는 맞춤 `ViewHolder` 클래스를 만듭니다. `RecyclerView.ViewHolder` 클래스에서 확장합니다.
- `ItemAdapter` 클래스를 수정하여 `RecyclerView.Adapter` 클래스에서 확장합니다(맞춤 `ViewHolder` 클래스 사용).
- 어댑터 내에서 `getItemsCount(), onCreateViewHolder(), onBindViewHolder()` 메서드를 구현합니다.
