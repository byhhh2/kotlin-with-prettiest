# [Unit 3] Pathway3

- UI 컴트롤러와 ViewModel 한문장 정리
    
    Activity와 Fragment 는 화면에 뷰와 데이터
    
    ViewModel은 UI에 필요한 모든 데이터를 보유하고 처리
    

## 앱 아키텍쳐

관심사 분리 : 각각 별개의 책임이 있는 여커 클래스로 앱을 나눠야 함

모델에서 UI 만들기 : 모델은 앱 데이터 처리를 담당하고, 앱의 수명주기 문제에 영향 받지 않음.

구성요소 : UI 컨트롤러, View Model, LiveData, Room

## UI 컨트롤러 (Activity / Fragment)

뷰와 사용자 이벤트나 사용자가 상호작용하는 다른 모든 UI 관련 동작을 캡처하여 UI를 제어.

앱의 데이터 또는 데이터에 관한 모든 의사 결정 로직은 UI 컨트롤러 클래스에 포함 불가.

 → 특정 사용자 상호작용을 기반으로 또는 메모리 부족과 같은 시스템 조건으로 인해 언제든지 UI 컨트롤러를 제거할 수 있기 때문.  이벤트는 개발자가 직접 제어할 수 없기 때문에, UI 컨트롤러에 앱 데이터나 상태를 저장해서는 안 됨. → 데이터에 관한 의사 결정 로직을 ViewModel에 추가

## ViewModel

뷰에 표시되는 앱 데이터의 모델 (앱의 데이터 처리를 담당하는 구성요소)

Android 프레임워크에서 활동이나 프래그먼트가 소멸 후 재생성 시 폐기되지 않는 앱 데이터를 저장

구성이 변경되는 동안 자동으로 유지되어(Ativity 또는 fragment 인스턴스처럼 소멸되지 않음) 보유하고 있는 데이터가 다음 활동 또는 프래그먼트 인스턴스에 즉시 사용 가능.

속성 위임

```kotlin
private val viewModel = GameViewModel()
private val viewModel: GameViewModel by viewModels()
```

기기에서 구성이 변경되는 동안 앱이 viewModel 참조의 상태를 손실하게 됨. 

속성 위임 접근 방식을 사용해 viewModel 객체의 책임을 viewModels 라는 별도의 클래스에  위임.

즉, viewModel 객체에 액세스하면 이 객체는 대리자 클래스 viewModels에 의해 내부적으로 처리. 

대리자 클래스는 첫 번째 액세스 시 자동으로 viewModel 객체를 만들고 이 값을 구성 변경 중에도 유지했다가 요청이 있을 때 반환.

```kotlin
private var _count = 0
val count: Int
   get() = _count
```

shuffle 단어 만들기

```kotlin
private fun getNextWord() {
   currentWord = allWordsList.random()
   val tempWord = currentWord.toCharArray()
   tempWord.shuffle()

   while (tempWord.toString().equals(currentWord, false)) {
       tempWord.shuffle()
   }
   if (wordsList.contains(currentWord)) {
       getNextWord()
   } else {
       _currentScrambledWord = String(tempWord)
       ++currentWordCount
       wordsList.add(currentWord)
   }
}
```

Array는 List와 비슷하지만 초기화될 때 고정 크기를 가진다.

Array는 크기를 확장하거나 축소할 수 없는(크기를 조절하려면 배열을 복사해야 함) 

반면, List에는 add() 함수와 remove() 함수가 있어 크기를 늘리고 줄일 수 있다.

대화상자 만들기

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

## Livedata

수명 주기를 인식하는 식별 가능한 데이터 홀더 클래스

특징 : 데이터 보유, 관찰 가능, 수명 주기 인식

MutableLiveData : 변경 가능한 버전의 LiveData

```kotlin
private fun increaseScore() {
    _score.value = (_score.value)?.plus(SCORE_INCREASE)
}

private fun getNextWord() {
   ...
    } else {
        _currentScrambledWord.value = String(tempWord)
        _currentWordCount.value = (_currentWordCount.value)?.inc()
        wordsList.add(currentWord)
       }
   }
```

_score가 더 이상 정수가 아니고 LiveData이기 때문에 +=  사용하지 않는다.

`inc()`  null에 안전하게 값을 1씩 증분합니다.

## 데이터결합

선언적 형식을 사용하여 레이아웃의 UI 구성요소를 앱의 데이터 소스에 바인딩

간단히 말해서 코드에서 데이터를 뷰 + 뷰 결합에 결합(뷰를 코드에 결합)하는 것

장점) Activity에서 많은 UI 프레임워크 호출을 삭제할 수 있어 파일이 더욱 단순하고 쉽게 유지관리       

         앱 성능이 향상되며 메모리 누수 및 null 포인터 예외를 방지