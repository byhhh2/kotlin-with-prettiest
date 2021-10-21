# Pathway2

### Room으로 데이터 유지하기

- Room
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/7521165e051cc0d4.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/7521165e051cc0d4.png?hl=ko)
    
    Room의 아키텍쳐
    
    Jetpack의 일부인 지속성 라이브러리
    
    Jetpack이란?
    
    안드로이드 라이브러리 모음
    
    [](https://developer.android.com/jetpack/?hl=ko)
    
- Room의 기본 구성 요소
    - 데이터 항목 : 앱 데이터베이스 테이블
    - 데이터 액세스 객체 (DAO) : 앱 데이터베이스 SELECT,INSERT,UPDATE..등의 메서드
    - 데이터베이스 클래스 : 데이터베이스 보유 및 액세스 포인트
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/33a193a68c9a8e0e.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/33a193a68c9a8e0e.png?hl=ko)
    
    - 종속성 추가하기 `build.gradle (Module: InventoryApp.app)`
        
        ```kotlin
        // Room
            implementation "androidx.room:room-runtime:$room_version"
            kapt "androidx.room:room-compiler:$room_version"
            implementation "androidx.room:room-ktx:$room_version"
        ```
        
- Item Entitiy 만들기
- Item DAO 만들기
    
    DAO : 데이터 액세스 객체 (INSERT등의 메서드)
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/dcef2fc739d704e5.png](https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/dcef2fc739d704e5.png)
    
- 데이터베이스 인스턴스 만들기
    
    RoomDatabase : 만든 Entitiy와 DAO를 사용하는 데이터베이스 인스턴스
    
    **[RoomDatabase 인스턴스를 가져오는 순서]**
    
    1. RoomDatabase를 확장하는 추상 클래스 생성
        - 추상 클래스는 데이터베이스 홀더 역할
        - @Database 주석 달기
    2. RoomDatabase 인스턴스 하나만 있으면 됨→ RoomDatabase를 싱글톤으로 만든다.
        
        Room의 Room.databaseBuilder를 사용하여 데이터베이스 생성
        
    
    **[예제 상에서의 데이터베이스 만들기]**
    
    1. `ItemRoomDatabase.kt` 에서 `ItemRoomDatabase` 클래스 생성
        - `ItemRoomDatabase` : `RoomDatabase` 를 확장하는 abstract 클래스
        - @Database 주석달기
        - `ItemDao` 를 반환하는 추상 메서드 선언
            
            ```kotlin
            abstract fun itemDao(): ItemDao
            ```
            
        - 컴패니언 객체 선언 : 데이터베이스 인스턴스의 싱글턴을 위함
            
            컴패니언 객체 안에서 volatile 변수로 Instance 선언
            
            ```kotlin
            companion object {
                   @Volatile
                   private var INSTANCE: ItemRoomDatabase? = null
                   fun getDatabase(context: Context): ItemRoomDatabase {
                       return INSTANCE ?: synchronized(this) {
                           val instance = Room.databaseBuilder(
                               context.applicationContext,
                               ItemRoomDatabase::class.java,
                               "item_database"
                           )
                               .fallbackToDestructiveMigration()
                               .build()
                           INSTANCE = instance
                           return instance
                       }
                   }
               }
            ```
            
            - Instance : 휘발성 변수
            - getDatabase 메서드 정의
                - 만약 인스턴스가 존재하면 기존의 인스턴스 반환
                - 없다면 새로 생성
                    - `synchronized{}` 안에서 데이터베이스 초기화
                    - `Room.databaseBuilder()`를 사용하여 데이터베이스 가져오기
                        - 인수
                            - application context
                            - 데이터베이스 클래스
                            - 데이터베이스 이름
                            
                            을 전달해줌
                            
                    - 데이터베이스 빌더를 하고 나면 타입이 다르므로 변수 타입 맞춰주기
                        - `.fallbackToDestructiveMigration()`
                            - 데이터베이스 다시 빌드
                            - 기존 데이터베이스에서 마이그레이션 하는 것
                        - `.build()`
                            - 데이터베이스 인스턴스 생성
        - Application 클래스 구현
            
            ```kotlin
            class InventoryApplication : Application(){
                val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
            }
            ```
            
            - application에서 데이터베이스 인스턴스 생성
