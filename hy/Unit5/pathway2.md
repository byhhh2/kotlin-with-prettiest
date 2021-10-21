
### Room의 기본 구성요소
<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/33a193a68c9a8e0e.png" style="width:70%; margin-top: 20px;">

* Data entities(데이터 항목) : 앱 데이터베이스의 테이블을 나타냄. 데이터를 업데이트하고 삽입할 새 행을 만드는 데 사용됨
* DAO(데이터 액세스 객체) : 앱이 데이터베이스의 데이터를 검색 및 업데이트, 삽입, 삭제하는 데 사용하는 메서드를 제공
* 데이터베이스 클래스 : 데이터베이스 보유. 기본 앱 데이터베이스 연결을 위한 기본 액세스 포인트로 앱에 데이터베이스와 연결된 DAO 인스턴스를 제공합니다.
  




<br><br><br>

### Item Entity 생성
* Entity(항목) 클래스 : 테이블 정의. 클래스의 각 인스턴스는 데이터베이스 테이블의 행을 나타냄
* Mapping(매핑) : 데이터베이스의 정보를 표시하고 상호작용하는 방법을 Room에 알려줌
* `@Entity`주석 
  * 클래스를 데이터베이스의 Entity 클래스로 표시
  * `tableName` 인수로 테이블의 이름 지정 가능 `@Entity(tableName = "item")`
 

<br><br><br>


### data 클래스
* 데이터를 보유하는 데 사용. `data` 키워드로 표시
* 컴파일러가 `toString()`, `copy()`, `equals()`와 같은 유틸리티 자동으로 생성
* 요구사항
  * 기본생성자에 하나 이상의 매개변수 포함
  * 기본 생성자 매개변수는 `val` / `var` 로 표시
  * `abstract` / `open` / `sealed` / `inner` 불가능
* copy() <br>
  : 일부 속성을 변경하지만 나머지 속성은 변경하지 않고 객체를 복사하는데 사용
  ```kotlin
  data class User(val name: String = "", val age: Int = 0)

  val jack = User(name = "Jack", age = 1)

  val olderJack = jack.copy(age = 2)
  ```




<br><br><br>

### DAO(데이터 액세스 객체) 생성
* 데이터베이스 쿼리/검색, 삽입, 삭제, 업데이트를 위한 편의 메서드를 제공하는 맞춤 인터페이스
* Room은 컴파일 시간에 DAO 클래스의 구현을 생성
* 추상 인터페이스를 제공하여 지속성 레이어를 애플리케이션의 나머지 부분과 분리하는 데 사용되는 패턴
* 기본 지속성 레이어에서 데이터베이스 작업과 관련된 모든 복잡성을 나머지 애플리케이션으로부터 숨김. 데이터를 사용하는 코드와 별개로 데이터 액세스 레이어를 변경 가능.
* `@Insert` / `@Delete` / `@Update`  <br>
  : suspend 함수로 생성 (데이터베이스 작업은 실행에 오랜 시간이 걸릴 수 있으므로 별로의 스레드로 실행)
* `@Insert(onConflict = OnConflictStrategy.IGNORE)`
  * onConflict : 충돌이 발생할 경우 Room에 실행할 작업을 알려줌
  * onConflictStrategy 전략
    * ABORT : 기본값. 충돌 시 트랜잭션 롤백
    * IGNORE : 충돌 무시. 기존 데이터 유지
    * REPLACE : 이전 데이터를 교체하고 트랜젝션 계속 수행


<br><br><br>

### 데이터베이스 인스턴스 생성
* Database : Entity, DAO의 목록 정의

1. Data 패키지 > ItmeRoomDatabase 파일 생성 > ItemRoomDatabase 클래스를 RoomDatabase를 확장하는 abstract 클래스로 생성 
2. `@Database` 주석 추가 > 인수 entities = [item::calss] 지정 / 인수 exportSchema = false (스키마 버전 기록 백업 유지x)
3. 컴패니언 객체로 INSTANCE 생성 > null로 초기화 > `@Volatile`주석 추가
4. 컴패니언 객체 내부에서 Context를 매개변수로 갖는 getDatabase() 메서드 정의 > ItemRoomDatabase 반환
5. getDatabase() 내에서 INSTANCE를 반환하거나 INSTANCE가 null이면 `synchronized{}` 블록 내에서 초기화. elvis 연산자(?:) 사용
6. synchronized 블록 내에서 val instance 변수 생성 > Room.databaseBuilder()로 데이터베이스를 가져옴 > applicationContext, ItemRoomDatabase, 데이터베이스 이름 전달
7. Room.databaseBuilder() 끝에서 `fallbackToDestructiveMigration()` 추가 > build() 호출
8. synchronized 블록 내에서 `INSTANCE = instance` 할당 
9. synchronized 블록 끝에서 instance 반환 


