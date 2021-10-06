# Pathway3

### Kotlin 목록

- 읽기 전용 목록 : List (만든 후 수정 불가)
    
    ```kotlin
    val numbers: List<Int> = listOf(1, 2, 3, 4, 5, 6)
    val numbers = listOf(1, 2, 3, 4, 5, 6)
    
    // size = len
    println("Size: ${numbers.size}") 
    
    // 첫번째 요소
    println("First: ${numbers.first()}") 
    
    // 마지막 요소
    println("Last: ${numbers.last()}") 
    
    // 요소포함 여부, 반환값:Boolean
    println("Contains 4? ${numbers.contains(4)}") 
    
    // reversed()와 sorted()는 새로운 목록을 반환함
    println("Reversed list: ${colors.reversed()}") 
    ```
    
- 변경 가능한 목록 : MutableList
    - 원소 추가
        - 1개 : add()
        - 여러개 : addAll()
    - 원소 삭제
        - 요소 : remove()
        - 인덱스 : removeAt()
        - 전체 : clear()
    - 요소 확인 : isEmpty()
        - 반환값 : Boolean (비었으면 True)
    
    ```kotlin
    // 요소 없이 list를 만들려고 할 경우 유형을 모르기때문에 오류
    val entrees = mutalbleListOf()
    
    // 아래와 같이 유형 명시하기
    val entrees = mutableListOf<String>()
    val entrees: MutableList<String> = mutableListOf()
    
    // 원소 추가하기
    // 1개 : add()
    entrees.add("noodles")
    // 여러개 : addAll()
    val moreItems = listOf("ravioli", "lasagna", "fettuccine")
    entrees.addAll(moreItems)
    
    // 원소 삭제하기
    // 요소 : remove()
    entrees.remove("rice")
    // 인덱스 : removeAt()
    entrees.removeAt(0)
    // 전체
    entrees.clear()
    
    // 요소 확인
    entrees.isEmpty()
    ```
    
     반복문 사용 예시 (for문)
    
    ```kotlin
    for (item in list)
    
    // 설정된 범위에서 진행 
    for (item in 'b'..'g') 
    for (item in 1..5) 
    
    // 역행 
    for (item in 5 downTo 1) 
    
    // 2개씩 증가
    for (item in 3..6 step 2) 
    ```
    
    vararg
    
    클래스나 메서드에서 매개변수로 동일한 유형이지만 가변적인 개수를 보내고 싶을때
    
    ```kotlin
    class Vegetables(vararg val toppings: String) : Item("Vegetables", 5)
    
    // 3개 또는 1개 둘 다 가능
    val vegetables = Vegetables("Cabbage", "Sprouts", "Onion")
    val vegetables = Vegetables("Onion")
    ```
    
    vararg는 매개변수 중에서 하나만 가능,
    
    joinToString() : 배열을 String 한 줄로 만들어버림, ()사이에 들어오는 값으로 각 항목 사이를 구분
    

---

### RecycleView를 사용하여 스크롤 가능한 목록 표시하기

- 패키지
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/809a0d77a0759dc5.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/809a0d77a0759dc5.png?hl=ko)
    
    - 패키지 구성
        
        com.example.affirmations : 코드용 패키지
        
        com.example.affirmations (androidTest) : 테스트 파일용 패키지
        
        com.example.affirmations (test) : 테스트 파일용  패키지
        
    
    - 패키지 구분
        
        데이터 작업용 클래스 & UI 으로 보통 나눔
        
    - 패키지 이름
        
        구체적인 순서로 구성
        
        각 부분을 소문자로 표기
        
        마침표로 구분
        
        일반적으로 com.example 다음에 앱 이름을 사용
        
    - 패키지 만들기
        
        Android뷰에서 → `app` > `java` > `com.example.affirmations` 우클릭 > New > Package
        
        - 클래스 만들기
            - Data Class (데이터 소스를 가지는 클래스)
                
                **Data Class**
                
                [Data Class 정의] 
                
                class 앞에 data 붙이기
                
                [Data Class 요구사항]
                
                1. 최소한 한 개의 매개변수를 갖는 기본 생성자를 가져야한다.
                2. 기본 생성자의 매개변수에 val /var 가 지정되어야 한다.
                3. abstract, open, sealed, inner 키워드는 사용 불가하다.
            - 데이터 소스가 되는 클래스

