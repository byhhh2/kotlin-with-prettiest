# Advanced navigation app examples

## Cupcake app

- 사용자는 주문시 수량과 맛, 기타 옵션 선택 가능

<br>
<br>

## 시작 코드 살펴보기

<br>

#### MainActivity

- 컨텐츠 뷰를 `activity_main.xml`로 설정
- 레이아웃 확장을 변수화된 생성자 `AppCompatActivity(@LayoutRes int contentLayoutId)` 사용

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main)
```

```kotlin
class MainActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.main_activity)
   }
}
```

- 둘은 같은 의미의 코드

<br>

### layout

- fragment_start.xml : 컵케이크 주문 갯수 버튼
- fragment_flavor.xml : 맛 목록 라디오 버튼, Next 버튼
- fragment_pickup.xml : 수령일 선택, Next 버튼
- fragment_summary.xml : 주문 세부 정보 요약 표시

<br>

### layout

- StartFragment.kt : 앱에 표시되는 첫번째 화면
- FlavorFragment.kt
- PickupFragment.kt
- SummaryFragment.kt

<br>

### res

- navigation/nav_graph.xml : 아직 작업이 없는 fragment 대상이 있음

<br>
<br>
<br>

---

## 탐색 그래프 완성시키기

<br>

- <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit3/%5B2%5D%20Introduction%20to%20the%20Navigation%20component.md#%EC%A2%85%EC%86%8D">프로젝트 설정</a>

<br>

1. `nav_graph.xml`에서 fragment 연결 해주기

   - 마우스로 연결해줄 것
   - `start` -> `flavor` -> `pickup` -> `summary`

   <br>

   ![image](https://user-images.githubusercontent.com/52737532/136061711-bb1d22fc-c7af-4f4f-bff6-1a2ee9491aa7.png)

2. 첫번째 NavHost에 표시될 fragment 표시

   - 우클릭 > `set as start destination`

   <br>

3. 버튼에 연결된 `setOnClickListener`에 연결된 함수에서 navigatie를 해준다.

```kotlin
//StartFragment.kt

import androidx.navigation.fragment.findNavController

orderOneCupcake.setOnClickListener { orderCupcake(1) }

...

fun orderCupcake(quantity: Int) {
   findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
   // action id 값 전달
}
```

<br>
<br>

### 앱 바 (앱 위에 있는 상단바) 제목 업데이트

- `NavController` 를 사용해서 앱바 제목, 뒤로가기 버튼 표시하기

<br>

1. `MainActivity.kt`에서 `onCreate()` 재정의

   ```kotlin
   //MainActivity.kt

   ..
   import androidx.navigation.ui.setupActionBarWithNavController

   class MainActivity : AppCompatActivity(R.layout.activity_main) {

       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)

           val navHostFragment = supportFragmentManager
                   .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
           val navController = navHostFragment.navController

           setupActionBarWithNavController(navController) //[1]
       }
   }
   ```

   - [1] : 대상의 label (xml 파일에 있음)을 기반으로 앱 바에 제목이 표시되고, 최상위 대상이 아니면 뒤로가기 버튼이 표시된다.

<br>

2. `nav_graph.xml`에서 label값 변경
   - `android:label="@string/choose_flavor"`

<br>
<br>

### 공유 ViewModel 만들기

- 앱의 데이터를 단일 ViewModel에 저장한다.
  - 주문 수량, 맛, 날짜, 가격

<br>

> **권장사항**
>
> ViewModel의 데이터를 `public`으로 노출하지 않을 것  
> 예상치 못한 방식으로 수정되는 것을 방지  
> `private` 속성 앞에 `_`을 붙이자

<br>

1. `model`이라는 새 패키지 만들기
   - `com.example.- > new > package`
   - 이름은 model로

<br>

2. model 우클릭 해서 kotlin file 추가 => `OrderViewModel`
   - `ViewModel()`를 상속받도록 하기

<br>

```kotlin
class OrderViewModel : ViewModel() {
    private val _quantity = MutableLiveData<Int>(0)
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>("")
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>("")
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>(0.0)
    val price: LiveData<Double> = _price

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
    }

    //kotlin 기본 한정자 public
}
```

- `LiveData`로 지정해서 소스데이터가 변경될 때 UI 업데이트 할 수 있도록

  > <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit3/%5B3%5D%20Architecture%20components.md#live-data">LiveData</a>

- setter는 public 데이터는 private

<br>
<br>

### fragment 간 공유되는 ViewModel

- ViewModel을 여러 fragment 간에 공유할 수 있다.

<br>

> **대리자 클래스 바꾸기**
>
> - `viewModels()` : 현재 fragment로 범위가 지정된 ViewModel 인스턴스 제공, 인스턴스는 fragment마다 다르다.
> - `activityViewModels()` : 현재 activity로 지정된 `ViewModel` 인스턴스를 제공, activity의 여러 fragment 간에 동일하게 유지

<br>
<br>

> **속성 위임 사용**
>
> var에는 getter와 setter, val에는 getter함수가 자동으로 존재한다. 속성 위임을 하면 getter-setter책임을 대리자 클래스에 넘길 수있다.  
> `var <property-name> : <property-type> by <delegate-class>()`  
> `private val sharedViewModel: OrderViewModel by activityViewModels()`

<br>

- 사용

```kotlin
fun orderCupcake(quantity: Int) {
    sharedViewModel.setQuantity(quantity)

    ...
}
```

<br>
<br>
<br>

### Data binding + ViewModel

- <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit3/%5B3%5D%20Architecture%20components.md#%EB%B7%B0-%EA%B2%B0%ED%95%A9%EA%B3%BC-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EA%B2%B0%ED%95%A9">데이터 바인딩</a>

<br>

1. flavor, pickup, summary의 xml 파일에서 `<data>` 태그 추가

   ```xml
       <layout ...>

            <data>
                <variable
                    name="viewModel"
                    type="com.example.cupcake.model.OrderViewModel" />
            </data>

            <ScrollView ...>

   ...

   ```

- 얘가 공유 `ViewModel`

<br>

2. fragment에 binding에 ViewModel 추가하기 <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit3/%5B3%5D%20Architecture%20components.md#%EB%B7%B0-%EA%B2%B0%ED%95%A9---%EB%8D%B0%EC%9D%B4%ED%84%B0-%EA%B2%B0%ED%95%A9">이 부분에서 초기화</a>

   ```kotlin
       binding?.apply { //binding == fragmentBinding
           viewModel = sharedViewModel
           ...
       }

   ```

<br>

> **apply**
>
> - 범위 함수
> - 객체의 컨텍스트 내에서 코드블록을 실행하며 임시 범위를 형성
> - 이름을 사용하지 않고 객체에 엑세스 가능

- 예시

```kotlin
clark.apply {
    firstName = "Clark" //clark. 없이 접근 가능
    lastName = "James"
    age = 18
}

