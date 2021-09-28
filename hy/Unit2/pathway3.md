### 목록유형
* `List` : 읽기 전용 목록. 수정 x
* `MutableList` : 변경 가능한 목록.


<br><br>

## List 
### List 생성
```kotlin
val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)
val colors = listOf("green", "black", "red")
```


<br><br><br>

### List 오프셋

![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-lists/img/cb6924554804458d.png?hl=ko)

```kotlin
println("First element: ${numbers[0]}")     //1
println("Second element: ${numbers[1]}")    //2
```

<br><br><br>

### List 메서드
* first() / last()
    ```kotlin
    println("First: ${numbers.first()}")    //1   
    println("Last: ${numbers.last()}")      //6
    ```

* contains()
    ```kotlin
    println("Contains 4? ${numbers.contains(4)}")   //true
    println("Contains 7? ${numbers.contains(7)}")   //false
    ```

* reversed() / sorted()
    ```kotlin
    println("Reversed list: ${colors.reversed()}")      //[red, black, green]
    println("Sorted Colors: ${colors.sorted()}") 		//[black, green, red]
    ```

<br>
<hr>
<br>

## MutableList
### MutableList 생성
```kotlin
val entrees : MutableList<String> = mutableListOf()
val entrees = mutableListOf<String>()
```

<br><br><br>

### MutableList 메서드
* add()
  리스트에 같은 유형의 데이터만 추가해야함. 
    ```kotlin    
    println("Add noodles: ${entrees.add("noodles")}")	 //Add noodles: true
    entrees.add("spaghetti")                             //[noodles, spaghetti]

    val moreItems = listOf("ravioli", "lasagna", "fettuccine")
    entrees.addAll(moreItems)                            //[noodles, spaghetti, ravioli, lasagna, fettuccine]
    ```

* remove()
    ```kotlin
    println("Remove spaghetti: ${entrees.remove("spaghetti")}") //Remove spaghetti: true
    println("Entrees: $entrees")								//[noodles, ravioli, lasagna, fettuccine]

    println("Remove rice: ${entrees.remove("rice")}") 			//Remove rice: false
    println("Entrees: $entrees")								//[noodles, ravioli, lasagna, fettuccine]
    
    println("Remove first element: ${entrees.removeAt(0)}")		//Remove first element: noodles
    println("Entrees: $entrees")								//[ravioli, lasagna, fettuccine]
    ```

* clear()
    ```kotlin
    entrees.clear()
    println("Entrees: $entrees")        //Entrees: []
    ```

* isEmpty()
    ```kotlin
    println("Empty? ${entrees.isEmpty()}")	//Empty? true
    ```
 

<br><br><br>

### 목록 반복
* `while` 루프
    ```kotlin
    while (expression) {
        // While the expression is true, execute this code block
    }
    ```


* `for`루프
    ```kotlin
    for (number in numberList) {
        // For each element in the list, execute this code block
    }   
    ```

    ```kotlin
    val names = listOf("Kim", "Park", "Jung", "Lee")
    for(name in names){
        println("$name Number of name characters : ${name.length}")
    }
    
    for (item in 'b'..'g') print(item)		//bcdefg
    for (item in 1..5) print(item)			//12345
    for (item in 5 downTo 1) print(item)	//54321
    for (item in 3..6 step 2) print(item)	//35
    ```

<br><br><br>

### 클래스 상속 `toString()` 메서드 재정의
1. 객체 인스턴스를 출력하면 객체의 toString() 메서드가 호출됨
2. Kotlin에서는 모든 클래스가 자동으로 toString() 메서드를 상속
3. 이 메서드의 기본 구현에서는 인스턴스의 메모리 주소가 있는 객체 유형을 반환
4. toString() 재정의

* toString() 재정의 전
    ```kotlin
    open class Item(val name: String, val price: Int)
    class Noodles : Item("Noodles", 10)

    fun main() {
        val noodles = Noodles()
        println(noodles)            //출력 : Noodles@5451c3a8
    }
    ```
* toString() 재정의 후
    ```kotlin
    class Noodles : Item("Noodles", 10) {
        override fun toString(): String {
            return name
        }
    }

    fun main() {
        val noodles = Noodles()
        println(noodles)            //출력 : Noodles
    }
    ```


<br><br><br>

