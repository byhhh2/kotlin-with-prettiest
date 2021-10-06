# Pathway2

### **Material Design**

- 색상
    
    `colors.xml` 에서 아래와 같이 색상과 이름 지정 가능
    
    ```kotlin
    <color name="green">#1B5E20</color>
    ```
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-theme/img/730ee7b8c4496433.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-theme/img/730ee7b8c4496433.png?hl=ko)
    
- 기본 테마 색상
    
    ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-theme/img/af6c8e0d93135130.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-theme/img/af6c8e0d93135130.png?hl=ko)
    
    [제목 없음](https://www.notion.so/017123668b444c11b359f6efc7b339ab)
    
    테마 색상은 `themes.xml` (app > res > values > themes.xml) 에 저장되어 있음
    
    - `Theme.MaterialComponents.DayNight.DarkActionBar`
        - `DayNight` : Material 구성요소 라이브러리에 미리 정의된 테마
        - `DarkActionBar` : 작업 모음이 어두운 색상을 사용한다는 의미
        
        테마는 상위 테마의 속성을 상속
        정의되지 않은 테마 생삭은 상위 테마의 색상 사용
        
    
    색상 도구 (색상 UI 미리보기)
    [https://material.io/resources/color/#!/?view.left=0&view.right=0](https://material.io/resources/color/#!/?view.left=0&view.right=0)
    Material Palette 생성기
    [https://material.io/design/color/the-color-system.html#tools-for-picking-colors](https://material.io/design/color/the-color-system.html#tools-for-picking-colors)0
    

- 어두운 테마 색상
    
    `themes.xml (night)`(app > res > values > themes > themes.xml(night)) 를 수정한다
    

---

### 앱 아이콘 변경

- 화면 픽셀 밀도
    - 중밀도 기기(mdpi) → 화면의 인치당 도트 수 : 160
    - 초고밀도 기기(xxxhdpi) → 화면의 인치당 도트 수 : 640

- 런처 아이콘 파일
    - `Project` 뷰에서 [리소스 디렉토리 : `app` > `src` > `main` > `res`] 로 이동
    - `mipmap` : 앱의 런처 아이콘 애셋을 배치해야 하는 위치
        - `mdpi` - 중밀도 화면의 리소스(~160dpi)
        - `hdpi` - 고밀도 화면의 리소스 (~240dpi)
        - `xhdpi` - 초고밀도 화면의 리소스(~320dpi)
        - `xxhdpi` - 초초고밀도 화면의 리소스(~480dpi)
        - `xxxhdpi` - 초초초고밀도 화면의 리소스(~640dpi)
        - `nodpi` - 화면의 픽셀 밀도와 관계없이 조정할 수 없는 리소스
        - `anydpi` - 어떤 밀도로도 조정 가능한 리소스
    - `ic_launcher.png` 와 `ic_launcher_round.png`
        
        일반은 정사각형 버전 아이콘, round는 원형 버전 아이콘
        
        오른쪽 상단에 이미지 픽셀 표시
        
        `webP` 확장자
        구글에서 만든 이미지 파일 포맷
        손실 압축(JPEG), 비손실 압축(PNG,GIF) 모두 지원
        

- 적응형 아이콘
    - 포그라운드 레이어 & 백그라운드 레이어
        1. 백그라운드 레이어 : 파란색과 흰색 격자 무늬
        2. 포그라운드 레이어 : 안드로이드 아이콘
        3. 원형 마스크 : 마스크 모양대로 앱 아이콘 생성
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/1b5d411e9a4665f0.gif?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/1b5d411e9a4665f0.gif?hl=ko)
        
    - 벡터 드로어블 & 비트맵 이미지
        - 벡터 드로어블 
        이미지를 정의하는 모양을 그리는 방법을 알고 있음 
        점,선,곡선,색상 정보로 구성됨
        - 비트맵 이미지
        각 픽셀의 색상 정보를 제외하고 보유한 이미지에 관해 알지 못함

- 앱 아이콘 변경
    1. `Project` 뷰 - `app` > `src` > `main` > `res`> `drawable` 또는 `drawable-v24` 로 이동
    아래 파일을 수정하여 앱 아이콘을 변경할 수 있다
        
        ```
        drawable/ic_launcher_background.xml
        drawable-v24/ic_launcher_foreground.xml
        ```
        
    2. 해당 파일을 삭제하고 새로운 애셋을 추가할 경우
    이미지 애셋 추가를 누른다.
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/c655215f2a42dd5e.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/c655215f2a42dd5e.png?hl=ko)
        
    3. 아래의 화면이 나타나면 backgorund 와 foreground 탭에서 각각 이미지를 설정한다.
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/2873b6dd2ae1661a.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/2873b6dd2ae1661a.png?hl=ko)
        
    4. 이미지가 잘리지 않게 크기를 조정한다.
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/d349f38383ed09fe.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/d349f38383ed09fe.png?hl=ko)
        
    5. Confirm Icon Path 단계에서 기존 앱 아이콘을 덮어 쓴다.
        
        ![https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/1ddb585036d12cf4.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-icon/img/1ddb585036d12cf4.png?hl=ko)
        