// The equivalent code without apply scope function would look like the following.

clark.firstName = "Clark"
clark.lastName = "James"
clark.age = 18
```

<br>

3. 라디오 버튼에서 flavor 변수 연결

```xml
<RadioGroup
   ...>

   <RadioButton
       android:id="@+id/vanilla"
       ...
       android:checked="@{viewModel.flavor.equals(@string/vanilla)}" [1]
       android:onClick="@{() -> viewModel.setFlavor(@string/vanilla)}" [2]
       .../>
```

- [1] : 만약 버튼에 써진 값이 flavor 변수에 저장된 값과 같으면 라디오 버튼을 선택된 상태로
- [2] : 라디오 버튼이 클릭되면 flavor값을 버튼에 써진 값으로

<br>
<br>
<br>

### 시간 설정

---

#### `SimpleDateFormat` 클래스

- 날짜 -> 텍스트, 텍스트 -> 날짜 파싱 가능
- 날짜 형식을 지정하고 파싱하는 클래스

<br>

예시

`SimpleDateFormat("E MMM d", Locale.getDefault())` // 인스턴스 생성

- `"E MMM d"` : 날짜 시간 형식
- `Locale.getDefault()` : 사용자 기기에 설정된 언어정보를 SimpleDateFormat 생성자에 전달

---

<br>

### `Calendar`

- 현재 날짜 및 시간 포함
- `val calendar = Calendar.getInstance()`

<br>

1. 픽업 날짜 4개의 옵션 만들기
   - 날짜는 오늘~3일 후까지

```kotlin
//OrderViewModel.kt


import java.text.SimpleDateFormat
import java.util.Locale

private fun getPickupOptions(): List<String> {
   val options = mutableListOf<String>()

   val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
   val calendar = Calendar.getInstance()

   repeat(4) {
       options.add(formatter.format(calendar.time))
       calendar.add(Calendar.DATE, 1)
       //캘린더를 1씩 증가
   }

   return options
}

val dateOptions = getPickupOptions()
```

<br>

2. 라디오 버튼에 옵션 달아주기

```kotlin
<RadioButton
   android:id="@+id/option0"
   ...
   android:checked="@{viewModel.date.equals(viewModel.dateOptions[0])}"
   android:onClick="@{() -> viewModel.setDate(viewModel.dateOptions[0])}"
   android:text="@{viewModel.dateOptions[0]}"
   ...
   />
```

<br>
<br>

### 가격계산

1. 가격 계산 함수 짜기

- 컴케이크 가격은 2이다.
- 당일 수령시 추가금액 3이 있다.

```kotlin
//OrderViewModel.kt

