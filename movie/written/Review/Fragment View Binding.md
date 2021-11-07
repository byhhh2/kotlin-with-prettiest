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
