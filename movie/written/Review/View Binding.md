## View Binding

<br>

### gradle

<br>

```
// gradle (module)

android {

    ..

    buildFeatures {
        viewBinding = true
    }
}

```

<br>

> 결합 클래스의 이름은 XML 파일의 이름을 카멜 표기법으로 변환하고 이름 끝에 'Binding'을 추가하여 생성됩니다. 마찬가지로 각 뷰를 위한 참조는 밑줄을 삭제하고 뷰 이름을 카멜 표기법으로 변환하여 생성됩니다. 예를 들어 activity_main.xml은 ActivityMainBinding이 되고 binding.textView로 @id/text_view에 액세스할 수 있습니다.

<br>

### Fragment에서의 설정

<br>

```kotlin
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding //연결된 xml파일 이름 카멜로 + Binding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle? //기본 설정
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //inflate

        binding.textHome.text = "안녕";
        //xml파일의 text_home의 text 변경

        return binding.root
        //binding.root를 반환해야함
    }
}
```

### Activity에서의 설정

<br>

```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater) //inflate

        setContentView(binding.root)
    }
}
```