### `vararg` 가변인자
* 함수, 생성자 호출 시 동일한 유형의 인자 개수를 유동적으로 지정 가능
* 매개변수 하나만 `vararg`로 표시 가능하며 일반적으로 마지막 매개변수로 지정

    ```kotlin
    class Vegetables(vararg val toppings: String) : Item("Vegetables", 5) {
        override fun toString():String{
            if (toppings.isEmpty()){
            	return "$name Chef's Choice"
            }
            else return name + " " + toppings.joinToString()
    }

    fun main() {
        ...
        val vegetables = Vegetables("Cabbage", "Sprouts", "Onion")
        ...
    }
    ```
 
<br><br><br>

### 빌더 패턴
단계별 접근 방식으로 복잡한 객체를 빌드할 수 있는 디자인 패턴
```kotlin
class Order(val orderNum : Int){
    var itemList = mutableListOf<Item>()
    
    fun addItem(newItem: Item): Order {
        itemList.add(newItem)
        return this             //현재 객체 인스턴스 반환
    }
    ...
}

fun main(){
    ... 
    val orderList = mutableListOf<Order>()
    ...

    // addItem 메서드가 Order 인스턴스를 반환하면 다시 이 인스턴스로 메서드 호출 가능
    // 반환된 결과는 order4에 저장됨
    var order4 = Order(4).addItem(Noodles()).addItem(Vegetables2("Bean","Onion"))
    orderList.add(order4)
    
    // 인스턴스를 변수에 저장하지 않고 사용 가능
    orderList.add( Order(5).addItem(Noodles()).addItem(Noodles()).addItem(Vegetables2("Spinach")) )
}
```

<br><br><br>

### 패키지
![이미지](https://acaroom.net/sites/default/files/uploads/Project_Module_Package_0.png)
* 이름 지정
  1. 다른 패키지의 이름과 중복 x
  2. 일반에서 구체적인 순서로 구성. 이름 각 부분을 소문자로 표기하고 마침표로 구분
  3. 이름 첫 부분에 개발자 도메인, 비즈니스 도메인 사용
  4. 패키지에 포함된 내용 및 관계 표시


<br><br><br>

### RecyclerView
![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/4e9c18b463f00bf7.png?hl=ko)
* Item : 표시할 목록의 단일 데이터 항목
* Adapter : RecyclerView에서 표시할 수 있도록 item을 가져와 준비함
* ViewHolder : 사용 및 재사용을 위한 뷰 모음
* RecyclerView : 화면에 표시되는 뷰


<br><br>

#### [1] RecyclerView 생성
1. app > res > layout > activity_main.xml 에서 TextView 삭제
2. `ConstraintLayout`을 지우고 `FrameLayout`으로 변경
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
    </FrameLayout>
    ```
3. Design 뷰 > Palette > Containers > RecyclerView 선택 후 레이아웃으로 드래그
4. RecyclerView의 리소스 id를 `recycler_view`로 설정
5. RecyclerView 요소 내부에 `layoutManager` 추가 (LinearLayoutManager, GridLayoutManager .. )
    ```xml
    app:layoutManager="LinearLayoutManager"
    ```
6. RecyclerView 요소 내부에 `scrollbars` 추가 (vertical, horizental)
   ```xml
   android:scrollbars="vertical"
   ```


<br><br>

### Adapter 
: 데이터를 RecyclerView에서 사용할 수 있는 형식으로 조정하는 설계 패턴
1. 앱을 실행하면 RecyclerView가 Adapter를 사용하여 화면에 데이터를 표시하는 방법을 파악
2. RecyclerView는 목록의 첫 번째 데이터 항목을 위한 새 목록 항목 뷰를 만들도록 Adapter에 요청
3. 뷰가 생성된 후에는 항목을 그리기 위한 데이터를 제공하도록 Adapter에 요청
4. 2~3 프로세스를 RecyclerView에 화면을 채울 뷰가 더 이상 필요하지 않을 때까지 반복

<br><br>

#### [2] Adapter 생성
1. res > layout에서 list_item.xml 빈 파일 생성 후 `TextView` 추가 
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <TextView xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/item_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
    ```
2. app > java > com.example.affirmations 에서 `adapter` 패키지 생성
3. `adapter` 패키지에서 `ItemAdapter.kt` 생성
4. 아이템 목록 `dataset`, 문자열 리소스를 확인하는 정보 `context` 를 ItemAdapter의 매개변수로 추가
   ```kotlin
    class ItemAdapter(private val context: Context, private val dataset: List<Affirmation>) { }
    ```

<br><br>

#### [3] ViewHolder 생성
: ViewHolder 인스턴스는 목록 항목 레이아웃 안에 개별 뷰의 참조를 보유

1. `ItemAdapter` 클래스 내부에서 `ItemViewHolder` 클래스 생성
2. `View`형의 `view` 매개변수 추가
3. `ItemViewHolder`를 `view`를 매개변수로 가지는 RecyclerView.ViewHolder의 서브클래스로 변경
4. `ItemViewHolder` 클래스 내부에 TextView형의 textView 정의 후 list_item.xml에서 정의한 `item_title`뷰 할당
    ```kotlin
    class ItemAdapter(private val context: Context, private val dataset: List<Affirmation>) {

        class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.item_title)
        }
    }
    ```


<br><br>

#### [4] Adapter 메서드 재정의 

```kotlin
class ItemAdapter(
    private val context: Context, 
    private val dataset: List<Affirmation>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.stringResourceId)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}
```


1. 추상 클래스 `RecyclerView.Adapter`에서 `ItemAdapter`를 확장하는 코드 추가 후 꺾쇠 괄호(<>) 안에 ViewHolder유형으로 `ItemAdapter.ItemViewHolder` 지정
2. ItemAdapter에 커서를 놓고 Control+I 를 눌러 구현해야 하는 메서드의 목록 확인 후 모두 추가<br>
   getItemCount(), onCreateViewHolder(), onBindViewHolder()
3. getItemCount() 구현 : 데이터세트의 크기 반환 <br>
4. onCreateViewHolder() 구현 : RecyclerView의 새 뷰 홀더를 만들기 위해 레이아웃 매니저가 호출함
    * parent 매개변수 : 새 목록 항목 뷰가 하위 요소로 사용되어 연결되는 뷰 그룹
    * viewType 매개변수 : 동일한 RecyclerView에 항목 뷰 유형이 여러 개 있을 때 중요. 동일한 항목 뷰 유형을 가진 뷰만 재활용 가능. 
    
    1 ) onCreateViewHolder()의 매개변수 parent의 context에서 LayoutInflater 인스턴스를 가져옴 <br>
    2 ) LayoutInflater 객체 인스턴스 뒤 inflate 메서드로 실제 목록 항목 뷰 확장<br>
    3 ) 루트 뷰가 adapterLayout인 새 ItemViewHolder 인스턴스를 반환<br>