- ViewModel 추가
    
    모든 작업은 UI 스레드에서 벗어나 실행됨 → 코루틴&viewModelScope 사용
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/91298a7c05e4f5e0.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-persisting-data-room/img/91298a7c05e4f5e0.png?hl=ko)
    
    1. ViewModel 만들기
        - `InventoryViewModel.kt` 생성
            
            ```kotlin
            class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {}
            ```
            
            - 팩토리 클래스 생성 (인스턴스 생성용 클래스)
                
                ```kotlin
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                   if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
                   @Suppress("UNCHECKED_CAST")
                   return InventoryViewModel(itemDao) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
                }
                ```
                
    2. ViewModel 채우기
        - Entity 클래스 만들기
            
            ```kotlin
            @Entity
                data class Item(
                    @PrimaryKey(autoGenerate = true)
                    val id: Int = 0,
                    @ColumnInfo(name = "name")
                    val itemName: String,
                    @ColumnInfo(name = "price")
                    val itemPrice: Double,
                    @ColumnInfo(name = "quantity")
                    val quantityInStock: Int
                )
            ```
            
        - insert 메서드 만들기
            
            itemDao의 insert메서드 호출 
            
            ❗ViewModelScope에서 코루틴 시작
            
            → ViewModelScope : ViewModel이 소멸될 때 하위 코루틴을 자동으로 취소
            
            ```kotlin
            private fun insertItem(item: Item) {
                    viewModelScope.launch {
                        itemDao.insert(item)
                    }
                }
            ```
            
        - getNewItemEntry : 문자열을 가져오고 Item 인스턴스 반환하는 메서드
            
            ```kotlin
            private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
               return Item(
                   itemName = itemName,
                   itemPrice = itemPrice.toDouble(),
                   quantityInStock = itemCount.toInt()
               )
            }
            ```
            
        - addNewItem : 항목을 getNewItemEntry로 설정하고 insertItem으로 삽입
            
            ```kotlin
            fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
               val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
               insertItem(newItem)
            }
            ```
            
- AddItemFragment 업데이트
    - 뷰 모델 설정하기
        
        ```kotlin
        private val viewModel: InventoryViewModel by activityViewModels {
                InventoryViewModelFactory(
                    (activity?.application as InventoryApplication).database
                        .itemDao()
                )
            }
        ```
        
        - by activityViewModels()  → 프래그먼트 전체에서 뷰 모델 공유
        - InventoryViewModelFactory() : 뷰 모델에서 만든 팩토리 클래스
            
             → 인스턴스를 만듦
            
    - 데이터 삽입시 텍스트 필드가 전부 작성되었는지 확인하는 메서드 만들기
        - 뷰모델에서
            
            ```kotlin
            fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
               if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
                   return false
               }
               return true
            }
            ```
            
        - AddItemFragment에서
            
            ```kotlin
            private fun isEntryValid(): Boolean {
                    return viewModel.isEntryValid(
                        binding.itemName.text.toString(),
                        binding.itemPrice.text.toString(),
                        binding.itemCount.text.toString()
                    )
                }
            ```
            
            → 데이터가 있는 경우 삽입을 실행하는 메서드 작성
            
            ```kotlin
            private fun addNewItem() {
                    if (isEntryValid()) {
                        viewModel.addNewItem(
                            binding.itemName.text.toString(),
                            binding.itemPrice.text.toString(),
                            binding.itemCount.text.toString(),
                        )
                    }
                }
            ```
            
    - 뷰가 생성되면 이 모든 작업을 실행하는 버튼 'save' 에 온클릭 리스너가 필요
        
        → onViewCreated되면 버튼에 리스너 달기
        
        ```kotlin
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)
           binding.saveAction.setOnClickListener {
               addNewItem()
           }
        }
        ```
        

---

### Room을 사용하여 데이터 읽기 및 업데이트