```kotlin
@Database(entities = [Item::class], version = 1, exportSchema = false)  //2
abstract class ItemRoomDatabase : RoomDatabase() {                      //1

   abstract fun itemDao(): ItemDao

   companion object {
       @Volatile
       private var INSTANCE: ItemRoomDatabase? = null                   //3
       fun getDatabase(context: Context): ItemRoomDatabase {            //4
           return INSTANCE ?: synchronized(this) {                      //5
               val instance = Room.databaseBuilder(                     //6
                   context.applicationContext,
                   ItemRoomDatabase::class.java,
                   "item_database"
               )
                   .fallbackToDestructiveMigration()                    //7
                   .build()
               INSTANCE = instance                                      //8
               return instance                                          //9
           }
       }
   }
}
```

* `@Volatile`  <br>
  * 휘발성 변수의 값은 캐시에 넣지 않고 모든 쓰기와 읽기는 기본 메모리에서 실행  
  * INSTANCE 값이 항상 최신 상태로 유지되고 모든 실행 스레드에서 같은지 확인 가능 
  * 한 스레드에서 INSTANCE 변경 시 다른 스레드에 즉시 표시
* `fallbackToDestructiveMigration()`
  * Migration 객체 : 데이터가 손실되지 않도록 이전 스키마의 모든 행을 가져와 새 스키마의 행으로 변환하는 방법을 정의



<br><br><br>

### ViewModelScope
* viewModel이 소멸될 때 하위 코루틴을 자동 취소하는 viewModel 클래스의 확장 속성
* viewModel 클래스에서 DAO 메서드 호출 시 사용  



<br><br><br>

### Fragment 업데이트
1. Fragment 시작 부분에 viewModel 변수 생성 > `by activityViewModels()` 속성 위임
2. 람다 내에 viewModelFactory 생성자 호출 > database 인스턴스를 사용하여 Dao 인스턴스 전달
3. lateint 속성의 item 변수 생성
4. onViewCreated() 재정의 > 클릭 리스너 추가



```kotlin
private val viewModel: InventoryViewModel by activityViewModels {           //1
    InventoryViewModelFactory(                                              //2
        (activity?.application as InventoryApplication).database.itemDao()
    )
}

lateinit var item: Item                                                     //3

...

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   super.onViewCreated(view, savedInstanceState)
   binding.saveAction.setOnClickListener { addNewItem() }                   //4
}
```
 


<br><br><br>

### 확장 함수 
* 클래스의 객체에서 함수를 호출할 때 점 표기법 사용
* 특정 클래스에 접근하지 않고 함수를 기존 클래스에 추가 가능


```kotlin
class Square(val side: Double) {
        fun area(): Double{ return side * side; }
}

fun Square.perimeter(): Double { return 4 * side; }   // <-- 확장 함수

fun main(args: Array<String>){
      val square = Square(5.5);
      val perimeterValue = square.perimeter()
      println("Perimeter: $perimeterValue")
}
```

<br><br><br>

### 데이터베이스 Entity 업데이트
1. viewModel에서 updateItem() 메서드 구현 > viewModelScope.launch 로 코루틴 시작 > ItemDao의 update 메서드 호출
2. sellItem() 메서드 구현 > if문으로 item.quantityInStock 수량 확인 > [copy()](#data-클래스) 메서드로 항목 업데이트 > updateItem 메서드 호출
3. Fragment 에서 bind() > 클릭리스너 설정 > viewModel.sellItem 호출 > item 전달

```kotlin
//viewModel
private fun updateItem(item: Item) {        
   viewModelScope.launch {
       itemDao.update(item)
   }
}

fun sellItem(item: Item) {
   if (item.quantityInStock > 0) {
       // Decrease the quantity by 1
       val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
       updateItem(newItem)
   }
}
```

<br><br><br>

### Entity 삭제
1. viewModel에서 deleteItem() 메서드 구현 > viewModelScope.launch 로 코루틴 시작 > ItemDao의 delete 메서드 호출
2. Fragment 에서 deleteItem() > viewModel.deleteItem 호출 > item 전달
3. Fragment 에서 showConfirmationDialog() > deleteItem() 호출
4. Fragment 에서 bind() > 클릭리스너 설정 > showConfirmationDialog() 호출  


```kotlin
//viewModel
fun deleteItem(item: Item) {
   viewModelScope.launch { 
       itemDao.delete(item)
   }
}

//Fragment
private fun deleteItem() {
    viewModel.deleteItem(item)
    findNavController().navigateUp()
}

private fun showConfirmationDialog() {
    MaterialAlertDialogBuilder(requireContext())
        ...
        .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteItem() }
        .show()
}
```



<br><br><br>
<hr>
<br>


### <퀴즈>

* synchronized() 를 사용하는 이유<br>
a. 데이터베이스의 사본 여러 개 생성 <br>
a. 여러 스레드에서 동시에 안전하게 접근 가능 <br>

* Room 앱에서 Application 클래스를 사용하면 다른 객체가 AppDatabase 클래스에 접근 가능<br>

<br>