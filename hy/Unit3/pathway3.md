
### Android Jetpack 라이브러리

Android 앱을 더 간편하게 개발하기 위해 활용할 수 있는 라이브러리 컬렉션



<br><br><br>


```kotlin
 
```

### Android 아키텍처 구성요소
* 앱 아키텍처를 안내하는 역할
* 일련의 디자인 규칙. 앱의 구조 제시



<br><br><br>

### 아키텍처 원칙
* 관심사 분리
* 모델에서 UI 만들기



<br><br><br>

###




<br><br><br>

###




<br><br><br>

###





 
<br><br><br>
<hr>
<br>

###


```kotlin
 
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
 