- RecyclerView 추가
    - 확장 함수 : 타사 라이브러리의 클래스에 관한 새 함수 작성 후 일반적인 방법으로 호출하는 것
    - `Item.kt` 에 메서드 추가
        
        ```kotlin
        fun Item.getFormattedPrice(): String =
           NumberFormat.getCurrencyInstance().format(itemPrice)
        ```
        
    - ListAdapter 추가
        
        ```kotlin
        class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
           ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {
        	 //DiffCallback : 변경된 목록 파악 객체
        
        	 //RecyclerView에 필요한 뷰홀더 반환해줌
        	 //view를 만들고 뷰를 레이아웃파일에 확장
           override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
               return ItemViewHolder(
                   ItemListItemBinding.inflate(
                       LayoutInflater.from(
                           parent.context
                       )
                   )
               )
           }
        
        	 //onItemClicked 로 현재 클릭한 Item 객체 가져옴
           override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
               val current = getItem(position)
               holder.itemView.setOnClickListener { 
                   onItemClicked(current) // 현재 클릭한 Item 객체 가져오기
               }
               holder.bind(current)
           }
        
        	 //각 아이템에 텍스트 가져와서 데이터 바인딩 해줌
           class ItemViewHolder(private var binding: ItemListItemBinding) :
               RecyclerView.ViewHolder(binding.root) {
        
               fun bind(item: Item) {
                    binding.apply {
                        itemName.text = item.itemName
                        itemPrice.text = item.getFormattedPrice()
                        itemQuantity.text = item.quantityInStock.toString()
                    }
                }
           }
        
        	 //DiffCallback 변수 정의 : 차이 확인용 객체
           companion object {
               private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
                   override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                       return oldItem === newItem
                   }
        
                   override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                       return oldItem.itemName == newItem.itemName
                   }
               }
           }
        }
        ```
        
    
    - ListAdapter 사용하기
        - 뷰모델에서 가져오기
            
            🧐 itemDao에서 getItems로 데이터를 가져오는데 LiveData로 가져오기
            
            ```kotlin
            val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()
            ```
            
        - `ItemListFragment` 에서 뷰모델 설정
            
            ```kotlin
            private val viewModel: InventoryViewModel by activityViewModels {
               InventoryViewModelFactory(
                   (activity?.application as InventoryApplication).database.itemDao()
               )
            }
            ```
            
        - `ItemListFragment` 에서 어댑터 설정
            
            ```kotlin
            onViewCreated(){
            ...
            val adapter = ItemListAdapter {
            }
            binding.recyclerView.adapter = adapter
            viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
                   items.let {
                       adapter.submitList(it)
                   }
               }
            ...
            }
            ```
            
- 항목 세부정보 표시
    - 클릭 핸들러 추가
        - `ItemListFragment` 에서 어댑터 정의 업데이트
            
            ```kotlin
            val adapter = ItemListAdapter {
                        val action =   ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
                        this.findNavController().navigate(action)
                    }
            ```
            
    - 데이터베이스 검색 및 표시
        - `InventotyViewModel` 에서 데이터 반환 메서드 작성
            
            ```kotlin
            fun retrieveItem(id: Int): LiveData<Item> {
                    return itemDao.getItem(id).asLiveData()
                }
            ```
            
    - textView를 ViewModel 데이터에 바인딩
        - `ItemDetailFragment` 에 Item 속성 추가 및 뷰 모델 추가
            
            ```kotlin
            lateinit var item: Item
            
            private val viewModel: InventoryViewModel by activityViewModels {
               InventoryViewModelFactory(
                   (activity?.application as InventoryApplication).database.itemDao()
               )
            }
            
            //아이템 속성 값들 바이딩
            private fun bind(item: Item) {
               binding.apply {
                   itemName.text = item.itemName
                   itemPrice.text = item.getFormattedPrice()
                   itemCount.text = item.quantityInStock.toString()
               }
            }
            
            //뷰가 생성되면 값 바인딩을 해야함
            //우선 itemId를 가져오고 id에 해당하는 속성값을 
            //retrieveItem으로 가져오고
            //observe를 달고 관찰을 함
            //그와 동시에 가져온 값으로 데이터 바인딩 해주기
            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                    super.onViewCreated(view, savedInstanceState)
                    val id = navigationArgs.itemId
                    viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                        item = selectedItem
                        bind(item)
                    }
                }
            ```
            
