## SQL

- SELECT
- INSERT
- UPDATE
- DELECT

<br>

### Database Inspector

- `View > Tool Windows > Database Inspector`

![image](https://user-images.githubusercontent.com/52737532/137889838-7f1007ba-6593-4d20-903b-ffe0cd1969ab.png)

- 쿼리를 작성할 수 있다.

<br>

### Room

- 라이브러리
- ORM(객체 관계형 매핑) 라이브러리
- 관계형 DB의 테이블을 코틀린 코드에서 사용할 수 있는 객체에 매핑

<br>
<br>

## Bus scheduler app

### 종속 추가

```
//project

ext {
   kotlin_version = "1.3.72"
   nav_version = "2.3.1"
   room_version = '2.3.0' //
}

//app
//dependencies

implementation "androidx.room:room-runtime:$room_version"
kapt "androidx.room:room-compiler:$room_version"

// optional - Kotlin Extensions and Coroutines support for Room
implementation "androidx.room:room-ktx:$room_version"

```

<br>

### 데이터 클래스 만들기

- `database.schedule > Schedule.kt`

<br>

```kotlin
@Entity
data class Schedule(
   @PrimaryKey val id: Int,
   @NonNull @ColumnInfo(name = "stop_name") val stopName: String,
   @NonNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
)
```

- `@Entity` : Room이 테이블을 정의하는 데 사용하는 것으로 인식하도록 annotation 추가
- `@PrimaryKey`
- `@NonNull`
- `@ColumnInfo` : 열 이름 지정 (sql와 koline은 명명기법이 다르기 때문)

<br>

### DAO 만들기

- 함수를 호출함으로써 SQL 명령어를 실행하는 것과 같이 만들기 위해서
- DAO : data access object
- 데이터를 읽고 조작하는 함수 등
- `database.schedule > ScheduleDao.kt`

```kotlin
@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule ORDER BY arrival_time ASC")
    fun getAll(): List<Schedule>

    @Query("SELECT * FROM schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
    fun getByStopName(stopName: String): List<Schedule>
}
```

- 앞에 `:`을 붙여서 쿼리에서 kotlin값을 참조할 수 있다.

<br>

### ViewModel 정의

- 앱이 복잡해지면 데이터의 특정 부분에만 엑세스하는 화면이 여러개 있을 수 있다.
- DAO만 사용하면 두개 이상의 화면으로 작업할 때 제어를 쉽게 확인할 수 없다.

```kotlin
@Dao
interface ScheduleDao {

    @Query(...)
    getForScreenOne() ...
    // 화면 1은 getForScreenOne()함수에 엑세스하지만 다른 함수에는 엑세스하지 않는다.

    @Query(...)
    getForScreenTwo() ...

    @Query(...)
    getForScreenThree()

}
```

- 이런 이유로 ViewModel로 일부의 DAO를 분리하여 사용한다.

![image](https://user-images.githubusercontent.com/52737532/137896950-20b22961-a4c2-4f25-8a6a-cb8265d37f74.png)

- 앱의 일반적인 아키텍처 패턴
- 큰 앱에서는 각 fragment에 별도의 ViewModel을 사용하는 것이 좋다.

<br>

- `viewmodels > BusScheduleViewModel.kt`

```kotlin
// ViewModel

class BusScheduleViewModel(private val scheduleDao: ScheduleDao): ViewModel() {

    fun fullSchedule(): List<Schedule> = scheduleDao.getAll()
    fun scheduleForStopName(name: String): List<Schedule> = scheduleDao.getByStopName(name)
}
```

- `BusScheduleViewModel`를 직접 인스턴스화하면 fragment객체가 모든 메모리 관리를 처리해야한다. 이런 불편함을 없애기 위해 `BusScheduleViewModel` 객체를 대신 인스턴스화 하는 factory 클래스를 사용

```kotlin
//factory class

class BusScheduleViewModelFactory(
    private val scheduleDao: ScheduleDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BusScheduleViewModel(scheduleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

- ViewModel을 인스턴스화 해주는 factory class
- `create()`로 인스턴스화 (fragment가 직접 처리하지 않고도 뷰모델이 수명주기를 인식할 수 있음)

<br>

### 데이터베이스 클래스

- DAO, ViewModel으로 할 작업을 **Room에게 알려야**한다. -> AppDatabase 클래스 필요

#### AppDatabase

1. DAO에 단일 인스턴스 엑세스 권한 제공
2. 데이터베이스 미리 채우기

- `database > AppDatabase.kt`

```kotlin
@Database(entities = arrayOf(Schedule::class), version = 1) // [3]
abstract class AppDatabase: RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object { //[1]
        @Volatile //잠재적 버그 방지
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/bus_schedule.db") //[2]
                    .allowMainThreadQueries() //오류 해결
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
```

[1] : 데이터베이스 인스턴스가 하나만 있는지 (기존에 있는걸 반환하거나 없으면 만듦)

- `?:` 사용

[2] : 기존 데이터 로드
[3] : 버전 번호 = 스키마를 변경할 떄 마다 증가

<br>

### Application

- `BusScheduleApplication.kt`

```kotlin
//BusScheduleApplication.kt

class BusScheduleApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
```

```xml
// AndroidMainifest.xml

<application
    android:name="com.example.busschedule.BusScheduleApplication"
    ...

```

- 기본 Application이 아니라 BusScheduleApplication가 사용되도록

<br>

### ListAdapter

- `RecyclerView`를 위한 adapter
- 이전 데이터 목록과 새 데이터 목록의 차이를 확인해서 차이에 기반하여 뷰 업데이트

![image](https://user-images.githubusercontent.com/52737532/137930602-4cc1f6b2-6532-477b-b837-9e058f1933cb.png)

```
-- Adapter
    ├--ViewHolder (class) - bind (fuction)
    ├--onCreateViewHolder (function) [return ViewHolder]
    ├--onBindViewHolder (function) [bind()]
    ├--DiffCallback (companion object) [compare difference]
```

```kotlin
//BusStopAdapter.kt

class BusStopAdapter(private val onItemClicked: (Schedule) -> Unit) : ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

    // ViewHolder class
    class BusStopViewHolder(private var binding: BusStopItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat") // 오류 방지
        fun bind(schedule: Schedule) { // bind()
            binding.stopNameTextView.text = schedule.stopName
            binding.arrivalTimeTextView.text = SimpleDateFormat(
                "h:mm a").format(Date(schedule.arrivalTime.toLong() * 1000)
            )
        }
    }

    // onCreateViewHolder [return ViewHolder]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        val viewHolder = BusStopViewHolder(
            BusStopItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            ) // inflate
        )

        // item 하나가 클릭되었을 때
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            // 인자로 받아온 함수 실행 (onItemClicked)
            // FullScheduleFragment.kt에서 navigate() 함수 넘겨줌
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    // onBindViewHolder [bind()]
    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // DiffCallback object - 차이 계산
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }
}

