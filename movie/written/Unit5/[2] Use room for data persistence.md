- 영구적인 데이터 저장을 위해 Room 사용

![image](https://user-images.githubusercontent.com/52737532/137955388-5084c7ce-a23c-4925-9803-60db6cfe78e3.png)

<br>
<br>

## 시작 코드

- `main_activity.xml` : fragment host
- `item_list_fragment.xml` - `ItemListFragment.kt` : 아이템 리스트 + 추가 버튼
- `fragment_add_item.xml` - `AddItemFragment.kt` : 아이템 추가 창

<br>

- **data entities** : 데이터베이스의 테이블
- **DAO** : 데이터를 삽입, 갱신 등의 메서드
- **database class** : 데이터베이스 보유, 데베와 연결, DAO 인스턴스 제공

<br>

![image](https://user-images.githubusercontent.com/52737532/137963659-03c7d6a8-a1a5-4d47-938a-fb632e3f33e6.png)

<br>

### Room 종속 추가

```
//build.gradle (Module)

//dependencies

    // Room
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
```

<br>
<br>
<br>
<br>

> **순서**  
> Entity 만들기 -> DAO 만들기 -> 데이터베이스 인스턴스 만들기 (Application에서 인스턴스화) -> ViewModel 만들기

<br>

## 1. Entity 만들기 = data class 만들기

![image](https://user-images.githubusercontent.com/52737532/137973038-e98cad4a-d695-496e-801c-df700af4e52d.png)

- `com.example.inventory.data` > `Item`

```kotlin
//데이터 클래스
@Entity(tableName = "item")
data class Item( //생성자
    @PrimaryKey(autoGenerate = true) //각 Item의 아이디 자동 생성
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val itemName: String,
    @ColumnInfo(name = "price")
    val itemPrice: Double,
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int
)
```

<br>

## 2. DAO 만들기

![image](https://user-images.githubusercontent.com/52737532/137974445-aaefe75d-c16e-491b-896b-bacc730672be.png)

<br>

- `data (package) > ItemDao.kt`

<br>

```kotlin
@Dao
interface ItemDao {
    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE) //ID가 충돌할 경우 무시하는 전략
    suspend fun insert(item: Item)
    //데이터베이스 작업이 오래 걸리므로, 코루틴에서 함수를 호출하도록 suspend함수로 만들어 준다.

    // update
    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    // id기반으로 Item찾기
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>
    // Flow를 통해 데이터를 자동 업데이트
    // Flow 반환 type을 사용하면 Room이 자동으로 백그라운드 스레드에서 쿼리를 수행하므로 suspend 사용할 필요 없음

    //이름 오름차순으로 SELECT ALL
    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>
}
```

- Room은 `@Insert`, `@Delete`, `@Update`와 같은 편의 annotation을 제공한다. -> 쿼리 직접 안짜도된다.
- 그 외에 경우에는 `@Query` 사용
- suspend : 데이터베이스 작업이 오래 걸리므로, 코루틴에서 함수를 호출하도록 suspend함수로 만들어 준다.
- Flow : Flow 반환 type을 사용하면 Room이 자동으로 백그라운드 스레드에서 쿼리를 수행하므로 suspend 사용할 필요 없음

<br>
<br>

## 3. 데이터베이스 인스턴스 만들기

![image](https://user-images.githubusercontent.com/52737532/137978188-73a1f410-be71-4a02-8ca2-82daf77e9939.png)

<br>

- 핵심은 데이터베이스를 가져오고, 데이터베이스 인스턴스가 없으면 인스턴스화하고, 있으면 참조를 리턴
- race condition이 있을 수 있기 때문에 synchronized 블록사용
- DAO를 반환하는 함수 포함

```kotlin
//ItemRoomDatabase.kt

// Item을 entities 목록이 있는 유일한 클래스로
// version은 스키마를 변경할 때마다 높아짐
// exportSchema = false 스키마 버전 기록 백업 유지 안함
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao //ItemDao 반환

    // 데이터베이스를 만들거나 있으면 반환
    companion object {
        // 데이터베이스의 단일 인스턴스 참조
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null


        //데이터 베이스를 반환
        fun getDatabase(context: Context): ItemRoomDatabase {
            // 동시에 데이터베이스 인스턴스를 요쳥할 때 방지용
            // synchronized 블록 내에 데이터베이스를 가져오면 한 번에 한 스레드만 이 코드 블록에 들어갈 수 있음
            // 무조건 데베가 한번만 초기화 된다.
            return INSTANCE ?: synchronized(this) { // 인스턴스를 로킹
                val instance = Room.databaseBuilder( //데이터베이스 가져오기
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration() //스키마 변경에 대한것.. migrate(이전), 간단한 방법으로 데베 제거후 다시빌드
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
```

---

### Application에서 데이터베이스 인스턴스화

```kotlin
class InventoryApplication : Application(){
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
    //database 인스턴스 인스턴스화
    //lazy : 선언과 동시에 초기화하지만 호출시점에 초기화가 이루어짐
}
```

---

<br>

## 4. ViewModel 만들기

```kotlin
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {
    // insert
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            //코루틴 시작
            itemDao.insert(item) //suspend 함수 호출
        }
    }

    //문자열을 통해 Item을 채우고 반환
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
                itemName = itemName,
                itemPrice = itemPrice.toDouble(),
                quantityInStock = itemCount.toInt()
        )
    }

    // Item을 만들고 Insert하는 함수
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // ViewModel 반환
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

- ViewModel을 인스턴스화 하는 ViewModelFactory
- ViewModel에는 frament에서 사용할 데베 관련 함수들 선언

<br>
<br>

## 5. Fragment 수정

- ViewModel의 데이터베이스 조작 함수를 가지고 앱에 맞게 함수 선언

<br>
<br>

## RecyclerView 추가

<br>

---

### 확장 함수

```kotlin
class Square(val side: Double){
        fun area(): Double{
        return side * side;
    }
}

// Extension function to calculate the perimeter of the square
fun Square.perimeter(): Double{ //확장 함수
        return 4 * side;
}

// Usage
fun main(args: Array<String>){
      val square = Square(5.5);
      val perimeterValue = square.perimeter()
      println("Perimeter: $perimeterValue")
      val areaValue = square.area()
      println("Area: $areaValue")
}
```

- 기존 클래스를 수정하지 않고 클래스에 함수 추가

---

```
-- Adapter
    ├--ViewHolder (class) - bind (fuction)
    ├--onCreateViewHolder (function) [return ViewHolder]
    ├--onBindViewHolder (function) [bind()]
    ├--DiffCallback (companion object) [compare difference]
```

1. Adapter 설정하기 (-ListAdapter.kt)
2. ViewModel에 모든 Item에 대한 LiveDate List를 DAO에서 가져오기 (-ViewModel.kt)
3. fragment들에서 ViewModel 선언하기 (-fragment.kt)
4. fragment들에서 adapter를 recyclerView에 바인딩하기 (-fragment.kt)
5. adapter에서 submitList()를 호출하고 List 전달 (-fragment.kt)

<br>
<br>

## Item의 세부사항 fragment

1. Item list에서 Item을 클릭하면 Item의 세부사항으로 넘어감.
2. 세부사항에서는 Item을 팔거나 DELETE할 수 있음

<br>
<br>

## Item detail에서 SELL, DELETE, EDIT 구현

<br>

### SELL

1. update 함수사용
2. 만약 재고가 0보다 크면 재고 -1 해줌

---

### `copy()`

```kotlin
// Data class
data class User(val name: String = "", val age: Int = 0)

// Data class instance
val jack = User(name = "Jack", age = 1)

// A new instance is created with its age property changed, rest of the properties unchanged.
val olderJack = jack.copy(age = 2)
```

- 클래스의 새로운 속성값으로 바꾼 후 객체를 복사

```kotlin
val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
```

---

<br>

1. viewModel에서 아이템을 Sell하는 함수를 만든다.
2. detail fragment에서 sell 버튼에 클릭 리스너를 달고 viewModel의 sell 함수를 연결해준다.
3. 만약 재고가 0 이하로 떨어지면 sell 버튼을 클릭 불가능하게 만든다
4. fragment에서 binding

```kotlin
   binding.apply {
       ...
       sellItem.isEnabled = viewModel.isStockAvailable(item)
       sellItem.setOnClickListener { viewModel.sellItem(item) }
   }
```

<br>

### DELETE

1. ViewModel에서 delete 함수를 만든다. (delete는 suspend이기 때문에 코루틴으로 실행)
2. fragment에서 binding

<br>

### EDIT

> **FAB**
>
> Floating Action Button : 오른쪽 아래에 둥둥 떠다니는 버튼

<br>

1. 여기서 ADD fragment 재활용

```kotlin
//detail fragment

private fun editItem() { // FAB 누르면
   val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
       getString(R.string.edit_fragment_title), // edit 타이틀 넘겨주기
       item.id
   )
   this.findNavController().navigate(action)
}
```

2. Edit 버튼 누르면 ADD fragment로 넘어가되 edit타이틀로 바꾸고, `item.id`넘겨주기
3. TextView 채우기 (수정이니깐 그 전 내용 있어야 함) `setText()`로 설정
4. `onViewCreated()`에서 `id > 0` 이면 (이미 Item이 존재하면) TextView 채운걸로 바인딩
5. ViewModel에서 update 메서드 구현
