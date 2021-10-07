# Pathway3

- 앱 아키텍처 알아보기
    - 관심사 분리
        - 별개의 책임이 있는 여러 클래스로 앱을 나눠야 함
    - 모델에서 UI 만들기
        - 모델
            
            앱의 데이터 처리 담당 요소
            
            앱의 Views 객체  및 앱의 구성요소와 독립되어 있음
            
            앱의 수명 주기 및 관련 문제의 영향을 받지 않음
            
    - Android 아키텍처 기본 클래스(구성요소)
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/53dd5e42f23ffba9.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/53dd5e42f23ffba9.png?hl=ko)
        
        - UI 컨트롤러
            - Activity 와 Fragment
            - 화면에 뷰를 그림
            - 사용자가 상호작용하는 다른 모든 UI 관련 동작을 캡처하여 UI를 제어함
            - 앱의 데이터나 데이터에 관한 의사 결정 로직은 포함되지 않음
            - 시스템 조건에 따라 UI 컨트롤러가 제거될 수 있음 → 따라서 앱의 데이터나 상태를 저장하면 안됨
        - ViewModel
            - 뷰에 표시되는 앱 데이터의 모델
                - 모델은 앱의 데이터 처리 담당
            - 폐기되지 않는 앱 관련 데이터 저장 (Activity 나 Fragment처럼 소멸되지 않음)
            - 데이터에 관한 의사 결정 로직이 담겨있음
        - LiveData
        - Room
    
- ViewModel 추가하기
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/2094f3414ddff9b9.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/2094f3414ddff9b9.png?hl=ko)
    
    MainActivity : GameFragment 포함
    
    GameFragment : GameViewModel에 있는 게임 관련 정보 액세스
    
    1. `build.gradle(Module:Unscramble.app)` 에 하단의 코드 삽입
        
        ```kotlin
        dependencies{
        		...
        		implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
        }
        ```
        
    2. `GameViewModel` 클래스 생성
        
        ```kotlin
        class GameViewModel : ViewModel() {
        }
        ```
        
    
    - 속성 위임
        - var 의 값을 할당하거나 읽는 메서드 : getter, setter
        
        위의 getter-setter 책임을 다른 클래스에 넘기는 것
        
        책임을 가진 클래스는 대리자 클래스가 됨
        
        ```kotlin
        var <property-name> : <property-type> by <delegate-class>()
        ```
        
- ViewModel로 데이터 이동하기
    1. 데이터 변수 GameViewModel로 이동
        
        → 속성이 ViewModel에만 공개되어 UI 컨트롤러에서 액세스를 할 수 없어 에러가 남
        
        → private을 public으로 바꾸는 것은 데이터 변경의 위험으로 인해 안됨
        
        ⇒ **지원 속성** 을 사용하면 됨
        
        지원 속성
        
        getter나 setter를 재정의 하여 고유한 맞춤 동작 제공
        
        ```kotlin
        private var _count = 0
        
        // 외부에서는 get()으로 _count에 접근 가능
        val count: Int
           get() = _count
        ```
        
    2. currentScrambledWord에 지원 속성 추가하기
    
- ViewModel의 수명 주기
    
    클리어 되기 전까지 ViewModel은 계속 살아있음
    
    연결된 fragment가 분리되거나 Activity가 완료되면 소멸됨
    
    소멸되기 직전에 onCleared() 콜백 → onCleared() 메서드 재정의
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/18e67dc79f89d8a.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/18e67dc79f89d8a.png?hl=ko)
    
    **init 블록**
    
    객체 인스턴스 초기화 중에 필요한 초기 설정 코드를 배치하는 장소
    
