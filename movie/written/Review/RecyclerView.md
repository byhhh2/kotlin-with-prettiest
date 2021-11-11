- item : 객체 하나
- Adapter : `RecyclerView`에 표시할 수 있도록 데이터를 가져와 준비
- ViewHolder : `RecyclerView`용 뷰 풀
- RecyclerView : 화면에 표시되는 뷰

<br>

```
-- RecyclerView
  ⊦ RecyclerView의 Item의 레이아웃 (list_item.xml) (연결은 Adapter에서)

```

<br>

### LayoutManager

- Linear나 Grid와 같은 다양한 방식으로 item을 표시하도록
- 만약에 세로 목록으로 표시하고 싶다? `LinearLayoutManager`를 사용
- 세로 목록으로 스크롤 가능하게 하려면 스크롤바 추가해야한다.

```xml
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical" //
        app:layoutManager="LinearLayoutManager" //
    />
```

<br>

### Adapter

- 데이터를 RecyclerView의 항목으로 표시할 수 있도록 **형식 지정**

```kotlin
// ViewHolder 유형으로 중첩 class(ViewHolder)를 지정해주고 RecyclerView.Adapter를 확장
class ItemAdapter(private val context: Context, private val dataset: List<ItemType>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    // ViewHolder
    class ItemViewHolder() : RecyclerView.ViewHolder(view) {
        // RecyclerView에 사용할 View들 할당
        // 만약 Text면 TextView ...
    }

    // ViewHolder를 새로 만듦 (레이아웃 관리자가 호출)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
                            .inflate(R.layout.list_item, parent, false)
        // 뷰 계층 구조를 추가
        // RecyclerView에서 사용할 Item View가 list_item.xml이니깐 이걸 추가해주면 된다.

        return ItemViewHolder(adapterLayout)
        // ItemViewHolder 인스턴스 반환
    }

    // getItemCount : 데이터 세트의 크기 반환 해야함
    override fun getItemCount(): Int {
        return dataset.size
    }

    // item View의 컨텐츠를 바꾸기 위해
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text =  context.resources.getString(item.stringResourceId) // 이런식으로 컨텐츠를 바꿈

        holder.itemView.setOnClickListener {
            // ..
        }
    }
}
```

<br>

### `RecyclerView` 사용하기 (`Adapter` 연결)

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize data.
        val myDataset = Datasource().loadAffirmations()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view) // RecyclerView 가져옴
        recyclerView.adapter = ItemAdapter(this, myDataset) // Adapter 연결
                                        //context, dataset

        recyclerView.setHasFixedSize(true) // 성능 향상
    }
}
```