- 앱에 RecycleView 추가하기
    - **RecycleView**
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/4e9c18b463f00bf7.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/4e9c18b463f00bf7.png?hl=ko)
        
        - Item : 각각의 표시할 목록 데이터
        - Adapter : RecycleView에서 표시할 수 있도록 item을 가져와 준비한다.
        - ViewHolder : 확인을 표시하기 위해 사용하거나 재사용할 RecyclerView용 뷰의 풀
        - RecyclerView : 화면에 표시되는 뷰
        
        - RecycleView 실행 순서
            1. RecycleView가 어댑터를 사용하여 화면에 데이터를 표시하는 방법을 파악함
            2. RecycleView가 목록의 첫 번째 데이터 항목을 위한 새 목록 항목 뷰를 만들도록 어댑터에 요청함
            3. 뷰가 생성된 후 항목의 데이터를 제공하도록 어댑터에 지속적인 요청을 보냄
            4. 화면에 채울 뷰가 더이상 필요하지 않을 때 까지 반복 (화면 꽉 차면 더이상 요청 보낼 필요가 없음)
    
    - **View 구성하기** : RecycleView 추가하기
        1. 레이아웃 변경
            
            현재 레이아웃 :ConstraintLayout (한 레이아웃에 하위 뷰 여러개때 적합)
            
            변경 후 레이아웃 :  FrameLayout (한 레이아웃에 단일 하위뷰일때 적합)
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/9e6f235be5fa31a8.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-recyclerview-scrollable-list/img/9e6f235be5fa31a8.png?hl=ko)
            
        2. Design 뷰에서 `RecycleView` 추가
            
            RecycleView의 항목 정렬은 LayoutManager에서 처리
            세로의 경우 LinearLayoutManager
            
            ```kotlin
            app:layoutManager="LinearLayoutManager"
            ```
            
        3. 세로 스크롤 바 추가
            
            화면보다 긴 항목을 스크롤하기 위함
            
            ```kotlin
            android:scrollbars="vertical"
            ```
            
        
    - **Adapter 구성하기 & ViewHoler 구성하기**
        
        Datasource에서 데이터를 가져와 Affirmation을 RecycleView의 항목으로 표시할 수 있도록 형식을 지정하는 방법이 필요함
        
        - Adapter : 데이터를 RecycleView에서 사용할 수 있는 형식으로 조정하는 설계 패턴
        
        [연습]
        
        Adapter → Recycleriew에 표시할 수 있도록 loadAffirmations()에서 반환된 목록에서 Affirmation인스턴스를 가져와 목록 항목 뷰로 전환
        
        - **Adapter 만들기**
            - 항목의 레이아웃 만들기
                
                RecyclerView의 각 항목에는 고유한 레이아웃이 있음.  이 레이아웃은 별도의 레이아웃 파일에서 정의
                
                `res` > `layout` 에 새로운 `list_item.xml`만들기
                
            - ItemAdapter 클래스 만들기
                
                `adapter` 패키지 > `ItemAdapter` 클래스 생성
                
                ```kotlin
                package com.example.affirmations.adapter
                
                import android.content.Context
                import com.example.affirmations.model.Affirmation
                
                // context : ItemAdapter 인스턴스에 전달할 수 있는 Context 객체 인스턴스에 저장
                // ItemAdapter에서 문자열 리소스를 확인하는 방법과 기타 앱 관련 정보가 context에 담김
                class ItemAdapter(private val context: Context, private val dataset: List<Affirmation>) {
                }
                ```
                
            
            - ViewHolder 만들기
                
                RecyclerView는 ViewHolders를 통해 항목 뷰와 간접 상호작용한다.
                
                - ViewHolders : RecyclerView의 단일 목록 항목 뷰를 나타냄
                
                `ItemViewHolder`를 생성
                
                - `ItemAdapter` 클래스 안에 중첩 클래스로 `ItemViewHolder`를 생성함 → `ItemAdapter`만 사용하므로
                - `RecyclerView.ViewHolder`의 서브클래스로 생성
                
                ```kotlin
                class ItemAdapter(
                    private val context: Context,
                    private val dataset: List<Affirmation>
                ) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
                
                    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
                        val textView: TextView = view.findViewById(R.id.item_title)
                    }
                }
                ```
                
                RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() : 상속받으면서 <>안의 정보를 넘겨주는 느낌 (사용하겠다)
                
            - Adapter 메서드 재정의
                
                추상 클래스 RecyclerView.Adapter 에서 ItemAdapter를 확장
                
                - getItemCount() 구현
                    
                    데이터 세트의 크기 반환
                    
                    ```kotlin
                    // 간결한 버전
                    override fun getItemCount() = dataset.size
                    
                    override fun getItemCount(): Int {
                        return dataset.size
                    }
                    ```
                    
                - onCreateViewHolder() 구현
                    
                    RecyclerView의 새 뷰 홀더를 만들기 위해 레이아웃 관리자가 호출하는 메서드
                    
                    [매개변수]
                    
                    - parent : 새 목록 항목 뷰가 하위 요소로 사용되어 연결되는 뷰 그룹
                    - viewType
                        
                        동일한 항목 뷰 유형을 가진 뷰만 재활용할 수 있습니다.
                        
                    
                    ```kotlin
                    val adapterLayout = LayoutInflater.from(parent.context)
                           .inflate(R.layout.list_item, parent, false)
                    ```
                    
                    ⇒ list_item 레이아웃을 틀로 사용하겠다
                    
                    → list_item 레이아웃이 parent 정보에 있으므로 parent의 context에서 list_item 레이아웃 정보를 가져와 객체화를 진행
                    
                    - 반환값 : ItemViewHolder(adapterLayout)
                        
                        만든 틀을 반환함 (ItemViewHolder의 틀(뷰)을 지정해둔 상태로 반환)
                        
                - onBindViewHolder() 구현
                    
                    틀이 정해졌으면 그 안의 콘텐츠를 바꿔야함
                    
                    [매개변수]
                    
                    holder : onCreateViewHolder에서 생성된 ItemViewHolder
                    
                    position : 데이터 목록(Affirmation)에서 현재 목록의 위치를 나타내는 Int
                    
                    1. 데이터 가져오기
                        
                        ```kotlin
                        val item = dataset[position]
                        ```
                        
                    2. 가져온 데이터 설정해주기
                        
                        ```kotlin
                        holder.textView.text = context.resources.getString(item.stringResourceId)
                        ```
                        
            - RecyclerView를 사용하도록 MainActivity 수정
                1. 데이터 셋 객체 만들기
                2. 뷰의 recyclerview  찾아서 변수에 할당하기
                3. 할당한 변수(recyclerview)에 어댑터 객체 만들어서 할당하기
                4. RecyclerView의 레이아웃 크기가 고정되어 있음을 설정하기
                
                ```kotlin
                // 1
                val myDataset = Datasource().loadAffirmations()
                
                // 2
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                
                // 
                recyclerView.adapter = ItemAdapter(this, myDataset)
                
                recyclerView.setHasFixedSize(true)
                ```
                

