
### MainActivity 기본 생성 코드 변경

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
   }
}
```

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main)
```

<br><br><br>

### 공유 ViewModel
* 여러 Fragment 간 단일 viewModel을 사용하여 데이터를 저장할 때 사용
* Activity 인스턴스 사용
* 생성
  1. app > java > com.example.cupcake > model package 생성 > OrderViewModel 클래스 생성
  2. OrderViewModel 클래스를 ViewModel로 확장
  3. Fragment 클래스에서 공유 OrderViewModel을 변수로 생성 
     : 대리자 클래스로 `activityViewModels()` 사용

      ```kotlin
      class StartFragment : Fragment() {
          private val sharedViewModel: OrderViewModel by activityViewModels()
          ...
      }
      ```
* data binding
  1. app > res > layout > fragment_flavor.xml > `layout`태그 내부에 `data` 태그 추가 
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
  2. Fragment 클래스 > `onViewCreated()` 에서 ViewModel인스턴스를 레이아웃의 공유 ViewModel 인스턴스와 결합
      ```kotlin
      class FlavorFragment : Fragment() {
          ...
          override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
              super.onViewCreated(view, savedInstanceState)

              binding?.apply {
                  viewModel = sharedViewModel
                  ...
              }
          }
          ...
      }
      ```


<br><br><br>

### `apply` 범위 함수
* 객체의 context 내에서 코드 블럭을 실행하며 임시 범위 형성
* 범위 내에서 이름을 사용하지 않고 객체에 접근 가능
* 객체 구성이 일반적인 사용 사례

```kotlin
clark.apply {
    firstName = "Clark"
    lastName = "James"
    age = 18
}

// The equivalent code without apply scope function would look like the following.

clark.firstName = "Clark"
clark.lastName = "James"
clark.age = 18
```

  
<br><br><br>

### 리스너 결합
* layout에서 데이터 결합 표현식으로 사용 가능
* viewModel의 메서드 실행 <br>
  : fragment_flavor.xml의 라디오버튼에 이벤트 리스너 추가 
    ```xml 
    <RadioButton
        android:id="@+id/vanilla"
        ...
        android:onClick="@{() -> viewModel.setFlavor(@string/vanilla)}"
        .../>

    ```
* Fragment의 메서드 실행
1. fragment_start.xml 에서 `startFragment`데이터 변수 추가
2. Fragment 클래스 > `onViewCreated()`에서 데이터 변수를 fragment인스턴스에 결합
3. fragment_start.xml의 버튼 `onclick` 속성 추가
```xml 
<layout ...>
    <data>
        <variable
            name="flavorFragment"
            type="com.example.cupcake.FlavorFragment" />
    </data>

    <ScrollView ...>
        <Button
            android:id="@+id/next_button"
            android:onClick="@{() -> pickupFragment.goToNextScreen()}"
            ...  />
```
```kotlin
class StartFragment : Fragment() {
    ...
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            ...
            flavorFragment = this@FlavorFragment
        }
    }
    ...
}
```






<br><br><br> 

### 날짜 형식 지정
```kotlin
    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("EEE MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
```
 
* `SimpleDateFormat` : 언어에 민감한 방식으로 날짜 형식 지정/파싱 하는 클래스
* `"E MMM d"` : 날짜 및 시간 형식의 표현
* `Locale`객체 : 특정한 지리/정치/문화적 지역 또는 언어/국가 등 조합 표현
* `Locale.getDefault()` : 사용자 기기에 설정된 언어 정보를 가져와 `simpleDateFormat`에 전달
* `calendar` : 현재 날짜 및 시간 포함
* `repeat` : 함수를 주어진 횟수만큼 반복

<br><br><br>

### `?:` elvis 연산자
연산자 왼쪽 표현식이 `null`이 아니면 값을 사용하고 `null`이면 오른쪽 표현식을 사용
```kotlin
//quantity.value 가 null 일 경우 0 사용
quantity.value ?: 0
```



<br><br><br>

### LiveData LifecycleOwner
* LifecycleOwner : Fragment
* LiveData observer : 관찰 가능한 데이터가 있는 레이아웃 파일의 결합 표현식
    ```xml
    android:text="@{@string/subtotal_price(viewModel.price)}
    ```
* 설정 : Fragment 클래스 > `onViewCreated()`에서 lifecycleOwner 설정
    ```kotlin
    class FlavorFragment : Fragment() {
        ...
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding?.apply {
                viewModel = sharedViewModel
                lifecycleOwner = viewLifecycleOwner
                ...
            }
        }
        ...
    }
    ```
 
<br><br><br>

### LiveData Transformation
* LiveData Transformation 메서드 : LiveData소스에서 데이터를 조작하고 결과 LiveData객체를 반환하는 방법 제공
* 사용할 수 있는 몇가지 예
    * 표시할 날짜 및 시간 문자열 형식 지정
    * 항목 목록 정렬
    * 항목 필터링 또는 그룹화
    * 모든 항목 합계, 항목 수, 마지막 항목 반환 등 목록 계산 결과

```kotlin
//숫자만 있는 가격 문자열
private val _price = MutableLiveData<Double>()
val price: LiveData<String>

//형식이 지정된 가격 문자열
private val _price = MutableLiveData<Double>()
val price: LiveData<String> = Transformations.map(_price) {
   NumberFormat.getCurrencyInstance().format(it)
}
```


<br><br><br>

### 뒤로(UP)버튼 동작 구현
```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main){

    private lateinit var navController :NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        
        setupActionBarWithNavController(navController)
    }

    //뒤로(up)버튼 처리
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
```


<br><br><br>

### back stack 백스택
#### back stack 수정 작업 추가
1. nav_graph.xml 에서 cancle 버튼 클릭 시 탐색 작업 추가
2. Fragment 레이아웃에 cancle 버튼 추가
3. Fragment 클래스에 버튼 클릭시 실행 될 메서드 추가
4. Fragment 레이아웃에 cancle 클릭 리스너 추가
> cancle버튼 > 뒤로 버튼 클릭 시 오류 발생
> : cancle 버튼 클릭 시 새로운 startFragment를 back stack에 추가함 
> <img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-navigation-backstack/img/5616cb0028b63602.png?hl=ko" style="width:80%; margin-top: 20px; ">

#### back stack에서 추가 대상 pop
1.  nav_graph.xml > 추가한 작업의 Attribute > Pop Behavior 
2.  popUpTo : startFragment 로 설정
3.  popUpTpInclusive : true 로 설정
> cancle 버튼 클릭 후 뒤로 버튼 클릭 시 앱 종료

 

<br><br><br>

### 이메일 인텐트
```kotlin
var orderSummary = getString(
    R.string.order_details,
    sharedViewModel.quantity.value.toString(),
    sharedViewModel.flavor.value.toString(),
    sharedViewModel.date.value.toString(),
    sharedViewModel.price.value.toString()
    )
val intent = Intent(Intent.ACTION_SEND)
    .setType("text/plain")
    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
    .putExtra(Intent.EXTRA_TEXT, orderSummary)

if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
    startActivity(intent)
}
```


<br><br><br>

### `plurals` 복수형 리소스 
```xml
string.xml
<plurals name="cupcakes">
    <item quantity="one">%d cupcake</item>
    <item quantity="other">%d cupcakes</item>
</plurals>
```
```kotlin
resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes)
```

  
<br><br><br>