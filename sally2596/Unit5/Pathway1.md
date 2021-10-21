# Pathway1

# SQL 기본 사항

- 관계형 데이터베이스 개요
    - 관계형 데이터베이스
        
        단순한 전자 형식으로 액세스하고 쓸 수 있는 구조화된 데이터 모음
        
- 기본 SELECET 문
    - SQL에서는 = 하나로 비교가 가능
    - LIMIT N : N개 만큼만 보여줌
    - DISTINCT : 고유한 값들을 보여줌 (ex 같은 이름이 여러개면 이름 하나만 보여줌)
    - ORDER BY : 정렬함
        
        ex) ORDER BY name DESC : 내림차순
        
        ex) ORDER BY name ASC : 오름차순
        
    - INSERT
        
        ```kotlin
        INSERT INTO table_name
        VALUES (column1, column2, ...)
        ```
        
    - UPDATE
        
        ```kotlin
        UPDATE table_name
        SET column1 = ...,
        column2 = ...,
        ...
        WHERE column_name = ...
        ...
        ```
        
    - DELETE
        
        ```kotlin
        DELETE FROM table_name
        WHERE <column_name> = ...
        ```
        

---

## Room 및 Flow 소개

- Room dependencies 추가하기
    - `build.gradle (project)` 에 추가하기
        
        ```kotlin
        ext {
           kotlin_version = "1.3.72"
           nav_version = "2.3.1"
           room_version = '2.3.0'
        }
        ```
        
    - `build.gradle (Module)` 에 추가하기
        
        ```kotlin
        implementation "androidx.room:room-runtime:$room_version"
        kapt "androidx.room:room-compiler:$room_version"
        
        // optional - Kotlin Extensions and Coroutines support for Room
        implementation "androidx.room:room-ktx:$room_version"
        ```
        
    
- 항목 만들기
    
    ```kotlin
    @Entity
    data class Schedule(
       @PrimaryKey val id: Int,
       @NonNull @ColumnInfo(name = "stop_name") val stopName: String,
       @NonNull @ColumnInfo(name = "arrival_time") val arrivalTime: Int
    )
    ```
    
    @Entity : 이 클래스를 데이터베이스 테이블을 정의하는데 사용할 수 있는것으로 Room이 인식하도록 함
    
- DAO 정의
    
    Data Access Objects(DAO)
    
    - 데이터 액세스 객체를 나타냄
    - 데이터 액세스 권한을 제공하는 코틀린 클래스
    - 데이터를 읽고 조작하는 함수 포함
    - @Dao : Room에서 인터페이스를 사용할 수 있도록 함
    - `:` 값으로 매개변수 값 참조 가능
    
    ```kotlin
    @Dao
    interface ScheduleDao {
    		@Query("SELECT * FROM schedule ORDER BY arrival_time ASC")
        fun getAll(): List<Schedule>
    
        @Query("SELECT * FROM schedule WHERE stop_name = :stopName ORDER BY arrival_time ASC")
        fun getByStopName(stopName: String): List<Schedule>
    }
    ```
    