5. onBindViewHolder() 구현 : 목록 항목 뷰의 콘텐츠를 바꾸기 위해 레이아웃 매니저가 호출함
6. RecyclerView를 사용하도록 MainActivity.kt 수정

    ```kotlin
    class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Initialize data.
            val myDataset = Datasource().loadAffirmations()

            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            recyclerView.adapter = ItemAdapter(this, myDataset)

            // Use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true)
        }
    }
    ```


<br><br><br>

### 리소스 주석
클래스, 메서드, 매개변수에 부가적인 정보 추가 (ex : @StringRes)
```kotlin
data class Affirmation(
   @StringRes val stringResourceId: Int,
   @DrawableRes val imageResourceId: Int
)
```



 
<br><br><br>
<hr>
<br>

### 리스트, 반복문
```kotlin
fun main() {

    // Int List
// 	val numbers : List<Int> = listOf(1,2,3,4,5,6) 
    val numbers = listOf(1,2,3,4,5,6)
	
    println("List : $numbers")   //List :[1, 2, 3, 4, 5, 6]
	println("List : " + numbers)
    println("Size : ${numbers.size}")
   
    println("First element : ${numbers[0]}")
    println("Last element : ${numbers[numbers.size - 1]}")
    println("First : ${numbers.first()}")
	println("Last : ${numbers.last()}")
    
    println("Contain 4? : ${numbers.contains(4)}")
    println("Contain 7? : ${numbers.contains(7)}")
    

    // String List
    val colors = listOf("green", "black","red")
//     // 리스트 변경 불가능. 오류
//     colors.add("purple")
//     colors[0] ="white"

    println("Reversed Colors : ${colors.reversed()}")   //[red, black, green]
    println("Sorted Colors : ${colors.sorted()}") 		//[black, green, red]
    println("Colors : ${colors}")                       //[green, black, red]
    


    // MutableList
//     val entrees : MutableList<String> = mutableListOf()
	val entrees = mutableListOf<String>()
    
    // add method
    println("Add noodles: ${entrees.add("noodles")}")		//Add noodles: true
	println("Entrees: $entrees")
    
    entrees.add("spaghetti")
    println("Entrees: $entrees")
    
    val moreItems = listOf("ravioli", "lasagna", "fettuccine")
	entrees.addAll(moreItems)
    println("Entrees: $entrees")
    
//   //오류. 올바른 데이터 유형만 추가해야함
//     entrees.add(10)
    
    // remove method
    println("Remove spaghetti: ${entrees.remove("spaghetti")}") //Remove spaghetti: true
	println("Entrees: $entrees")								//[noodles, ravioli, lasagna, fettuccine]
    println("Remove rice: ${entrees.remove("rice")}") 			//Remove rice: false
	println("Entrees: $entrees")								//[noodles, ravioli, lasagna, fettuccine]
    
    println("Remove first element: ${entrees.removeAt(0)}")		//Remove first element: noodles
	println("Entrees: $entrees")								//[ravioli, lasagna, fettuccine]
    
    entrees.clear()
	println("Entrees: $entrees")
    
    // isEmpty method
    println("Empty? ${entrees.isEmpty()}")						//Empty? true
    

    
    // 목록 반복
    // while
    val guestPerFamily = listOf(2,4,1,3)
    var totalGuest = 0
    var index =0
    while(index < guestPerFamily.size) {
        totalGuest += guestPerFamily[index]
        index++
    }
    
    println("Total Guest : ${totalGuest}")
    
    // for
    val names = listOf("Kim", "Park", "Jung", "Lee")
    for(name in names){
        println("$name Number of name characters : ${name.length}")
    }
    
    for (item in 'b'..'g') print(item)		//bcdefg
    for (item in 1..5) print(item)			//12345
    for (item in 5 downTo 1) print(item)	//54321
    for (item in 3..6 step 2) print(item)	//35


}
```
<br><br><br> 
<hr><br>