- 적응형 아이콘은 v26부터 지원하므로 `drawable-anydpi-v26` 디렉토리 생성후 포어그라운드와 백그라운드 이미지를 해당 디렉토리로 옮겨서 진행한다.

---

### Material 테마 적용하기

1. `EditText` 에 적용하기
    
    밑줄 친 곳에서 에러가 발생
     - `constraintLayout` 에서 해당 뷰를 제한하지 않았기 때문
     - 문자열 리소스도 존재하지 않음
    
    ```kotlin
    <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textField"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="@string/label">
    
            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
            />
    </com.google.android.material.textfield.TextInputLayout>
    ```
    
    해결 방법 : `EditText` 의 속성들을 옮김
    
    ```kotlin
    // 복사한 속성들
    <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/cost_of_service"
            android:layout_width="160dp"
            android:layout_height="wrap_content"
            android:hint="@string/cost_of_service"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">
    
            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/cost_of_service_edit_text"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="numberDecimal" />
    </com.google.android.material.textfield.TextInputLayout>
    
    // 기존 EditText 속성
    <EditText
            android:id="@+id/cost_of_service"
            android:layout_width="160dp"
            android:layout_height="wrap_content"
            android:hint="@string/cost_of_service"
            android:inputType="numberDecimal"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    ```
    

1. `MainActivity.kt` 에서 입력한 Text를 가져오는 부분을 수정
    
    ```kotlin
    val stringInTextField = binding.costOfServiceEditText.text.toString()
    ```
    

1. `Switch` 에 적용하기
    
    ```kotlin
    <~~Switch~~com.google.android.material.switchmaterial.SwitchMaterial
            android:id="@+id/round_up_switch"
            android:layout_width="0dp" .../>
    ```
    
    → `SwitchMaterial` 의 구현이 업데이트 되었을 경우 알아서 적용됨
    
