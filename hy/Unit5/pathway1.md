
### 관계형 데이터배이스
<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-sql-basics/img/ef61dd2663e4da82.png" style="width:80%; margin-top: 20px;">

* 데이터를 테이블과 열, 행으로 구성하는 일반적인 데이터베이스 유형
* Primary Key 기본키 : 고유 식별자
* 데이터 유형 :  문자나 문자열, 숫자(소수점 포함 여부와 상관없음), 바이너리 데이터, 날짜 및 시간
* SQL(Structered Query Language) : 구조화된 쿼리 언어. 데이터 읽기, 조작에 사용

    >| SQL 문|기능|
    >|------|---|
    >| SELECT | 데이터 테이블에서 특정 정보를 가져오고 결과를 필터링, 정렬|
    >| INSERT | 테이블에 새 행 추가|
    >| UPDATE | 테이블의 기존 행 업데이트|
    >| DELETE | 테이블의 기존 행 삭제|


<br><br><br>

### SELECT
* LIMIT : 행 수 제한
    ```sql
    SELECT name FROM park
    LIMIT 5
    ```

* WHERE : 결과 필터링
* AND / OR : 부울연산자. 조건 2개 이상 추가

    ```sql
    SELECT name FROM park
    WHERE type != "recreation_area"
    AND area_acres > 100000
    ```


<br><br><br>

### SQL 함수
* COUNT()
* SUM()
* MAX() /  MIN()
* DISTINCT : 중복 값 제거
    ```sql
    SELECT DISTINCT type FROM park
    ```

<br><br><br>

### 결과 정렬 및 그룹화
* ORDER BY : 특정 기준으로 결과 정렬  
* ASC / DESC : 오름차순 / 내림차순
* GROUP BY : 결과가 특정 하위 집단으로 분리되고 각 열의 결과가 나머지 쿼리에 따라 정렬

    ```sql
    SELECT type, COUNT(*) FROM park
    GROUP BY type
    ORDER BY type
    ```


<br><br><br>

### 행 삽입 및 삭제
* INSERT
    ```sql
    INSERT INTO table_name
    VALUES (column1, column2, ...)
    ```
* UPDATE
    ```sql
    UPDATE park
    SET area_acres = 46,
    established = 1088640000,
    type = 'office'
    WHERE name = 'Googleplex'
    ```
* DELETE
    ```sql
    DELETE FROM park
    WHERE name = 'Googleplex'
    ```


<br><br><br>

### ROOM 종속 항목 추가
1. build.gradle(project) > buildscript > ext > room_version 정의 
    ```gradle
    room_version = '2.3.0'
    ```
2. build.gradle(app) > dependencies 종속항목 추가
    ```gradle
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation "androidx.room:room-ktx:$room_version"
    ```

 

<br><br><br>

### ROOM
* ORM(Object Relational Mapping 객체 관계형 매핑) 라이브러리
* 각 테이블은 클래스로 표시 : 모델클래스, 항목

* 테이블 항목 생성
  1. database.schedule 패키지 > Schedule 파일 생성
  2. 데이터 클래스 정의
  3. @Entity 주석 추가
  ```kotlin
  @Entity
  data class Schedule (
      @PrimaryKey val id: Int,
      @NonNull @ColumnInfo(name = "stop_name") val stopName: String,
      @NonNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
  )
  ```
    * `@Entity` : 클래스를 독립체로 명시
    * `@PrimaryKey` : 기본키로 정의
    * `@NonNull` : null 값이 되지 않도록 명시
    * `@ColumnInfo` : 새 열의 경우 사용



<br><br><br>

### DAO
* 데이터 액세스 객체를 나타내며 데이터 액세스 권한을 제공하는 Kotlin 클래스
* 데이터를 읽고 조작하는 함수 포함
* DAO 에서 함수 호출은 DB에서 sql 명령 실행과 비슷

* DAO 정의
  1. database.schedul 패키지 > ScheduleDao 파일 생성
  2. ScheduleDao 인터페이스 정의
  3. `@Dao` 주석 추가
  4. `@Query` 주석으로 쿼리 작성 - `:`으로 코틀린 값 참조

  ```kotlin
  @Query("SELECT * FROM schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
  fun getByStopName(stopName: String): List<Schedule>
  ```

 
