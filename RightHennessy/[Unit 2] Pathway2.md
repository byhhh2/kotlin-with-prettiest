# [Unit 2] Pathway2

## 색상 ARGB

4개의 16진수 (#00~#FF(0~255))로 표현

투명도 (alpha) : 명도(#00=0%=완전 투명, #FF=100%=완전 불투명)

빨초파 (RGB) : 숫자가 클수록 구성요소의 색이 더 많이 포함. 

색상은 RGB 값을 직접 사용하는 것이 아니라 색상 리소스(예: @color/purple_500)로 지정

## 앱 아이콘 변경

- 밀도 한정자 (mipmap) : 특정 화면 밀도 기기의 리소스

`mdpi`  중밀도 화면의 리소스(~160dpi)

`hdpi`  고밀도 화면의 리소스 (~240dpi)

`xhdpi`  초고밀도 화면의 리소스(~320dpi)

`xxhdpi`  초초고밀도 화면의 리소스(~480dpi)

`xxxhdpi`  초초초고밀도 화면의 리소스(~640dpi)

`nodpi`  화면의 픽셀 밀도와 관계없이 조정할 수 없는 리소스

`anydpi`  어떤 밀도로도 조정 가능한 리소스

## 적응형 아이콘

API 26에서 Android 플랫폼에 도입. 특정 요구사항을 준수하는 **포그라운드** 및 **백그라운드** 레이어로 구성되므로 다양한 OEM 마스크가 적용된 여러 기기에서 앱 아이콘이 고화질로 표시 

- 비트맵 이미지 : 각 픽셀의 색상 정보를 제외하고 보유한 이미지에 관해 잘 알지 못함.다
- 벡터드로어블 : Android의 벡터 그래픽 구현. 관련 색상 정보와 함께 일련의 점과 선, 곡선으로 XML에서 정의. 화질 저하 없이 어떤 밀도로도 조정 가능

## 머티리얼 구성요소

앱에서 머티리얼 스타일을 더 쉽게 구현할 수 있는 일반적인 UI 위젯

사용자 기기에 있는 다른 앱과 함께 더 일관된 방식으로 작동 

→ 한 앱에서 학습된 UI 패턴이 다음 앱에 이전

- 텍스트 필드

MDC 라이브러리의 TextInputEditText가 포함된 TextInputLayout을 사용

- 항상 표시되는 입력 텍스트 또는 라벨 표시하기

- 텍스트 필드에 아이콘 표시하기

- 도우미 또는 오류 메시지 표시하기

## 아이콘 삽입

```kotlin
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="16dp">

    <ImageView
        android:id="@+id/icon_cost_of_service"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:importantForAccessibility="no"
        app:layout_constraintBottom_toBottomOf="@id/cost_of_service"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@id/cost_of_service"
        app:srcCompat="@drawable/ic_store" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/cost_of_service"
        android:layout_width="160dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:hint="@string/cost_of_service"
        app:layout_constraintStart_toEndOf="@id/icon_cost_of_service"
        app:layout_constraintTop_toTopOf="parent">

        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/cost_of_service_edit_text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="numberDecimal" />
    </com.google.android.material.textfield.TextInputLayout>
	...

</androidx.constraintlayout.widget.ConstraintLayout>
```

`app:srcCompat` 드로어블 리소스를 이 XML 줄 옆에 아이콘의 미리보기 표시 

→ `android:importantForAccessibility="no"`  이미지가 장식용일 때 설정.

## 스타일

styles.xml

```kotlin
<resources>
		// 텍스트 뷰
    <style name="Widget.TipTime.TextView" parent="Widget.MaterialComponents.TextView">
        <item name="android:minHeight">48dp</item>
        <item name="android:gravity">center_vertical</item>
        <item name="android:textAppearance">?attr/textAppearanceBody1</item>
    </style>

		// 버튼
    <style name="Widget.TipTime.CompoundButton.RadioButton" parent="Widget.MaterialComponents.CompoundButton.RadioButton">
        <item name="android:paddingStart">8dp</item>
        <item name="android:textAppearance">?attr/textAppearanceBody1</item>
    </style>

		//스위치
    <style name="Widget.TipTime.CompoundButton.Switch" parent="Widget.MaterialComponents.CompoundButton.Switch">
        <item name="android:minHeight">48dp</item>
        <item name="android:gravity">center_vertical</item>
        <item name="android:textAppearance">?attr/textAppearanceBody1</item>
    </style>
</resources>
```

`android:minHeight` TextView에서 최소 높이 48dp를 설정

→ 모든 행의 최소 높이는 머티리얼 디자인 가이드라인에 따라 48dp

`android:gravity` TextView에서 세로로 중앙에 텍스트를 배치

→ 중력값 : center, center_vertical, center_horizontal, top, bottom 

`TextAppearance` 텍스트 크기, 글꼴, 기타 텍스트 속성과 관련된 일련의 사전 제작된 스타일

`paddingStart`  구성요소의 시작 부분에만 패딩을 설정

패딩 : 뷰의 콘텐츠와 뷰 경계 사이의 공간 크기

## 테마

나중에 스타일, 레이아웃 등에 참조할 수 있는 명명된 리소스(테마 속성이라고 함)의 모음

## 스크롤 뷰

```kotlin
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

## 키보드 숨기기

MainActivity.kt

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
   ...

   setContentView(binding.root)

   binding.calculateButton.setOnClickListener { calculateTip() }

   binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> 
handleKeyEvent(view, keyCode)
   }
}

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

`handleKeyEvent()`는 keyCode 입력 매개변수가 `KeyEvent.KEYCODE_ENTER`와 같은 경우 터치 키보드를 숨기는 비공개 도우미 함수

`OnKeyListener`  키 누름이 발생할 때 트리거되는 `onKey()` 메서드가 존재 

`onKey()` 입력 인수 세 개, 즉 뷰, 누른 키의 코드, 키 이벤트(사용 안 할 경우 : _)를 사용