- 판매 항목 구현
    - 항목 업데이트
        - 뷰 모델에서 업데이트 함수 구현
            
            ```kotlin
            private fun updateItem(item: Item) {
                    viewModelScope.launch {
                        itemDao.update(item)
                    }
                }
            ```
            
        - 뷰 모델에서 sellItem 함수 구현
            
            copy를 통해 기존 속성 유지하면서 재고를 하나 줄임
            
            ```kotlin
            fun sellItem(item: Item) {
                    if (item.quantityInStock > 0) {
                        // Decrease the quantity by 1
                        val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
                        updateItem(newItem)
                    }
                }
            ```
            
            copy()
            
            일부 속성만 변경하면서 객체를 복사함
            
            ```kotlin
            // Data class
            data class User(val name: String = "", val age: Int = 0)
            
            // Data class instance
            val jack = User(name = "Jack", age = 1)
            
            // A new instance is created with its age property changed, rest of the properties unchanged.
            val olderJack = jack.copy(age = 2)
            ```
            
        - 뷰 모델에서 재고가 있는지 확인 메서드 생성
            
            ```kotlin
            fun isStockAvailable(item: Item): Boolean {
               return (item.quantityInStock > 0)
            }
            ```
            
        - `ItemDetailFragment` 에서 sellItem 및 sell버튼 활성/비활성 설정 호출
            
            ```kotlin
            private fun bind(item: Item) {
            binding.apply {
            
            ...
                sellItem.isEnabled = viewModel.isStockAvailable(item)
            		sellItem.setOnClickListener { viewModel.sellItem(item) }
                }
            }
            ```
            
    
    - 항목 삭제
        - 뷰 모델이 delete메서드 작성
            
            ```kotlin
            fun deleteItem(item: Item) {
               viewModelScope.launch {
                   itemDao.delete(item)
               }
            }
            ```
            
        - `ItemDetailFragment` 에서 뷰모델의 deleteItem 호출
            
            ```kotlin
            private fun deleteItem() {
                    viewModel.deleteItem(item)
                    findNavController().navigateUp()
                }
            ```
            
            bind에서도 delete버튼에 메서드 달기
            
            ```kotlin
            deleteItem.setOnClickListener { showConfirmationDialog() }
            ```
            
    - 항목 수정
        - 기존 데이터 가져오는 bind 메서드 작성 (`AddItemFragment` )
            
            ```kotlin
            val price = "%.2f".format(item.itemPrice)
               binding.apply {
                   itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
                   itemPrice.setText(price, TextView.BufferType.SPANNABLE)
                   itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
               }
            ```
            
        - 값 수정하고 save 누르면 값이 업데이트 되게 하기
            - 뷰 모델에 업데이트용 함수 추가
                
                ```kotlin
                private fun getUpdatedItemEntry(
                   itemId: Int,
                   itemName: String,
                   itemPrice: String,
                   itemCount: String
                ): Item {
                   return Item(
                       id = itemId,
                       itemName = itemName,
                       itemPrice = itemPrice.toDouble(),
                       quantityInStock = itemCount.toInt()
                   )
                }
                fun updateItem(
                        itemId: Int,
                        itemName: String,
                        itemPrice: String,
                        itemCount: String
                    ) {//getUpdatedItemEntry로 반환된 값을 updateItem 객체로 설정
                        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
                				updateItem(updatedItem)    
                }
                ```
                
            - `AddItemFragment` 에 업데이트 함수 추가
                
                ```kotlin
                private fun updateItem() {
                   if (isEntryValid()) {
                       viewModel.updateItem(
                           this.navigationArgs.itemId,
                           this.binding.itemName.text.toString(),
                           this.binding.itemPrice.text.toString(),
                           this.binding.itemCount.text.toString()
                       )
                       val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
                       findNavController().navigate(action)
                   }
                }
                ```
                
            - bind에 온클릭 리스너 추가