2. 아이콘 적용
    1. 리소스 추가
        
        `Resource Manager` 에서 Vector Asset을 추가할 경우 Material Icon들이 ClipArt에 있음
        
        여기서 아이콘을 Resource에 추가하고 사용함
        
        **이전 Android 버전 지원하기**
        
        벡터 드로어블은 Android 5.0 이전에서는 지원이 안됨
        따라서 이전 버전에서 지원하려면 아래의 코드를 `build.gradle` 파일에 추가해야 함
        
        ```kotlin
        android {
          defaultConfig {
            ...
            vectorDrawables.useSupportLibrary = true
           }
           ...
        }
        ```
        
    2. 아이콘 배치하기
        
        추가하고 싶은 `View` 아이템 앞에 `ImageView` 아이템을 삽입
        
        ```kotlin
        <ImageView    //제약조건 설정 전으로 오류 발생
            android:id="@+id/icon_cost_of_service"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:importantForAccessibility="no"
            app:srcCompat="@drawable/ic_store" />
        ```
        
        - 제약조건 설정
            
            ![https://developer.android.com/codelabs/basic-android-kotlin-training-polished-user-experience/img/e23287bdeca07a1e.png?hl=ko](https://developer.android.com/codelabs/basic-android-kotlin-training-polished-user-experience/img/e23287bdeca07a1e.png?hl=ko)
            
            위와 같이 설정하고자 함
            상,하 : `@id/cost_of_service` 인 텍스트 필드의 상,하로 제한 (텍스트 필드의 상하를 기준으로 가운데 맞춤)
            좌 : 앱의 시작
            
            우 : `@id/cost_of_service` 의 좌측 제약조건이 앱의 시작으로 되어 있으므로 `@id/cost_of_service` 의 좌측 제약조건을 업데이트(`ImageView`의 우측으로 제한) 하고 `ImageView` 의 우측 제약 조건을 추가함
            
            ```kotlin
            ~~app:layout_constraintStart_toStartOf="parent"~~
            
            android:layout_marginStart="16dp" // ImageView와 16dp 만큼 거리를 둠
            app:layout_constraintStart_toEndOf="@id/icon_cost_of_service" // ImageView의 우측으로 제한
            ```
            
    
    1. 스타일 및 테마
        
        스타일 : 단일 위젯 유형의 뷰 속성 값 모음 → 한마디로 디자인 세트
        
        - 스타일 만들기
        스타일 정의 후 레이아웃의 모든 속성에 적용할 수 있다.
            1. `styles.xml` 파일 생성 (`res` > `values` 디렉터리)
                
                `values` 우클릭 → New → Values Resource File 순서
                
            2. 스타일 이름 설정 (Material → TipTime(앱이름))
                
                ```kotlin
                <style name="Widget.TipTime.TextView" parent="Widget.MaterialComponents.TextView">
                </style>
                ```
                
            3. 속성 설정
                - gravity : 뷰 안의 콘텐츠가 배치되는 방식 제어
                속성 : center_vertical, center, center_horizontal, top, bottom
                    
                    ```kotlin
                    <item name="android:gravity">center_vertical</item> //세로로 중앙에 텍스트 배치
                    ```
                    
                - textAppearance : 텍스트 크기, 글꼴, 기타 텍스트 속성과 관련된 사전 정의된 스타일
                    
                    [Material Design](https://material.io/develop/android/theming/typography)
                    
                    해당 링크에서 스타일 확인 가능
                    
                    ```kotlin
                    <item name="android:textAppearance">?attr/textAppearanceBody1</item>
                    ```
                    
                - paddingStart : 구성요소의 시작 부분에 패딩 추가
                    
                    ```kotlin
                    <item name="android:paddingStart">8dp</item>
                    ```
                    
        
        - 스타일 적용하기
            
            `activity_main.xml`에서 각 `TextView` 에 스타일 속성 추가
            
            ```kotlin
            <TextView
                android:id="@+id/service_question"
                style="@style/Widget.TipTime.TextView"
                ... />
            ```
            
            `**dimens.xml`** 
            자주 사용되는 값을 관리하는 파일
            리소스 파일 생성과 같은 방법으로 생성
            
        
        - 테마에 스타일 추가
            
            `themes.xml` 파일에 만든 스타일 추가
            - TextLayout에는 OutlinedBox 테마 적용함
            - 스위치와 버튼에는 만든 스타일 적용
            
            → `activity_main.xml`에서 각 속성에 추가하는 방법 & 테마에 스타일 추가하는 방법
            스위치와 버튼은 후자, 텍스트 뷰는 전자
            
            ```kotlin
            <resources xmlns:tools="http://schemas.android.com/tools">
            
                <!-- Base application theme. -->
                <style name="Theme.TipTime" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
                    ...
            				<!-- Text input fields -->
                    <item name="textInputStyle">@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox</item>
                    <!-- Radio buttons -->
                    <item name="radioButtonStyle">@style/Widget.TipTime.CompoundButton.RadioButton</item>
                    <!-- Switches -->
                    <item name="switchStyle">@style/Widget.TipTime.CompoundButton.Switch</item>
                </style>
            
            </resources>
            ```
            
    2. 사용자 환경 향상
        - 스크롤 뷰
            
            `ConstrainrLayout` 주변을 `ScrollView`로 감쌈
            
            ```kotlin
            <ScrollView
               xmlns:android="http://schemas.android.com/apk/res/android"
               xmlns:app="http://schemas.android.com/apk/res-auto"
               xmlns:tools="http://schemas.android.com/tools"
               android:layout_height="match_parent"
               android:layout_width="match_parent">
            
               <androidx.constraintlayout.widget.ConstraintLayout
                   ...
               </ConstraintLayout>
            
            </ScrollView>
            ```
            
        - Enter 키 누르면 키보드 사라짐
            1. 키보드 리스너 설정
                
                InputMethodManager : 소프트 키보드를 표시할지 숨길지 제어하고 어느 소프트 키보드를 표시할지 선택할 수 있도록 함
                
                메서드 반환값 : Boolean ( 이벤트 처리 경우 : true, 미처리 : false)
                
                ```kotlin
                private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
                   if (keyCode == KeyEvent.KEYCODE_ENTER) {
                       // Hide the keyboard
                       val inputMethodManager =
                           getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                       inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                       return true
                   }
                   return false
                }
                ```
                
            2. `TextInputEditText` 에 키 리스너를 연결
                
                `costOfServiceEditText` (숫자 입력창)에 `setOnKeyListener()` 메서드를 호출해서 키리스너를 달고 `OnKeyListener`를 전달
                
                `OnKeyListener` 구조
                
                ```
                public static interface OnKeyListener {
                    boolean onKey(android.view.View view, int i, android.view.KeyEvent keyEvent);
                }
                ```
                
                `onKey()` 메서드 매개변수 : 뷰, 누른 키의 코드, 키 이벤트 (사용하지 않을 경우 _ )
                
                `onKey()` 메서드 호출 후 `handleKeyEvent()` 메서드를 호출
                
                ```kotlin
                view, keyCode, _ -> handleKeyEvent(view, keyCode) //람다 표현식
                ```
                
        - 벡터 드로어블의 색조 조정하기
            - android:tint
                - colorControlNormal : 위젯의 정상 색상
                    
                    위의 속성 대신 설정하고 싶은 색상으로 변경
                    
                    ```kotlin
                    android:tint="?attr/colorPrimary">
                    ```
                    
            - android:fillcolor