- ViewModel 채우기
    
    lateinit : 지연 초기화
    
    속성을 나중에 초기화함 = 지연된 초기화
    
    변수가 초기화될 때까지 메모리 할당 x
    
    초기화 전에 변수에 액세스 → 비정상 종료
    
    - GameViewModel 클래스에서 할 일
        - getNextWord() 메서드 생성 : 글자가 뒤섞인 다음 단어를 가져오는 메서드
            
            init블록에서 getNextWord() 실행
            
            ```kotlin
            init {
                Log.d("GameFragment", "GameViewModel created!")
                getNextWord()
            }
            ```
            
            - `allWordsList`에서 무작위로 단어를 가져와 `currentWord.`에 할당
                
                array 는 list와 비슷하나 초기화 시 고정크기를 가짐 (확장,축소 불가)
                
            - `currentWord`의 글자를 임의 배열하여 글자가 뒤섞인 단어를 만들고 `currentScrambledWord`에 할당
            - 글자가 뒤섞인 단어와 뒤섞이지 않은 단어가 동일한 경우를 처리
            - 게임 중에 같은 단어가 두 번 표시되지 않도록 합니다.
        - 게임에서 반복된 단어가 나오지 않게 할 MutableList 생성
        - 도우미 메서드 추가
            
            목록에서 다음 단어 가져옴
            
            단어수가 MAX_NO_OF_WORDS보다 적으면 true 반환
            
            ```kotlin
            fun nextWord(): Boolean {
                return if (currentWordCount < MAX_NO_OF_WORDS) {
                    getNextWord()
                    true
                } else false
            }
            ```
            
- 대화 상자
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/a5ecc09450ae44dc.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-viewmodel/img/a5ecc09450ae44dc.png?hl=ko)
    
    1. 알림 대화상자
    2. 제목(선택사항)
    3. 메시지
    4. 텍스트 버튼
    
    - 최종 점수 대화상자 구현하기
        
        MaterialAlertDialog 생성 : MaterialAlertDialogBuilder 클래스를 사용하여 대화상자의 부분을 단계별로 빌드
        
        ```kotlin
        private fun showFinalScoreDialog() {
           MaterialAlertDialogBuilder(requireContext())
               .setTitle(getString(R.string.congratulations))
               .setMessage(getString(R.string.you_scored, viewModel.score))
               .setCancelable(false)
               .setNegativeButton(getString(R.string.exit)) { _, _ ->
                   exitGame()
               }
               .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                   restartGame()
               }
               .show()
        }
        ```
        
        - setTitle() : 알림 대화상자의 제목 설정
        - setMessage() : 최종 점수를 표시하도록 설정
        - setCancelable(false) : 뒤로가기 키로 알림 대화상자를 취소할 수 없음
        - setNegativeButton() / setPositiveButton() : EXIT 및 PLAY AGAIN의 두 텍스트 버튼
            - 람다식으로 각각 exitGame()과 restartGame() 호출
            
            **후행 람다 구문**
            
            다를 괄호 안에 배치하거나 바깥에 배치하여 코드를 작성하는 방법
            
        - show() : 대화 상자 표시
- Submit 버튼의 OnClickListener 구현하기
    - 글자가 뒤섞인 단어 표시 :onSubmitWord() 메서드
        
        ```kotlin
        private fun onSubmitWord() {
            if (viewModel.nextWord()) {
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        }
        ```
        
    - 플레이어의 단어 검증하는 도우미 메서드
        - 점수 + 메서드
            
            ```kotlin
            private fun increaseScore() {
               _score += SCORE_INCREASE
            }
            ```
            
        - 플레이어의 단어가 맞는지 검증하는 메서드
            
            ```kotlin
            fun isUserWordCorrect(playerWord: String): Boolean {
               if (playerWord.equals(currentWord, true)) {
                   increaseScore()
                   return true
               }
               return false
            }
            ```
            
    - 텍스트 필드에 오류 표시하기
        
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
        
- Skip Button 구현하기
    - onSkipWord() 메서드 구현하기
        
        ```kotlin
        private fun onSkipWord() {
            if (viewModel.nextWord()) {
                setErrorTextField(false)
                updateNextWordOnScreen()
            } else {
                showFinalScoreDialog()
            }
        }
        ```
        
- ViewModel에 데이터가 보존되는지 확인하기
    
    ViewModel의 wordCount에 지원 속성 설정
    
    ```
    private var _currentWordCount = 0
    val currentWordCount: Int
       get() = _currentWordCount
    ```
    
    → GameFragment의 onCreateView() 에서 실행
    
    →Fragment가 소멸했다가 다시 생성되더라도 데이터가 살아있음을 확인할 수 있다.
    

---

