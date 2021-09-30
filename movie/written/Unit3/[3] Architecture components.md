# Architecture components

<br>

## Unscramble 앱

- 뒤죽박죽인 단어 맞추기 앱
- 단어 맞추면 점수 획득
- 재시도 가능
- 단어 건너뛰기 가능

<br>

### 시작 코드의 문제

- 점수 채점안함
- 종료 방법 없음
- 기기 회전시 점수 초기화
  - 이건 `onSaveInstanceState()`로 해결할 수 있는데 이것으로 해결하려면 저장할 수 있는 데이터 양이 최소이다.

<br>

### 코드 분석

#### game_fragment.xml

- 게임화면 레이아웃

#### main_activity.xml

- `FragmentContainerView` 존재
  - 단일 `game_fragment`가 존재

#### ui.game > ListOfWords.kt

- 게임에 쓰이는 단어 목록
- 최대 단어 수, 점수

#### ui.game > GameFragment.kt

- 유일한 fragment

<br>
<br>
<br>

---

## 아키텍처 원칙

1. 관심사 분리

   - Activity나 Fragment에 모든 코드를 작성하면 수명주기 관련 문제 발생가능성 높음

2. 모델에서 UI 도출
   - 모델 : 데이터 처리를 담당하는 구성요소
   - View와 독립적으로 앱의 수명주기 관련 문제에 영향을 받지 않음

<br>

## 아키텍처 구성요소

- UI 컨트롤러 (activity, fragment)
  - 뷰를 그리고 사용자가 상호작용하는 UI 동작을 캡처하여 UI 제어
  - 데이터에 대한 로직은 컨트롤러 클래스에 포함되어서는 안됨 (ViewModel에)
  - UI 컨트롤러 [단어표시, 점수표시 ..], ViewModel [단어고르기, 점수계산 ..]
- ViewModel
  - 앱 데이터 모델
  - 앱의 데이터 처리
  - UI 컨트롤러가 소멸되고 다시 생성될 때까지 폐기되지 않는 데이터를 저장
  - 구성이 변경되는 동안 자동으로 유지
- LiveData
- Room

---

<br>
<br>

### ViewModel 추가하기

> MainActivity -> GameFragment -> GameViewModel

1. ui.game 우클릭 > new > kotlin file/class
2. `GameViewModel` class 만들기

```kotlin
  private val viewModel: GameViewModel by viewModels() //[1]
}
```

3. `GrameFragment` 내에 `GameViewModel` 객체 인스턴스 만들기

```kotlin
private val viewModel: GameViewModel by viewModels() //[1]
```

- [1] : 속성 위임

<br>
<br>

---

### 속성 위임

- var : 기본 getter, setter 존재
- val : getter만 존재

- 속성 위임을 사용하면 get-setter 책임을 다른 클래스에 넘기낟.
  - `viewModel` 객체의 책임을 `viewModels`라는 별도의 클래스에 위임
  - 대리자 클래스 = `viewModels`

> 만약 `private val viewModel = GameViewModel()` 이런식으로 뷰 모델을 참조하면 기기 구성이 변경되면 참조상태를 손실하게 된다. 대리자 클래스를 사용하면 대리자 클래스는 첫번째 access에 자동으로 `viewModel`객체를 만들고 이 값을 구성 변경 중에도 유지했다가 요청이 있을 때 반환 한다.

---

<br>
<br>

### ViewModel로 데이터 이동하기

```kotlin
class GameViewModel : ViewModel() {
    private var score = 0
    private var currentWordCount = 0

    private var _currentScrambledWord = "test"
    val currentScrambledWord: String //public [1]
        get() = _currentScrambledWord
}
```

- 지원 속성을 사용
- 모델에 있는 데이터를 private로 ViewModel에서만 수정할 수만 있도록 하고 접근은 할 수 있도록(public) 한다면 지원 속성을 사용한다.

<br>
<br>

---

### 지원 속성

- getter와 setter 중 하나 또는 두개를 재정의 해서 맞춤 동작 제공가능

[1]

- 모델 내부
  `_currentScrambledWord` : private로 모델 내부에서만 변경가능

- 모델외부
  `currentScrambledWord` : public으로 접근은 가능하지만 변경 불가
  `viewModel.currentScrambledWord`로 access

---

<br>
<br>

## ViewModel의 수명주기

<br>

