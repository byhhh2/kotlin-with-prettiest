# [Unit 3] Pathway2

- 바인딩을 까먹었다면 ~?
    
    [https://duckssi.tistory.com/42](https://duckssi.tistory.com/42)
    
    뷰바인딩 사용하기 전 활성화 시켜주기~
    
    Gradle → buildFeatures → viewBinding true
    
- context
    
    설명) Interface to global information about an application environment. This is an abstract class whose implementation is provided by the Android system. It allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
    
    getContext 와 requireContext의 차이)  [https://4z7l.github.io/2020/11/22/android-getcontext-requirecontext.html](https://4z7l.github.io/2020/11/22/android-getcontext-requirecontext.html)  → 요약: require는 fragment가 이것의 호스트에 확실히 붙어있을 때만 사용
    

## 프래그먼트

앱의 사용자 인터페이스에서 재사용 가능한 부분, 수명 주기가 존재

- 수명주기 (5가지 상태)

INITIALIZED : 프래그먼트의 새 인스턴스가 인스턴스화

CREATED : 첫 번째 프래그먼트 수명 주기 메서드가 호출. 프래그먼트와 연결된 뷰 생성.

STARTED : 프래그먼트가 화면에 표시되지만 '포커스'가 없으므로 사용자 입력에 응답 불가능.

RESUMED : 프래그먼트가 표시되고 포커스가 있음.

DESTROYED : 프래그먼트 객체의 인스턴스화가 취소.

- 메서드

`onCreate()` 프래그먼트가 인스턴스화.  CREATED 상태. but 상응하는 뷰는 없는 상태.

`onCreateView()`  레이아웃을 확장. CREATED 상태로 전환.

`onViewCreated()`  뷰가 만들어진 후 호출.  `findViewById()` 를 호출 해 특정 뷰를 속성에 바인딩.

`onStart()`  STARTED 상태로 전환.

`onResume()`  RESUMED 상태로 전환. 포커스를 보유(사용자 입력에 응답 가능).

`onPause()` 프래그먼트가 STARTED 상태로 다시 전환. UI가 사용자에게 표시.

`onStop()` CREATED 상태로 다시 전환. 객체가 인스턴스화 but 더 이상 화면에 표시되지 않음.

`onDestroyView()` DESTROYED 상태로 전환되기 직전에 호출. 

                       뷰는 메모리에서 이미 삭제되었지만 프래그먼트 객체는 존재.

`onDestroy()` DESTROYED 상태로 전환.

MainActivity → LetterListFragment

```kotlin
private var _binding: FragmentLetterListBinding? = null
private val binding get() =_binding!!
private lateinit var recyclerView: RecyclerView
private var isLinearLayoutManager= true

class LetterListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
				_binding= FragmentLetterListBinding.inflate(inflater, container, false)
        val view =binding.root
				return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
				recyclerView = binding.recyclerView
        chooseLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
				_binding= null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater ){
        inflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
						recyclerView.layoutManager= LinearLayoutManager(context)
        } else {
						recyclerView.layoutManager= GridLayoutManager(context, 4)
        }
						recyclerView.adapter= LetterAdapter()
	   }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

	      menuItem.icon=
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout-> {
								isLinearLayoutManager= !isLinearLayoutManager
								chooseLayout()
                setIcon(item)

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
```

 Activity  클래스에는  menuInflater라는 전역속성이 있지만 프래그먼트에는 없음.

메뉴 인플레이터가 대신 onCreateOnptionsMenu()로 전달.

 프래그먼트는 Context가 아니므로 this를 레이아웃 관리자의 컨텍스트로 전달 불가능.

대신 사용할 수 있는 context 속성을 제공

 바인딩 변수 추가 방법

 여기서 get() 은 get-only를 의미. 값을 가져올 수 있지만 여기서 할당되고 나면 다른 것에 할당 불가

## Jetpack 탐색 구성요소

- 탐색 그래프 : 앱에서 탐색을 시각적으로 보여주는 XML 파일.
- NavHost : 활동 내에서 탐색 그래프의 대상을 표시하는 데 사용.
    
                     MainActivity에서 NavHostFragment라는 기본 제공 구현을 사용.
    
- NavController :  NavHost에 표시되는 대상 간 탐색을 제어 가능. 탐색을 식시.
    
                              `navigate()` 메서드를 호출하여 표시되는 프래그먼트를 교체.
    
    NavHostFragment : 목적지를 담는 컨테이너
    

MainActivity.xml

```kotlin
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true" />

</FrameLayout>
```