private const val PRICE_PER_CUPCAKE = 2.00

class OrderViewModel : ViewModel() {
    ...

    private fun updatePrice() {
        _price.value = (quantity.value ?: 0) * PRICE_PER_CUPCAKE //[1]
    }

```

<br>

---

### elvis 연산자 (`?:`)

- `quantity.value`가 `null`이면 뒤의 값을 사용하고, `null`이 아니면 `quantity.value`값을 사용

![image](https://user-images.githubusercontent.com/52737532/136087077-8dff99a6-3e58-4921-9f63-76de4e97a698.png)

---

<br>

2. 당일 수령시 추가요금 청구하기

```kotlin
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

private fun updatePrice() {
    var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE

    if (dateOptions[0] == _date.value) {
        calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
    }

    _price.value = calculatedPrice
}
```

- 수령 날짜를 변경해도 요금이 바뀌지 않는다.  
  -> **이는 ViewModel에서 가격이 바뀌었지만 레이아웃에 알려지지 않았기 때문**

<br>

### LiveData를 관찰하려면 ? = `LifecycleOwner`

- <a href="https://github.com/byhhh2/kotlin-with-prettiest/blob/main/movie/written/Unit3/%5B3%5D%20Architecture%20components.md#live-data">Live Data</a>
- Live Data의 변경 사항을 관찰하려면 `LifecycleOwner`를 사용해야한다.

```kotlin
binding?.apply {
    lifecycleOwner = viewLifecycleOwner
    ...
}
```

- 수명 주기 소유자를 설정하면 앱이 LiveData 객체를 관찰할 수 있다.

<br>
<br>

### 가격 형식 지정

- 나라에 맞는 가격으로 형식을 지정하자.

```kotlin
private val _price = MutableLiveData<Double>()
val price: LiveData<String> = Transformations.map(_price) {
   NumberFormat.getCurrencyInstance().format(it)
}
```

- `Transformations.map()` : 가격이 현지 통화를 사용하도록 가격 형식 지정

<br>
<br>

### clickListener를 xml에 하기

1. fragment 변수를 xml에 추가한다.

   ```xml
    <variable
            name="startFragment"
            type="com.example.cupcake.StartFragment" />
   ```

<br>

2. fragment를 바인딩에 추가한다.

   ```kotlin
       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)
           binding?.startFragment = this
       }
   ```

   - apply안에 존재 한다면

   ```kotlin
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            pickupFragment = this@PickupFragment
        }
   ```

   - **this가 binding 객체를 참조하므로 `@`를 사용하여 fragment를 명시해준다.**

<br>

3. xml에서 fragment를 불러와서 onClick시 함수에 연결

   ```xml
    <Button
        android:id="@+id/order_one_cupcake"
        android:onClick="@{() -> startFragment.orderCupcake(1)}"
        ... />
   ```

<br>
<br>
<br>

## 뒤로가기 버튼 구현하기 (= up 버튼)

```kotlin
//MainActivity

override fun onSupportNavigateUp(): Boolean {
   return navController.navigateUp() || super.onSupportNavigateUp()
}
```

- `navController.navigateUp()` 로 뒤로 가기

<br>
<br>
<br>

## Back stack 알아보기

- 앱의 주문 흐름안에 cancel버튼을 추가해서 cancel버튼을 클릭하면 언제든지 주문을 취소하여 초기화할 수 있도록
- 주문을 취소하면 `StartFragment`로 이동한다.

<br>

---

### Tasks 작업

- 작업은 사용자가 이메일 확인, 컵케이크 주문생성 등 특정한 일을 할 때 상호작용하는 activity의 모음
- tasks는 back stack으로 배열 됨
- stack 맨 위에 있는 activity는 사용자가 현재 상호작용하는 활동
- 이 아래에 있는 활동들은 백그라운드

![image](https://user-images.githubusercontent.com/52737532/136175197-96e57f3d-51b4-4eab-a4ed-325942c9a96c.png)

- 사용자가 뒤로가기를 누르면 스택 맨위의 activity를 pop하고 그 아래에 있느 activity를 다시 시작한다.
- 이전 activity가 포그라운드로 이동
- back stack에서 더 이상 활동이 없으면 홈 화면으로 돌아감

---

<br>
<br>
<br>

1. 앱 처음 실행 (`MainActivity`)

![image](https://user-images.githubusercontent.com/52737532/136175678-6d7dd14e-16bf-4f46-9703-6acc917ef649.png)

<br>

2. 글자 버튼을 클릭 (`DetailActivity`)

![image](https://user-images.githubusercontent.com/52737532/136175868-4ddf3992-d646-4a89-86e6-54083bcd34a2.png)

<br>

3. 뒤로가기 버튼 누름

![image](https://user-images.githubusercontent.com/52737532/136176015-9347f784-b24f-414e-9996-b17030f5d04b.png)

4. `MainActivity`가 포그라운드로 돌아옴

<br>

> 앱을 연후 Home키를 눌러 홈으로 돌아가면 전체 작업이 백그라운드로 전환되고 다시 앱을 열면 기존 작업을 포그라운드로 가져온다. 이때 back stack은 그대로 유지된다.

<br>
<br>
<br>

## Jetpack 탐색 구성요소에서의 back stack (fragment)

<br>

![image](https://user-images.githubusercontent.com/52737532/136176700-ced13ac6-b004-4439-ae7f-1721b5fd6f08.png)

- back버튼을 누를 때마다 back stack에서 fragment를 pop

![image](https://user-images.githubusercontent.com/52737532/136179292-4bc67ad9-53e0-4e3c-a2a6-2097723db33e.png)

<br>
<br>

- 주문 취소를 하려면 stack에 쌓인 fragment를 전부 pop하고 startFragment로 돌아가야한다
- 이럴때 사용하는 것이 `popUpTo`

<br>

1. fragment들을 startFragment와 연결하는 action을 새로 만들어 준다.

![image](https://user-images.githubusercontent.com/52737532/136179765-abcc22c6-857c-4d52-9f55-0f0057a6556f.png)

<br>

2. fragment들의 xml에 주문 취소 버튼을 추가한다.

```xml
<Button
    android:id="@+id/cancel_button"
    style="?attr/materialButtonOutlinedStyle"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_marginEnd="@dimen/side_margin"
    android:text="@string/cancel"
    app:layout_constraintEnd_toStartOf="@id/next_button"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="@id/next_button" />