<br><br><br>

### ViewModel 정의
<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-intro-room-flow/img/ffe1fde60e83972b.png" style="width:60%; margin-top: 20px; margin-bottom:10px;"> 

* viewmodel을 직접 인스턴스화 불가능
* 팩토리 클래스 : 뷰모델 객체를 인스턴스화

<br>

* 생성  
  1. viewmodels 패키지 > BusScheduleViewModel 파일 생성
  2. ViewModel 클래스 정의
  3. ScheduleDao에 상응하는 메서드 호출
  4. `BusScheduleViewModelFactory` 클래스 생성
  5. `create()`메서드 재정의

  ```kotlin
  class BusScheduleViewModel(private val scheduleDao: ScheduleDao)
      : ViewModel() {

      fun fullSchedule(): Flow<List<Schedule>> = scheduleDao.getAll()
      fun scheduleForStopName(name: String): Flow<List<Schedule>> = scheduleDao.getByStopName(name)

  }

  class BusScheduleViewModelFactory(
      private val scheduleDao: ScheduleDao
  ) : ViewModelProvider.Factory {
      override fun <T : ViewModel?> create(modelClass: Class<T>): T {
          if (modelClass.isAssignableFrom(BusScheduleViewModel::class.java)) {
              @Suppress("UNCHECKED_CAST")
              return BusScheduleViewModel(scheduleDao) as T
          }
          throw IllegalArgumentException("Unknown ViewModel class")
      }
  }
  ```

<br><br><br>

### 데이터베이스 클래스 생성 
* AppDatabase
  * 데이터베이스에서 정의되는 항목 지정
  * 각 DAO 클래스의 단일 인스턴스 액세스 권한 제공
  * 데이터베이스 미리 채우기 등 추가 설정 실행
  
 <br>

* AppDatabase 클래스 생성
  1. database 패키지 > AppDatabase 파일 생성
  2. RoomDatabase를 상속받는 추상 클래스로 정의
  3. ScheduleDao를 반환하는 추상 함수 추가
  4. 컴패니언객체로 `INSTANCE` 추가, AppDatabase 인스턴스를 반환하는 함수 정의
  5. `@Database` 주석 추가 : 항목 유형(`CalssName::class`)는 배열로 나열, 데이터베이스는 버전번호 1로 부여

  ```kotlin
  @Database(entities = arrayOf(Schedule::class), version = 1)
  abstract class AppDatabase : RoomDatabase() {
      abstract fun scheduleDao(): ScheduleDao

      companion object {
          @Volatile
          private var INSTANCE: AppDatabase? = null  // null값이므로 ?로 유형 표현

          fun getDatabase(context: Context): AppDatabase {
              return INSTANCE ?: synchronized(this) {
                  val instance = Room.databaseBuilder(
                      context,
                      AppDatabase::class.java,
                      "app_database")
                      .createFromAsset("database/bus_schedule.db")  //기존 데이터 로드
                      .build()
                  INSTANCE = instance

                  instance
              }
          }
      }//companion object
  }
  ```

<br>

* Application 클래스 생성
  1. BusScheduleApplication 파일 생성 후 Application을 상속받는 클래스 정의
  2. AppDatabase 유형의 데이터베이스 속성 추가, 지연 속성(`lazy`) 추가 
  3. 매니페스트 name 속성 추가


  ```kotlin
  //BusScheduleApplication
  class BusScheduleApplication: Application() {
      val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
  }
  ```
  ```xml
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.example.busschedule">

      <application
          android:name="com.example.busschedule.BusScheduleApplication"
      ...
  ```

<br><br><br>