- Livedata
    
    수명 주기를 인식하는 식별 가능한 데이터 홀더 클래스
    
    - 데이터를 보유하는 데이터 래퍼
    - Activity, Fragment 등의 LifeCycle을 인식하여 LifeCycle 내에서만 동작하는 요소
    - Livedata 객체가 보유한 데이터가 변경되면 관찰자에 알림이 제공됨
    - Livedata에 관찰자를 연결하면 관찰자는 LifecycleOwner와 연결됨
        
        STARTED 또는 RESUMED 같은 활성 Lifecycle 상태인 관찰자만 업데이트
        
        **LifecycleOwner**
        
        클래스에 Lifecycle(수명주기)가 있음을 나타내는 단일 메서드 인터페이스
        
    
- 게임에 LiveData 추가하기
    - MutableLiveData
        
        변경 가능한 버전의 LiveData
        
    - `GameViewModel` 에서 `_currentScrambledWord` 유형 바꾸기
        
        ```kotlin
        private val _currentScrambledWord = MutableLiveData<String>()
        val currentScrambledWord: LiveData<String>
           get() = _currentScrambledWord
        ```
        
        LiveData 객체 내의 데이터에 액세스하려면 value 속성을 사용
        
- LiveData 객체에 관찰자 연결하기
    - GameFragment에서 관찰자 설정
        
        GameFragment가 STARTED 또는 RESUMED 상태일 경우에만 알람을 받는다.
        
        1. `GameFragment` 에서 `updateNextWordOnScreen()` 및 이 메서드의 모든 호출을 삭제
        2. `onSubitword()` 수정
            
            ```kotlin
            private fun onSubmitWord() {
                val playerWord = binding.textInputEditText.text.toString()
            
                if (viewModel.isUserWordCorrect(playerWord)) {
                    setErrorTextField(false)
            				~~if (viewModel.nextWord()) {
                         updateNextWordOnScreen()
                    } else {
                        showFinalScoreDialog()
                    }~~
                    if (!viewModel.nextWord()) {
                        showFinalScoreDialog()
                    }
                } else {
                    setErrorTextField(true)
                }
            }
            ```
            
        3. `onViewCreated()` 끝에 `currentScrambledWord()` 에 관찰자 달기
            
            ```kotlin
            // Observe the currentScrambledWord LiveData.
            viewModel.currentScrambledWord.observe()
            ```
            
        4. 관찰자에 파라미터 넘겨주기
            
            ```kotlin
            viewModel.currentScrambledWord.observe(viewLifecycleOwner,
               { newWord ->
                   binding.textViewUnscrambledWord.text = newWord
               })
            ```
            
        
        → 글자가 뒤섞인 단어 텍스트 뷰가 LiveData 관찰자에서 자동으로 업데이트
        
- 점수 및 단어 수에 관찰자 연결하기
    1. LiveData로 점수 및 단어 수 래핑하기
        - LiveData의 value는 ++이 안됨
            
            → plus()나 inc() 사용
            
            ```kotlin
            _score.value = (_score.value)?.plus(SCORE_INCREASE)
            
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            ```
            
            plus( n ) : n만큼 덧셈
            
            inc() : 1만큼 덧셈
            
    2. 관찰자를 점수 및 단어 수에 연결하기
    
- 데이터결합
    
    뷰 + 뷰 결합에 데이터 결합함
    
    1. 뷰 결합을 데이터 결합으로 변경하기
        
        gradle 파일 수정 (module)
        
        ```kotlin
        buildFeatures {
           dataBinding = true
        }
        
        plugins {
        	 ...
           id 'kotlin-kapt'
        }
        ```
        
    2. 레이아웃 파일을 데이터 결합 레이아웃으로 변환하기
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-livedata/img/f356fc45e8fe91b1.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-livedata/img/f356fc45e8fe91b1.png?hl=ko)
        
        자동 추가 가능
        
    3. GameFragment의 onCreateView() 메서드 binding 변수의 인스턴스화를 변경
        
        ```kotlin
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        ```
        

- 결합 표현식
    
    `@` 로 시작
    
    `{}` 로 랩핑
    

- 앱 리소스 참조
    
    ```kotlin
    android:padding="@{@dimen/largePadding}"
    ```
    
- 레이아웃 속성 전달
    
    string에 %s 표현식일때 매개변수를 ()안에 넣어서 전달
    
    ```kotlin
    android:text="@{@string/example_resource(user.lastName)}"
    ```