---

### 카드를 사용하여 이미지 목록 표시

- 이미지 추가
    
    app/src/main/res/drawable 디렉토리에 추가
    

- Affirmation 클래스에서 이미지 지원 추가
    
    > 리소스 주석
    > 
    > 
    > → 메서드 또는 매개변수에 부가적인 정보를 추가함
    > 
    > → @ 기호로 선언
    > 
    
    ```kotlin
    import androidx.annotation.DrawableRes
    import androidx.annotation.StringRes
    
    data class Affirmation(
       @StringRes val stringResourceId: Int,
       @DrawableRes val imageResourceId: Int
    )
    ```
    

- Datasource 클래스에 누락된 imageResourceId 추가

- 목록 항목 레이아웃에 ImageView추가
    
    `list_item.xml` 수정
    
    기존 : TextView 하나
    
    변경 후 : View가 하나 늘었으므로 ViewGroup 추가 → LinearLayout과 ImageView추가
    

- ItemAdapter 업데이트
    - ItemViewHolder에 imageView 추가
    - onBindViewHolder() 에서 holder에 이미지 바인딩
    
- UI 수정
    - 패딩 추가 및 테마 설정
        - list_item에서 수정
            
            TextView에 `textAppearanceHeadline6` 적용
            
    - 카드 사용 : MaterialCardView
        
        LinearLayout 주변을 MaterialCardView로 감싸기
        
    - 색상 리소스 추가
        
        `colors.xml` 에 추가
        
    - 테마 색상 변경