## Get user input in an app : part2

<br>

### 앱 테마

- <a href="https://material.io/resources/color/#!/?view.left=0&view.right=0">색상 도구</a>

<br>

### 테마 변경하기

<br>

![image](https://user-images.githubusercontent.com/52737532/134378775-071bbb0f-2e0f-48e8-bbeb-d820de06581d.png)

<br>

1.  `color.xml`에 `name` 속성과 함께 색을 추가한다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>

    <color name="green">#1B5E20</color>
    <color name="green_dark">#003300</color>
    <color name="green_light">#A5D6A7</color>
    <color name="blue">#0288D1</color>
    <color name="blue_dark">#005B9F</color>
    <color name="blue_light">#81D4FA</color>
</resources>
```

2. `themes.xml`에 `ColorPrimary, ColorSecondary` 등을 추가 해준 색깔로 변경한다.
   - `@color/<name>` (예: `<item name="colorPrimary">@color/green</item>`)

<br>

- 다크모드도 설정해줄 수 있음 (`themes.xml` (night))

<br>

### 앱 아이콘 변경하기

- 런처 아이콘 경로 : (project 뷰) `app > src > main > res > mipmap ...`

  새로운 앱 아이콘으로 변경해보자.

  1. xml 파일들 삭제

  ```
  drawable/ic_launcher_background.xml
  drawable-v24/ic_launcher_foreground.xml
  ```

  삭제

  2. `res 우클릭 > new > image asset`

<br>
<br>
<br>

## 머티리얼 구성요소

- <a href="https://material.io/components">머티리얼 구성요소</a>
- 일반적인 디자인 머티리얼 가이드라인
- 일관된 방식, 유연성, 맞춤 설정 가능성

```
// app/build.gradle

dependencies {
    ...
    implementation 'com.google.android.material:material:<version>'
}

// 머티리얼 디자인 구성요소 (MDC) 라이브러리를 종속 함목으로 포함
```

<br>

### 머티리얼 가이드라인을 따르도록 `EditText` --> `TextInputEditText`

- `TextInputEditText`은 `TextInputLayout`에 포함되어 있다.

```xml
<com.google.android.material.textfield.TextInputLayout
    ..
    >

    <com.google.android.material.textfield.TextInputEditText
        ..
    />

</com.google.android.material.textfield.TextInputLayout>
```

<br>

### 머티리얼 가이드라인을 따르도록 `Switch` --> `SwitchMaterial`

- 만약 머티리얼 switch를 쓴다면 머티리얼 가이드라인이 변경되어도 직접 변경할 필요가 없이 위젯이 자동으로 업데이트 된다.

<br>
<br>
<br>

## 아이콘

- <a href="https://fonts.google.com/icons?selected=Material+Icons">머티리얼 아이콘</a>

<br>

### 아이콘 추가하기

1. `Resource Manager > + > Vector Asset > Clip Art 옆 버튼 클릭 > 아트 이미지 선택`
2. 이름 설정할 때는 `ic_`를 접두어로 사용할 것

> 안드로이드에서 벡터 드로어블 지원은 `API 21` 전에는 추가되지 않았기 때문에 21미만의 기기에서 실행하려면 설정을 추가해주어야 한다. (현재 프로젝트 설정 최소 `API 19`)

```
android {
  defaultConfig {
    ...
    vectorDrawables.useSupportLibrary = true
   }
   ...
}
```

<br>

### 아이콘을 `@id/cost_of_service` 좌측에 위치하도록 하자

<br>

![image](https://user-images.githubusercontent.com/52737532/134390707-0e8c9af9-972e-485a-9175-7fa5b4a720d8.png)

```xml
<!-- 아이콘 위치 설정 -->
<ImageView
    ..
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="@id/cost_of_service"
    app:layout_constraintBottom_toBottomOf="@id/cost_of_service" />


<!-- @id/cost_of_service 위치 설정 -->
<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/cost_of_service"
    ...
    android:layout_marginStart="16dp"
    app:layout_constraintStart_toEndOf="@id/icon_cost_of_service">

    <com.google.android.material.textfield.TextInputEditText ... />

</com.google.android.material.textfield.TextInputLayout>
```

1. 아이콘

   - `@id/cost_of_service`의 중간인데 부모의 왼쪽부터 시작

<br>

2. `@id/cost_of_service`

   - start (왼)에서 16dp만큼 margin과
   - 아이콘의 오른쪽에서 시작하도록 설정

<br>

### 시작하는 위치 맞추기

<br>

![image](https://user-images.githubusercontent.com/52737532/134393124-aa77fc3e-5e5f-4c40-a071-b16b35f4cb53.png)

- TextView의 시작 가장자리로 RadioGroup의 시작 가장자리를 제한한다.

```xml
...

<RadioGroup
    android:id="@+id/tip_options"
    ...
    app:layout_constraintStart_toStartOf="@id/service_question">

...

```

<br>
<br>
<br>

## 스타일 만들기

- 글꼴 색상, 크기 등을 스타일로 추출하면 여러 뷰에 쉽게 적용할 수 있다.
- res > values > (우클릭 New > Values Resource File) styles.xml 새로운 파일 만들기

### 이름 규칙

- 상위 머티리얼 스타일을 상속하는 경우 `MaterialComponents --> TipTime(앱 이름)` 으로 바꾼다
  - 충돌 가능성 줄임

```xml
<!-- styles.xml-->
<style name="Widget.TipTime.TextView" parent="Widget.MaterialComponents.TextView">
    <item name="android:minHeight">48dp</item>
    <item name="android:gravity">center_vertical</item>
    <item name="android:textAppearance">?attr/textAppearanceBody1</item>
</style>

<!-- activity_main.xml-->
<TextView
    android:id="@+id/service_question"
    style="@style/Widget.TipTime.TextView"
    ... />
```

<br>

### 자주 쓰는 속성은 `dimens.xml`에 저장

```xml
<!-- dimens.xml-->
<resources>
   <dimen name="min_text_height">48dp</dimen>
</resources>

<!-- styles.xml-->
<item name="android:minHeight">@dimen/min_text_height</item>
```

<br>

### 테마에 스타일 추가하기

- 테마는 모든 라디오버튼, 모든 스위치.. 에 적용된다.
- 다크모드도 똑같이 적용해줄 것

```xml
<!-- themes.xml -->
<item name="textInputStyle">@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox</item>
<item name="radioButtonStyle">@style/Widget.TipTime.CompoundButton.RadioButton</item>
<item name="switchStyle">@style/Widget.TipTime.CompoundButton.Switch</item>
```

<br>

## 사용자 환경 향상하기

### 구성요소들이 짤리지 않게 `ScrollView`로 감싸기

```xml

<ScrollView
   xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools"
   android:layout_height="match_parent"
   android:layout_width="match_parent">

    <androidx.constraintlayout.widget.ConstraintLayout
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:padding="16dp"
       tools:context=".MainActivity">

       ...
   </ConstraintLayout>

</ScrollView>

```

<br>

### Enter 키로 키보드 숨기기

```kotlin
// onCreate
    binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)}


// function
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // inputMethodManager : 키보드를 숨길지 표시할지 제어
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true // 키 이벤트가 처리된 경우
        }
        return false //처리되지 않은 경우
    }

```

- `OnKeyListener`에서 키를 누르면 onkey()가 실행되고, `onKey()`가 호출되면 `handleKeyEvent()`가 호출된다.

- 인수로 뷰, 코드, 키 이벤트가 전달되는데 키 이벤트는 사용하지 않으므로 `_`로 지정