### ListAdapter 생성
* AsyncListDiffer를 사용하여 이전 데이터 목록과 새 데이터 목록의 차이를 확인
* 생성
  1. BusStopAdapter 파일 생성
  2. ViewHolder 클래스 생성 > bind() 구현
  3. `onCreateViewHolder()` 재정의 : 레이아웃 확장, 현재 위치 항목에 클릭 리스너 설정
  4. `onBindViewHolder()` 재정의 : 지정된 위치에 뷰 바인딩
  5. 컴패니언 객체 `DiffCallback` 구현
     * `areItemsTheSame()` : ID만 확인하여 객체(데이터베이스이 행)이 같은지 확인
     * `areContentsTheSame()` : 모든 속성을 확인하여 객체가 같은지 확인

  ```kotlin
  class BusStopAdapter(private val onItemClicked: (Schedule) -> Unit) :
      ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

      class BusStopViewHolder(private var binding: BusStopItemBinding) :
          RecyclerView.ViewHolder(binding.root) {
          @SuppressLint("SimpleDateFormat")
          fun bind(schedule: Schedule) {
              binding.stopNameTextView.text = schedule.stopName
              binding.arrivalTimeTextView.text = SimpleDateFormat("h:mm a").format(
                  Date(schedule.arrivalTime.toLong() * 1000) )
          }
      }

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
              : BusStopViewHolder {
          val viewHolder = BusStopViewHolder(
              BusStopItemBinding.inflate(
                  LayoutInflater.from(parent.context),
                  parent,
                  false
              )
          )
          viewHolder.itemView.setOnClickListener {
              val position = viewHolder.adapterPosition
              onItemClicked(getItem(position))
          }
          return viewHolder
      }

      override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
          holder.bind(getItem(position))
      }

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

* Fragment에 어댑터 설정
  1. viewModel 참조
  2. `onViewCreated()`에서 recycler뷰 설정 후 레이아웃 매니저 할당
  3. 어댑터 속성할당
  4. `submitList()`를 호출하여 목록 보기 업데이트
  ```kotlin
  class FullScheduleFragment: Fragment() {
      ...
      private val viewModel: BusScheduleViewModel by activityViewModels {
          BusScheduleViewModelFactory(
              (activity?.application as BusScheduleApplication).database.scheduleDao()
          )
      }
      ...
      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          recyclerView = binding.recyclerView
          recyclerView.layoutManager = LinearLayoutManager(requireContext())

          val busStopAdapter = BusStopAdapter({
              val action = FullScheduleFragmentDirections.actionFullScheduleFragmentToStopScheduleFragment( stopName = it.stopName )
              view.findNavController().navigate(action)
          })
          recyclerView.adapter = busStopAdapter

          lifecycle.coroutineScope.launch {
              viewModel.fullSchedule().collect() {
                  busStopAdapter.submitList(it)
              }
          }
      }   
      ...     
  ```



<br><br><br>

### Flow
* DAO가 데이터베이스에서 데이터를 지속적으로 내보낼 수 있는 비동기 흐름
* 항목이 삽입, 업데이트, 삭제 되면 결과가 다시 Fragment로 전송
* `collect()` 함수로 `submitList()` 호출 가능

1. `ScheduleDao` 함수의 반환 유형을 `Flow<List<Schedule>>` 로 변경
2. ViewModel 함수의 반환 유형을 `Flow<List<Schedule>>` 로 변경
3. Fragment에서 ViewModel의 함수를 사용하는 부분에서 코루틴 호출
4. 
```kotlin
// FullScheduleFragment 변경 전
busStopAdapter.submitList(viewModel.fullSchedule())

//변경 후
lifecycle.coroutineScope.launch {
   viewModel.fullSchedule().collect() {
       busStopAdapter.submitList(it)
   }
}
```
 

<br><br><br>
<hr>
<br>


### <퀴즈>

* DISTINCT<br>
a. 집계 함수에서 사용 가능<br>
 

* 기본키<br>
a. 테이블 간 관계를 나타냄<br>
a. 한 테이블의 행이 다른 테이블의 행 참조<br>
a. 테이블 각 행의 고유 식별자<br>

* ListAdapter <br>
a. RecyclerViewAdapter처럼 ViewHolder 클래스 사용<br>
a. DiffUtil 활용<br>

* FLOW <br>
a. DAO 함수에서 일반유형을 반환할 경우 앱이 데이터 변경에 응답 x<br>
a. flow로 반환된 데이터는 collect 함수로 처리<br>
a. 비동기로 계산된 여러 개의 값을 반환<br>

   