- ViewModel 정의
    
    뷰에 노출하는 DAO의 일부를 뷰 모델 클래스로 분리
    
    BusScheduleViewModel에 필요한 ViewModel 클래스는 수명 주기를 인식해야 함 → 수명 주기 이벤트에 응답할 수 있는 객체로 인스턴스화 : **팩토리 클래스**
    
    - ViewModelProvider.Factory : ViewModel 인스턴스를 생성하는 팩토리
    
    ```kotlin
    class BusScheduleViewModel(private val scheduleDao: ScheduleDao): ViewModel() {
        fun fullSchedule(): List<Schedule> = scheduleDao.getAll()
    
        fun scheduleForStopName(name: String): List<Schedule> = scheduleDao.getByStopName(name)
    }
    
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
    
    - BusScheduleViewModelFactory.create()로 BusScheduleViewModelFactory 객체를 인스턴스화 함
- 데이터베이스 클래스 만들기 및 데이터 베이스 미리 채우기
    
    `AppDatabase`  → 모델과 DAO 클래스, 실행하려는 모든 데이터베이스 설정을 완벽하게 제어할 수 있음
    
    - 데이터베이스에서 정의되는 항목 지정
    - 각 DAO 클래스의 단일 인스턴스 액세스 권한 제공
    - 데이터베이스 미리 채우기와 같은 추가 설정 실행
    
    ```kotlin
    @Database(entities = arrayOf(Schedule::class), version = 1)
    abstract class AppDatabase: RoomDatabase() {
        abstract fun scheduleDao(): ScheduleDao
        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null
    
            fun getDatabase(context: Context): AppDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app_database")
                        .createFromAsset("database/bus_schedule.db")
                        .build()
                    INSTANCE = instance
    
                    instance
                }
            }
        }
    }
    ```
    
    - 데이터베이스가 있다면 기존 인스턴스 반환
    - 없다면 처음으로 데이터베이스 생성
        - createFromAsset() : 채워진 데이터 로드하기
    - 모델 클래스도 주석이 필요
        - @Database(entities = arrayOf(Schedule::class), version = 1)
            - 버전 번호는 스키마를 변경할 때마다 증가
    
    ```kotlin
    class BusScheduleApplication : Application() { //상속받음
        val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    }
    ```
    
    - Application클래스의 맞춤 서브 클래스 제공
    
    **AndroidMainifest.xml에서 android:name 속성을 com.example.busschedule.BusScheduleApplication으로 설정**
    
    ```kotlin
    <application
        android:name="com.example.busschedule.BusScheduleApplication"
        ...
    ```
    
    - 기본 클래스 Application이 아닌 BusScheduleApplication 클래스가 사용되도록 바꿈
- ListAdapter 만들기
    
    RecyclerView 사용시 한 항목의 콘텐츠만 변경되어도 전체 새로고침 됨
    
    → ListAdapter는 차이만 반영함 : `AsyncListDiffer` 사용
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-intro-room-flow/img/2ad76f8db0852fe3.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-intro-room-flow/img/2ad76f8db0852fe3.png?hl=ko)
    
    - `BusStopAdapter` 클래스 생성 (어댑터 설정)
        
        ```kotlin
        class BusStopAdapter(
            private val onItemClicked: (Schedule) -> Unit) : ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {
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
            class BusStopViewHolder(private var binding: BusStopItemBinding): RecyclerView.ViewHolder(binding.root) {
                @SuppressLint("SimpleDateFormat")
                fun bind(schedule: Schedule) {
                    binding.stopNameTextView.text = schedule.stopName
                    binding.arrivalTimeTextView.text = SimpleDateFormat(
                        "h:mm a").format(
                        Date(schedule.arrivalTime.toLong() * 1000)
                    )
                }
            }
        
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
                val viewHolder = BusStopViewHolder(
                    BusStopItemBinding.inflate(
                        LayoutInflater.from( parent.context),
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
        }
        ```
        
        - `Schedule` 객체 목록과 UI의 `BusStpoViewHolder` 클래스를 사용하는 일반 ListAdapter를 확장한다.
        
        - `BusStopViewHolder` 구현
            
            🧐데이터 연결 메서드라고 생각하면 될 듯
            
            뷰 홀더 : 뷰에 엑세스 하기 위함
            
            ```kotlin
            class BusStopViewHolder(private var binding: BusStopItemBinding): RecyclerView.ViewHolder(binding.root) {
                @SuppressLint("SimpleDateFormat") // 에러 없이 사용하기 위함
                fun bind(schedule: Schedule) { //여기서 데이터 바인딩해줌
                    binding.stopNameTextView.text = schedule.stopName
                    binding.arrivalTimeTextView.text = SimpleDateFormat(
                        "h:mm a").format(Date(schedule.arrivalTime.toLong() * 1000)
                    )
                }
            }
            ```
            
            - bind()
                - `stopNameTextView`의 텍스트를 정류장 이름으로 설정
                - `arrivalTimeTextView`의 텍스트를 날짜로 설정
        
        - `onCreateViewHolder()` 재정의
            
            🧐 뷰 홀더 클래스는 만들었으니까 이젠 인스턴스 만들 차례
            
            ```kotlin
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
               val viewHolder = BusStopViewHolder(
                   BusStopItemBinding.inflate(
                       LayoutInflater.from( parent.context),
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
            ```
            
            - 레이아웃 확장
            - onItemClicked() 호출을 위한 리스너 설정
        
        - `onBindViewHolder()` 재정의
            
            ```kotlin
            override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
               holder.bind(getItem(position))
            }
            ```
            
            🧐 홀더(버스스탑뷰홀더의 인스턴스)가 bind(버스스탑뷰홀더 클래스 내의 메서드)를 사용하여 데이터를 바인딩 (getItem(position)은 홀더 내에서 현재 데이터 설정할 `1개` 에 대한 위치를 가져옴)
            
        
        - 컴패니언 객체 & DiffCallBack 구현
            
            ```kotlin
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
            ```
            
            - DiffCallBack : 목록 업데이트 시 이전과 달라지는 항목 확인용 객체
            - `areItemsTheSame()` : ID로 객체(데이터베이스의 행)가 이전과 같은지 확인함
            - `areContentsTheSame()` : ID와 모든 속성까지도 같은지 확인함(UPDATE 시 속성값이 변하므로 속성값도 확인 필요)
            
    - 뷰 모델 참조 가져오기
        - `FullScheduleFragment.kt` 에서 뷰 모델 참조 가져오기
            
            ```kotlin
            // 뷰 모델 가져오기
            private val viewModel: BusScheduleViewModel by activityViewModels {
               BusScheduleViewModelFactory(
                   (activity?.application as BusScheduleApplication).database.scheduleDao()
               )
            }
            
            onViewCreated(){ //리사이클러뷰에 할당하기
            ...
            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val busStopAdapter = BusStopAdapter({
               val action = FullScheduleFragmentDirections.actionFullScheduleFragmentToStopScheduleFragment(
                   stopName = it.stopName
               )
               view.findNavController().navigate(action)
            })
            recyclerView.adapter = busStopAdapter
            }
            ```
            
- Flow를 사용하여 데이터 변경사항에 응답
    
    기본 데이터가 업데이트 되더라도 UI를 업데이트 하려고 submitList()가 호출되지는 않음
    
    따라서 `collect()` 를 사용하여 항목이 바뀔때 결과가 프래그먼트로 전송되도록 함
    
    - List<Schedule>로 반환하던 것을 Flow<List<Schedule>>로 변경
        - `ScheduleDao.kt` 바꾸기
        - `ScheduleListViewModel.kt` > `BusScheduleViewModel` 바꾸기
    - collect 사용하게 바꾸기
        - `FullScheduleFragment.kt`
            
            ```kotlin
            ~~busStopAdapter.submitList(viewModel.fullSchedule())~~
            
            lifecycle.coroutineScope.launch {
               viewModel.fullSchedule().collect() {
                   busStopAdapter.submitList(it)
               }
            }
            ```
            
            - fullSchedule()은 정지 함수이므로 코루틴에서 호출
        - `StopScheduleFragment.kt`
            
            ```kotlin
            lifecycle.coroutineScope.launch {
               viewModel.scheduleForStopName(stopName).collect() {
                   busStopAdapter.submitList(it)
               }
            }
            ```