### 음식 주문 목록

```kotlin 
   open class Item(val name: String, val price: Int)

class Noodles : Item("Noodles", 10){
    override fun toString():String{
        return name
    }
}

class Vegetables1(val toppings :List<String>) : Item("Vegetables1", 5){
    override fun toString():String{
        return name
    }
}

class Vegetables2(vararg val toppings: String) : Item("Vegetables2", 5) {
        override fun toString():String{
            if (toppings.isEmpty()){
            	return "$name Chef's Choice"
            }
            else return name + " " + toppings.joinToString()
    }
}

class Order(val orderNum : Int){
    var itemList = mutableListOf<Item>()
    
    fun addItem(newItem : Item) :Order {
        itemList.add(newItem)
        return this
    }
    
    fun addAll(newItems : List<Item>) : Order{
        itemList.addAll(newItems)
        return this
    }
    
    fun print(){
    	var totalPrice :Int = 0
        
        println("Order #${orderNum}")
        for (item in itemList){
            println("$item : $${item.price}")
            totalPrice += item.price
        }
        println("Total : $$totalPrice")
    }
} // class Order

fun main() {
    val noodles = Noodles()

    val vegetables1 = Vegetables1(listOf("Cabbage", "Sprouts", "Onion"))
    val vegetables2 = Vegetables2("Cabbage", "Sprouts", "Onion")
    val vegetables3 = Vegetables2()
    println(noodles)
    println(vegetables1)
    println(vegetables2)
    println(vegetables3)
    
    val orderList = mutableListOf<Order>()
    
    println("-------------------------")
    var order1 = Order(1)
    order1.addItem(noodles)
    orderList.add(order1)
    
    var order2 = Order(2)
    order2.addItem(Noodles())
    order2.addItem(Vegetables2())
    orderList.add(order2)
    
    var order3 = Order(3)
    var items = listOf(Noodles(), Vegetables2("Carrot","Bean","Onion"))
    order3.addAll(items)
    orderList.add(order3)
    
    // 빌더 패턴
    var order4 = Order(4).addItem(Noodles()).addItem(Vegetables2("Bean","Onion"))
    orderList.add(order4)
    
    orderList.add( Order(5).addItem(Noodles()).addItem(Noodles()).addItem(Vegetables2("Spinach")) )

    for (order in orderList){
        order.print()
    	println("-------------------------")
    }
    
}
```



<br><br><br>
<hr>
<br>


### <퀴즈>
* RecyclerView에 Adapter가 필요한 이유<br>
a. 새로운 ViewHolders를 만들고 데이터를 바인딩하기 위해 <br>


* RecyclerView의 장점 <br>
a. RecyclerView는 기본 제공 레이아웃 관리자와 함께 제공됨 <br>
a. 처리 시간을 절약해 목록을 부드럽게 스크롤 가능<br>
a. 화면 밖으로 넘어간 뷰를 다시 사용하여 목록의 효율성을 높임<br>