![image](https://user-images.githubusercontent.com/52737532/135418596-cb827f70-a6ec-4f6b-ac83-f4e138cbb1f6.png)

<br>

### 로그를 추가해서 이해하기

```kotlin
//GameViewModel
class GameViewModel : ViewModel() {
   init {
       Log.d("GameFragment", "GameViewModel created!")
   }

   ...
}

override fun onCleared() {
    super.onCleared()
    Log.d("GameFragment", "GameViewModel destroyed!")
}
```

- init : 인스턴스가 처음 생성되어 초기화 될 때 실행
- onCleared() : fragment가 분리되거나 활동이 종료되면

```kotlin
//GameFragment
override fun onCreateView(
   inflater: LayoutInflater, container: ViewGroup?,
   savedInstanceState: Bundle?
): View {
   binding = GameFragmentBinding.inflate(inflater, container, false)
   Log.d("GameFragment", "GameFragment created/re-created!")
   return binding.root
}

override fun onDetach() {
    super.onDetach()
    Log.d("GameFragment", "GameFragment destroyed!")
}
```

- `onCreateView()` : 처음 생성 + 구성 변경시 실행
- `onDetach()` : fragment가 소멸될 때

<br>
<br>

```
com.example.android.unscramble D/GameFragment: GameFragment created/re-created!
com.example.android.unscramble D/GameFragment: GameViewModel created!
com.example.android.unscramble D/GameFragment: GameFragment destroyed!
com.example.android.unscramble D/GameFragment: GameFragment created/re-created!
com.example.android.unscramble D/GameFragment: GameFragment destroyed!
com.example.android.unscramble D/GameFragment: GameFragment created/re-created!
com.example.android.unscramble D/GameFragment: GameFragment destroyed!
com.example.android.unscramble D/GameFragment: GameFragment created/re-created!
com.example.android.unscramble D/GameFragment: GameFragment destroyed!
com.example.android.unscramble D/GameFragment: GameFragment created/re-created!
```

- 화면을 회전해서 구성을 변경하면 fragment는 destroyed된 후 다시 create되지만, ViewModel은 소멸되지 않는다.

```
com.example.android.unscramble D/GameFragment: GameViewModel destroyed!
com.example.android.unscramble D/GameFragment: GameFragment destroyed!
```

- 만약 앱에서 나가면 GameViewModel이 소멸된다. (onCleared() 호출)

<br>
<br>

## ViewModel 구성

---

### 지연초기화

- 값을 할당할 준비가 되지 않았을때 `lateinit` 키워드 사용
- 변수가 초기화 될 때까지는 변수에 메모리가 할당되지 않는다. 초기화 전에 변수에 접근하면 에러

---

- wordsList : 게임에서 사용할 단어의 목록
- currentWord : 사용자가 추측해야할 단어 보유
- getNextWord() : `allWordsList`에서 무작위로 단어가져와서 currentWord에 할당
  - currentWord를 shuffle하고 \_currentScrambledWord에 할당하고 \_currentScrambledWord 증가후 wordsList에 currentWord추가

<br>
<br>

### 대화상자

- 머티리얼 가이드라인을 따르는 대화상자를 추가하자 (`MaterialAlertDialog`)
- 축하메시지, 점수, 나가기 버튼, 다시 플레이 버튼이 있는 대화상자

---

### 후행람다구문

- 매개변수로 전달되는 인자가 여러개이고 전달되는 마지막 인수가 함수이면 괄호 바깥에 람다 표현식을 배치가능하다.

```kotlin
    .setNegativeButton(getString(R.string.exit), { _, _ -> exitGame()})

    //후행람다구문
    .setNegativeButton(getString(R.string.exit)) { _, _ ->
        exitGame()
    }

```

---

<br>
<br>

### Submit버튼

- 유저가 정답을 입력하고 submit버튼을 눌렀을 때
- `onSubmitWord()` 와 연결
  - playerWord를 가져오고
  - 만약 playerWord와 답이 같은지 확인해서
  - 다르면 `setErrorTextField(true)`로 텍스트 필드에 오류표시
  - 같다면 다음 단어로 혹은 종료 다이얼로그 띄우기

#### 텍스트 필드에 오류 표시

- `binding.textField.error = getString(R.string.try_again)`
  - 에러 시에 텍스트 필드에 오류 표시

<br>

### Skip버튼

- `nextWord()`로 단어를 가져오되, 단어가 남았으면 가져오고, 남지 않았으면 대화상자 표시

<br>
<br>

### ViewModel에 데이터가 보존되는지 확인

- 기기방향을 바꾸어봐도 GameFragment destroyed가 되고, GameFragment created/re-created가 되어도 ViewModel의 데이터는 그대로 남아있다.

<br>
<br>

<br>
<br>

## Live data

- 데이터를 보유
- 관찰이 가능하다 -> 객체에서 보유한 데이터가 변경되면 관찰자에게 알림이 제공됨
- 수명주기를 인식
  - LiveData에 Observer를 연결하면 LifecycleOwner와 연결된다.
  - Observer 객체가 LiveData 객체를 구독하여 변경사항에 관한 알림을 받는다.
  - Observer를 activity와 fragment와 연결
- LiveData를 사용하면 UI를 업데이트하기 위해 여러 위치에서 `updateNextWordOnScreen()`메서드를 실행할 필요가 없다. 관찰자에서 한번만 실행하면 된다.

<br>

### \_currentScrambledWord --> MutableLiveData

- MutableLiveData는 변경 가능한 버전의 LiveData
- LiveData 객체에 접근하려면 value 속성 사용
  - `_currentScrambledWord.value = String(tempWord)`

### observer 연결하기

- 활성 수명 주기 상태인 관찰자만 업데이트
  - LiveData가 수명주기를 인식하여 수명주기 상태인 fragment의 경우에만 알림
- GameFragment의 관찰자는 GameFragment가 STARTED 또는 RESUMED 상태인 경우에만 알림을 받음
- **데이터 값이 변경되면 새 관찰자가 트리거 된다.**

<br>

- updateNextWordOnScreen() 는 새로 받아온 섞인 단어를 textView의 text로 변경하는 함수이다.

```kotlin
viewModel.currentScrambledWord.observe(viewLifecycleOwner,
   { newWord ->
       binding.textViewUnscrambledWord.text = newWord
   })
```

- onViewCreated()에서 currentScrambledWord(LiveData)의 관찰자를 연결
- viewLifecycleOwner는 fragment의 수명주기를 나타낸다
- Livedata가 viewLifecycleOwner를 통해 GameFragment의 수명주기를 인식하고 GameFragment가 활성 상태일 때만 관찰자에게 알린다!
- **데이터 값이 변경되면 새 관찰자가 트리거 된다.**

<br>

### score와 currentWordCount도 LiveData로 변경해준다.

- 값의 접근은 `.value`로
- 값에 더하려면 `.plue()`로
- 값을 1씩 증분하려면 `.inc()`로

<br>

## 레이아웃에서 LiveData사용

- LiveData 값이 변경될 때 바인딩된 레이아웃의 UI 요소에도 알림이 전송되어 레이아웃 내에서도 UI 업데이트 가능

<br>

---

## 뷰 결합과 데이터 결합

> **뷰 결합**
>
> xml 레이아웃에서 바인딩 클래스를 생성하고 뷰를 직접 참조 가능
>
> **데이터 결합**
>
> 선언적 형식을 사용해서 레이아웃 구성요소에 앱의 데이터 소스를 바인딩!  
> 즉, 뷰를 코드에 결합하는 것  
> activity에서 UI 호출을 삭제할 수 있어서 파일이 단순해진다.

- **뷰 결합을 사용하면?**

```kotlin
binding.textViewUnscrambledWord.text = viewModel.currentScrambledWord
```

- **데이터 결합을 사용하면?**

```xml
android:text="@{gameViewModel.currentScrambledWord}"
```

---

<br>

### 뷰 결합 -> 데이터 결합

1. bulid.gradle 고치기

```
buildFeatures {
   viewBinding = true
}

buildFeatures {
   dataBinding = true
}

plugins {
   ..
   id 'kotlin-kapt'
}
```

<br>

2. 레이아웃 파일을 데이터 결합 레이아웃으로 변경

- `<layout>` 루트로 시작함
- ScrollView 우클릭 > Show Context Actions > Convert to data binding layout

<br>

3. fragment에서 뷰 바인딩을 데이터 바인딩으로 변경

```kotlin
//binding = GameFragmentBinding.inflate(inflater, container, false)

binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

```

<br>

4. 레이아웃 파일에서 ViewModel에 access할 수 있도록 속성 추가

```xml
<data>
   <variable
       name="gameViewModel"
       type="com.example.android.unscramble.ui.game.GameViewModel" />

   <variable
       name="maxNoOfWords"
       type="int" />
</data>

```

```kotlin
//onViedCreated()

binding.gameViewModel = viewModel //초기화
binding.maxNoOfWords = MAX_NO_OF_WORDS
binding.lifecycleOwner = viewLifecycleOwner
//레이아웃에 수명주기 소유자를 전달 (LiveData가 수명주기를 인식하므로)
```

<br>

---

### 결합 표현식 사용하기

- 종속 변수중 하나라도 변경되면 결합 표현식을 실행하고 뷰를 이에 따라 업데이트한다.
- 구문
  - `@`로 시작하여 `{}`로 래핑 ex `@{user.firstName}`

---

<br>

5. GameFragment에서 관찰자 삭제

- UI에 연결해놓은 LiveData가 알아서 데이터를 변경하면 알림을 주므로 관찰자를 삭제하면 됨

<br>

6. 스크린 리더 지원

- <a href="https://support.google.com/accessibility/android/answer/6007100?authuser=1&hl=ko">음성 안내 지원 설정</a>
- String -> Spannable : 텍스트를 한글자씩 소리내서 읽어줌