```

<br>

3. 취소 버튼에 클릭리스너를 추가한다.

- 도우미 메서드를 먼저 class에 추가한다.

```kotlin
fun cancelOrder() {
    sharedViewModel.resetOrder() //취소버튼이 눌리면 order를 초기화하고
    findNavController().navigate(R.id.action_flavorFragment_to_startFragment)
    //startFragment로 이동하기
}
```

```xml
<Button
    android:id="@+id/cancel_button"
    android:onClick="@{() -> flavorFragment.cancelOrder()}" ... />
```

<br>

---

> 여기서 주의해야할 점
>
> 만약 navigate로 `StartFragment`로 돌아가면 `StartFragment`가 그냥 back stack에 쌓인다.

![image](https://user-images.githubusercontent.com/52737532/136180376-1618d3f5-335e-400b-a759-8b9b8aac814a.png)

- 이 상태이므로 back버튼을 누르면 홈화면이 아니라 SummaryFragment로 돌아간다.

---

<br>

- 이를 해결하기 위해 `popUpTo` 속성을 사용한다

### `popUpTo`

- `app:popUpTo="@id/startFragment"`를 지정해주면 스택에 남게될 StartFragment에 도달할 때까지 back stack에 있는 대상이 모두 pop된다.
- 하지만 여기서 취소버튼을 누르면 다시 StartFragment가 나온다
  - 이는 navigate를 사용해서 새로운 StartFragment를 stack에 추가하기 때문이다.
  - 이를 해결하기 위해선 `popUpToInclusive`속성을 사용해야한다.

<br>

### `popUpToInclusive`

- StartFragment에 이르기까지(StartFragment를 포함해서) 모든 대상을 백 스택에서 팝하도록 요청
- `app:popUpToInclusive="true"`를 지정

![image](https://user-images.githubusercontent.com/52737532/136180967-22827d1e-da56-4edf-9cd1-58bd4367184b.png)

<br>
<br>
<br>

## 주문 전송하기

- 암시적 인텐트를 통해 다른 앱으로 정보를 전송하자 (gmail..등)

```kotlin
//sendOrder()

val orderSummary = getString(
    R.string.order_details,
    //sharedViewModel.quantity.value.toString(),
    resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes), //단복수 처리
    sharedViewModel.flavor.value.toString(),
    sharedViewModel.date.value.toString(),
    sharedViewModel.price.value.toString()
)

val intent = Intent(Intent.ACTION_SEND)
    .setType("text/plain")
    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order)) //제목 설정
    .putExtra(Intent.EXTRA_TEXT, orderSummary) //메일 내용 설정

if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
    startActivity(intent)
}//만약 인텐트를 처리할 앱이 없다면 앱이 비정상 종료되지 않도록
```

### 단복수 처리

- <a href="https://developer.android.com/guide/topics/resources/string-resource?hl=ko#Plurals">plurals</a>를 사용

```kotlin
<plurals name="cupcakes">
    <item quantity="one">%d cupcake</item>
    <item quantity="other">%d cupcakes</item>
</plurals>
```

- 한개 일때만 cupcake 다른 애들은 cupcakes
