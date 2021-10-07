
## Android 아키텍쳐   
* 앱 아키텍처를 안내하는 역할
* 일련의 디자인 규칙. 앱의 구조 제시
<br><br>
 
### 아키텍처 원칙
* 관심사 분리 : Activity, Fragment 등 UI 기반 클래스는 UI 및 OS의 상호작용 처리 로직만 포함해야함.
* 모델에서 UI 만들기
<br>

<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/53dd5e42f23ffba9.png?hl=ko" style="width:80%; margin-top: 20px; ">

<br><br><br>

### UI Controller (Activity/Fragment)
* 화면에 뷰를 그리고 사용자 이벤트, 사용자가 상호작용하는 UI 관련 동작을 캡처하여 UI를 제어함
* 데이터와 관련된 모든 의사 결정 로직 포함 X

<br><br><br>

## ViewModel
* 뷰에 표시되는 데이터의 모델
* model : 앱의 데이터 처리를 담당하는 구성요소
* Activity/Fragment 가 소멸되고 재생성될 때 폐기되지 않는 앱 관련 데이터 저장
  <br><br>

### [1] ViewModel 추가
  1. build.gradle(Module:Unscramble.app) > dependencies 에 ViewModel 라이브러리 종속 항목 추가  
        ```gradle
        implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
        ```
  2. GameViewModel 클래스 생성 후 `ViewModel`의 서브 클래스로 확장

<br><br>

