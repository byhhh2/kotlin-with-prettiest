# [Unit 3] Pathway4

- 탐색그래프
    
    res > navigation > nav_graph.xml
    
    시작화면 설정 : 원하는 프래그먼트 마우스 오른쪽 버튼 > set as start destination
    
- ViewModel 권장사항
    
    뷰 모델 데이터를 public 변수로 노출하지 않는 것이 좋다. 
    
    →이름 지정 규칙 : 변경 가능한 private 속성의 이름 앞에 밑줄(_)을 붙이는 것
    
- 대리자 속성
    
    var <property-name> : <property-type> by <delegate-class>()
    

## 컵케익 앱 구조 한눈에 보기

Layout : Activity 및 Fragment Layout 파일 존재

`fragment_start.xml`앱에 표시되는 첫 번째 화면. 컵케이크 이미지와 주문할 컵케이크 수를 선택.

`fragment_flavor.xml`컵케이크 맛 목록이 라디오 버튼 옵션으로 표시, **Next** 버튼.

`fragment_pickup.xml`수령일을 선택하는 옵션, 요약 화면으로 이동할 수 있는 **Next** 버튼.

`fragment_summary.xml`수량, 맛과 같은 주문 세부정보의 요약이 표시. 주문을 다른 앱으로 전 버튼.\

Fragment Class : 

`StartFragment.kt` 앱에 표시되는 첫 번째 화면. 3개의 버튼을 위한 클릭 핸들러 및 뷰 결합 코드.

`FlavorFragment.kt` `PickupFragment.kt` `SummaryFragment.kt` 

상용구 코드, 토스트 메시지를 표시하는 **Next** 또는 **Send Order to Another App** 버튼의 클릭 핸들러

Resource : 

`drawable` 첫 번째 화면의 컵케이크 애셋, 런처 아이콘 파일.

`navigation/nav_graph.xml` 작업이 없는 4개의 프래그먼트 대상이 존재.

→ `startFragment` `flavorFragment` `pickupFragment` `summaryFragment` 

`values` 앱 테마를 맞춤설정하는 데 사용되는 색상, 크기, 문자열, 스타일, 테마.

```kotlin
fun orderCupcake(quantity: Int) {
    findNavController().navigate(R.id.action_startFragment_to_flavorFragment)
}
```

`findNavController()` NavController를 가져온 후 `navigate()`를 호출. 

## ViewModel 생성

OrderViewModel.kt

```kotlin
package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
        _quantity.value= numberCupcakes
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value= desiredFlavor
    }

    fun setDate(pickupDate: String) {
        _date.value= pickupDate
    }
}
```

OrderViewModel 내에서 가격이 계산되기 때문에 가격에 관한 setter 메서드는 필요하지 않음. 

→ StartFragment에서 공유 뷰 모델을 사용하려면 viewModels() 대리자 클래스 대신 activityViewModels()를 사용하여 OrderViewModel을 초기화.

`viewModels()` 현재 프래그먼트로 범위가 지정된 ViewModel 인스턴스를 제공.

                        → 인스턴스는 프래그먼트마다 다르다.

`activityViewModels()` 현재 활동으로 범위가 지정된 ViewModel 인스턴스를 제공.                               

                                     → 인스턴스는 동일한 활동의 여러 프래그먼트 간에 동일하게 유지.

속성 위임을 이용해 ViewModel 공유 :

 StartFragment, FlavorFragment, PickupFragment, SummaryFragment 클래스에 모두 적용

```kotlin
import androidx.fragment.app.activityViewModels
import com.example.cupcake.model.OrderViewModel

private val sharedViewModel: OrderViewModel by activityViewModels()
```

## apply 범위함수

```kotlin
clark.apply {
    firstName = "Clark"
    lastName = "James"
    age = 18
}

clark.firstName = "Clark"
clark.lastName = "James"
clark.age = 18
```

객체의 컨텍스트 내에서 코드 블록을 실행하며, 임시 범위를 형성. 

→ 이 범위에서 이름을 사용하지 않고 객체에 액세스 가능.

## 날짜 형식 지정

```kotlin
private fun getPickupOptions(): List<String> {
   val options = mutableListOf<String>()
   val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
   val calendar = Calendar.getInstance()
   // Create a list of dates starting with the current date and the following 3 dates
   repeat(4) {
       options.add(formatter.format(calendar.time))
       calendar.add(Calendar.DATE, 1)
   }
   return options
}
```

`SimpleDateFormat`  클래스를 통해 날짜의 형식 지정(날짜 → 텍스트) 및 파싱(텍스트 → 날짜)이 가능.

`getPickupOptions()` 수령 날짜 목록을 만들고 반환

→ 이런 코드를 사용하면 오늘 날짜 기준 며칠후 버튼을 생성 가능 !!

## 가격 구하기

```kotlin
private const val PRICE_PER_CUPCAKE = 2.00

class OrderViewModel : ViewModel() {
    ...
		private fun updatePrice() {
		    _price.value = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
		}
	
		fun setQuantity(numberCupcakes: Int) {
		    _quantity.value = numberCupcakes
		    updatePrice()
		}
		...	
}
```

`?:` elvis 연산자, 왼쪽의 표현식이 null이 아니면 이 값을 사용한다는 의미

      위 코드에서는 quantity.value의 값이 null일 수 있으므로 elvis 연산자를 사용.

## Back Stack 설정하기