```

<br>

- `FullScheduleFragment` ---navigate()---> `StopScheduleFragment`

```kotlin
// FullScheduleFragment.kt

// ViewModel 참조 가져오기
private val viewModel: BusScheduleViewModel by activityViewModels {
   BusScheduleViewModelFactory(
       (activity?.application as BusScheduleApplication).database.scheduleDao()
   )
}

...

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    //recyclerView 설정하고, 레이아웃 관리자 할당
    recyclerView = binding.recyclerView
    recyclerView.layoutManager = LinearLayoutManager(requireContext())


    // adapter속성 할당 (람다로 onItemClicked함수 전달)
    val busStopAdapter = BusStopAdapter {
        val action =
            FullScheduleFragmentDirections.actionFullScheduleFragmentToStopScheduleFragment(
                stopName = it.stopName
            )
        view.findNavController().navigate(action)
    }
    recyclerView.adapter = busStopAdapter

    //List View 업데이트
    busStopAdapter.submitList(viewModel.fullSchedule())
    //            ├--fun fullSchedule(): List<Schedule> = scheduleDao.getAll()
}


```

```kotlin
// StopScheduleFragment

// onViewCreated()

recyclerView = binding.recyclerView
recyclerView.layoutManager = LinearLayoutManager(requireContext())

val busStopAdapter = BusStopAdapter({})
//navigation할 함수 없음

recyclerView.adapter = busStopAdapter
busStopAdapter.submitList(viewModel.scheduleForStopName(stopName))
//stopName에 맞는 리스트 반환

```

<br>

---

### 오류 해결

```kotlin
//AppDatabase.kt

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database")
                    .createFromAsset("database/bus_schedule.db")
                    .allowMainThreadQueries() //
                    .build()
                INSTANCE = instance

                instance
            }
        }
```

```kotlin
//StopScheduleFragment.kt

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val busStopAdapter = BusStopAdapter({})
        recyclerView.adapter = busStopAdapter
        busStopAdapter.submitList(viewModel.scheduleForStopName(stopName)) //
    }
```

---

<br>
<br>

## Flow

- 데이터가 추가, 갱신 될 때 자동적으로 리스트가 업데이트 되지 않는다.
- 이를 해결하기 위해 사용하는 것이 `Flow`

- `submitList(List<Schedule>)`가 한번 실행되면 DAO에서 리스트가 딱 한번만 반환되고 그 이후로 반환되지 않는다.
- 데이터가 업데이트되면 결과가 fragment로 전송되게 하고 싶다면 `collect()`라는 함수를 사용하면된다.
  - Flow에서 내보낸 새 값으로 `submitList()`를 자동으로 실행해준다.

<br>

```kotlin
//BusScheduleViewModel

class BusScheduleViewModel(private val scheduleDao: ScheduleDao): ViewModel() {

   fun fullSchedule(): Flow<List<Schedule>> = scheduleDao.getAll()
   fun scheduleForStopName(name: String): Flow<List<Schedule>> = scheduleDao.getByStopName(name)
} //Flow를 반환하도록 변경
```

<br>

```kotlin
// FullScheduleFragment.kt

//busStopAdapter.submitList(viewModel.fullSchedule()) ❌

lifecycle.coroutineScope.launch {
   viewModel.fullSchedule().collect() {
       busStopAdapter.submitList(it)
   }
}

// StopScheduleFragment.kt

lifecycle.coroutineScope.launch {
   viewModel.scheduleForStopName(stopName).collect() {
       busStopAdapter.submitList(it)
   }
}
```

- 설정이후로 INSERT를 해보면 앱의 List가 자동으로 업데이트 된다.