### [2] ViewModel - Fragment 연결
  1. GameFragment 클래스 상단에 `GameViewModel`의 객체 인스턴스 생성
  2. [kotlin 속성 위임](#kotlin-속성-위임) 사용해 객체 초기화
  3. import androidx.fragment.app.viewModels
    ```kotlin
    private val viewModel: GameViewModel by viewModels()
    ```
  4. Activity/Fragment의 데이터를 ViewModel로 이동 : [지원속성](#지원-속성-backing-property)

<br><br><br>

### kotlin 속성 위임
* `var` : `getter` / `settet` 함수 자동 생성 <br>
* `val` : `getter` 함수 생성 필요
* 속성 위임을 사용하면 getter-setter 책임을 다른 클래스(delegate class 대리자 클래스)에 넘길 수 있음
  
```kotlin
var <property-name> : <property-type> by <delegate-class>()
private val viewModel = GameViewModel()
```

<br><br><br>

### 지원 속성 Backing property
```kotlin
class GameViewModel : ViewModel() {

    private var _count = 0
    val count: Int
        get() = _count

    private var _currentScrambledWord = "test"
    val currentScrambledWord: String
        get() = _currentScrambledWord   
    ...
}
```
```kotlin
class GameFragment : Fragment() {
    private val viewModel : GameViewModel by viewModels()
    ...
    viewModel.currentScrambledWord
    ...
}
```

* `_[name]` : ViewModel 내부에서 접근, 수정
* `[name]` : ViewModel 외부에서 접근

<br><br><br>

### ViewModel Lifecycle
* 연결된 Fragment 분리, Activity 완료 시 소멸. 소멸 직전 `onCleared()` 메서드 호출 
* Activity/Fragment 소멸 시 `onDetach()`메서드 호출

<img src = "https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/18e67dc79f89d8a.png" style="width:80%; height:80%; margin-top: 20px; ">

 


<br><br><br>

### 지연 초기화
* 변수 선언 시 값을 할당하지 않고 나중에 할당
* `lateinit` 키워드 사용

<br><br><br>

### 대화상자
* MaterialAlertDialogBuilder 클래스 사용
* context : 애플리케이션, 활동, 프래그먼트의 컨텍스트나 현재 상태를 의미. 리소스, 데이터베이스, 기타 시스템 서비스 접근 시 사용
```kotlin
private fun showFinalScoreDialog() {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.congratulations))
        .setMessage(getString(R.string.you_scored, viewModel.score))
        .setCancelable(false)       //뒤로 버튼으로 대화상자 취소x
        .setNegativeButton(getString(R.string.exit)) { _, _ -> exitGame() }  //후행 람다 구문
        .setPositiveButton(getString(R.string.play_again)) { _, _ -> restartGame() }
        .show()
}
```
> 후행 람다 구문 <br>
> 람다가 함수의 마지막 인자로 전달시 함수 밖에 배치 가능

<br><br><br>

### 텍스트 필드에 오류 표시
![이미지](https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/18069f0e6b2fddbc.png)
```kotlin
private fun setErrorTextField(error: Boolean) {
   if (error) {
       binding.textField.isErrorEnabled = true
       binding.textField.error = getString(R.string.try_again)
   } else {
       binding.textField.isErrorEnabled = false
       binding.textInputEditText.text = null
   }
}
```
<br><br><br>

## LiveData
* 수명 주기를 인식하는 식별 가능한 데이터 홀더 클래스
* 특성
  - 데이터 보유 : 모든 유형 데이터 사용 가능
  - 관찰 가능 : 객체가 보유한 데이터가 변경되면 관찰자(observer)에게 알림
  - lifecycle 인식 : 객체에 관찰자를 연결하면 관찰자는 LifecycleOwner(Activity/Fragment)와 연결됨. LiveData는 `STARTED`/`RESUMED` 등 활성 수명주기 상태인 관찰자만 업데이트함

<br><br><br>

### [1] LiveData 추가
* MutableLiveData : 변경 가능한 LiveData
* LiveData 객체의 데이터에 접근하려면 `value` 속성 사용
```kotlin
class GameViewModel : ViewModel() {
    ...
    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord  
    ...
}
```
```kotlin
class GameFragment : Fragment() {
    ...
    _currentScrambledWord.value = String(tempWord)
    ...
}
```
  
<br><br><br>

### [2] LiveData 객체에 관찰자 연결
1. `onViewCreated()` 메서드에서 LiveData객체에 대해 `observe()` 메서드 호출
2. `observe()` 첫번째 매개변수로 `viewLifecycleOwner`(: Fragment 뷰 lifecycle 표현) 전달 <br>
   : LiveData가 Fragment의 수명주기를 인식하고 활성상태일 때만 관찰자에게 전달
3. `observe()` 두번째 매개변수로 람다 추가
```kotlin
viewModel.currentScrambledWord.observe(viewLifecycleOwner,
   { newWord -> binding.textViewUnscrambledWord.text = newWord })
```

<br><br><br>

### [3] Data Binding & LiveData 
```kotlin
// UI Controller - view binding
binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord

// layout file - data binding
android:text="@{gameViewModel.currentScrambledWord}"
```
* 뷰결합을 데이터 결합으로 변경
  : 레이아웃 xml 파일용 결합 클래스 자동 생성<br> 
  (file name : activity_main.xml / class name : ActivityMainBinding)
  1. build.gradle(Module) > buildFeatures > dataBinding 속성 사용
  2. build.gradle(Module) > kotlin-kapt 플러그인 적용
    ``` gradle
    buildFeatures {
        dataBinding = true
    }
    ...
    plugins {
        id 'com.android.application'
        id 'kotlin-android'
        id 'kotlin-kapt'
    }
    ```
<br>

* 데이터 결합 레이아웃으로 변환
  1. 루트 요소 <layout> 태그로 태핑 <br>
   : game_fragment.xml > 루트 요소 우클릭 > Show Context Actions > Convert to data binding layout
  2. Fragment의 `onCreateView()`에서 `binding` 변수의 인스턴스화 변경
    ```kotlin
    // 변경 전
    binding = GameFragmentBinding.inflate(inflater, container, false)

    // 변경 후
    binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
    ```
  3. 데이터 결합 변수 추가
    ``` xml
    <data>
        <variable
            name="gameViewModel"
            type="com.example.android.unscramble.ui.game.GameViewModel" />
        <variable
            name="maxNoOfWords"
            type="int" />    
    </data>
    ```
  4. Fragment의 `onViewCreated()` 메서드에서 레이아웃 변수 초기화, 수명 주기 소유자를 전달
    ``` kotlin
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        binding.lifecycleOwner = viewLifecycleOwner
        ... 
    }
    ```
<br>

* 결합표현식 사용 (ex : android:[속성] = "@{[레이아웃변수].[속성]}" )
    ``` xml
    <TextView
        android:id="@+id/textView_unscrambled_word"
        ...
        android:text="@{gameViewModel.currentScrambledWord}"
        .../>
    ```

 
 
  

<br><br><br>
<hr>
<br>


### <퀴즈>

* viewModel이 파괴될 때 <br>
a. onDestroy() 메소드 호출 이후 (구성 변경이 아닌 경우) <br>

* Activity/Fragment 에서 시간이 오래걸리는 작업, I/O 요청을 해야한다<br>
a. false <br>

* UI controller 대신 ViewModel에서 LiveData를 초기화하는 이유 <br>
a. ViewModel, LiveData 모두 lifecycle을 인식하기 때문에<br>
a. UI controller가 파괴될 때 LiveData를 유지하기 위해 <br>
a. 구현정보를 분리하여 app을 유연하게 만들기 위해<br>

* ViewModel이 View 또는 Lifecycle 클래스에 직접 접근 가능하다 <br>
a. false <br>
 