

### Material
고품질 디지털 환경을 구축할 수 있도록 Google이 만든 설계 시스템

- color tool <br>
https://material.io/resources/color/#!/?view.left=0&view.right=0

- 어두운 모드 <br>
API 28(Android9), 29(Android10) 이상 필요. <br>
Design Editor 에서 나이트 모드 미리보기 가능

<br><br><br>


###  앱 아이콘 변경 
* Image Asset Studio를 사용하여 앱의 레거시 및 적응형 아이콘 생성<br>
* 프로젝트뷰 > app > src > main > res > mipmap폴더 : 앱의 런처 아이콘 애셋을 배치해야하는 위치 <br>
* 기기 제조업체(OEM)의 마스크 모양에 따라 아이콘의 가장자리가 잘릴 수 있으므로 아이콘의 핵심 정보를 레이어 중앙의 지름 66dp 원 모양인 '안전 영역'에 배치해야함
<br><br><br>


### 밀도 한정자
현재 화면 밀도를 바탕으로 Android는 가장 가까운 큰 밀도 버킷의 리소스를 선택한 후 축소
* mdpi - 중밀도 화면의 리소스(~160dpi)
* hdpi - 고밀도 화면의 리소스 (~240dpi)
* xhdpi - 초고밀도 화면의 리소스(~320dpi)
* xxhdpi - 초초고밀도 화면의 리소스(~480dpi)
* xxxhdpi - 초초초고밀도 화면의 리소스(~640dpi)
* nodpi - 화면의 픽셀 밀도와 관계없이 조정할 수 없는 리소스
* anydpi - 어떤 밀도로도 조정 가능한 리소스
<br><br><br>



### 적응형 아이콘
API 26(Android 8.0) 이상 에서 적용
* 관련폴더 <br>
> res/drawable-anydpi-v26/ic_lancher_background.xml <br>
res/drawable-anydpi-v26/ic_launcher_foreground.xml <br>
res/mipmap-anydpi-v26/ic_launcher.xml<br>
res/mipmap-anydpi-v26/ic_launcher_round.xml<br>
 
그 이하 기기는 레거시 런처 아이콘 사용
* 관련 폴더
> res/mipmap-mdpi/ic_launcher.png   <br>
res/mipmap-mdpi/ic_launcher_round.png <br>
res/mipmap-hdpi/ic_launcher.png <br>
res/mipmap-hdpi/ic_launcher_round.png <br>
res/mipmap-xhdpi/ic_launcher.png <br>
res/mipmap-xhdpi/ic_launcher_round.png <br>
res/mipmap-xxdpi/ic_launcher.png <br>
res/mipmap-xxdpi/ic_launcher_round.png <br>
res/mipmap-xxxdpi/ic_launcher.png <br>
res/mipmap-xxxdpi/ic_launcher_round.png <br>

 <br><br><br>

### foreground  / background
&#45; background 레이어 위 foreground 레이어가 쌓이고 그 위 마스크(원형 등) 적용 <br>
> project 뷰 > app > src > main > res > mipmap-anydpi-v26

: 리소스 드로어블을 제공하여 앱 아이콘의 background, foreground 레이어를 선언 <br>
> drawable > ic_launcher_background.xml <br> 
drawable-v24 > ic_launcher_foreground.xml<br>

: 벡터 드로어블 파일. 픽셀 단위의 크기 고정 x

<br><br><br>

### 벡터 드로어블 vs 비트맵 이미지
* 비트맵 이미지 : 각 픽셀의 색상 정보
* 벡터 그래픽 : 이미지를 정의하는 모양을 그리는 방법을 알고 있음. 색상 정보, 점, 선, 곡선으로 구성. 화질 저하 없이 모든 화면 밀도의 어떤 캔버스 크기로도 조정할 수 있음
* 벡터 드로어블 : Android의 벡터 그래픽 구현. XML로 정의

<br><br><br>

### 새 image asset 만들기
1. res 폴더 우클릭 > New > Image Asset 선택 or Resource Manager 탭 클릭 > +아이콘을 클릭 > Image Asset 선택
2. Icon Type : Launcher Icons (Adaptive and Legacy) <br>
   Name : ic_launcher
3. Foreground Layer 탭에서 파일 선택
4. Background Layer 탭에서 파일 선택
5. 미리보기로 아이콘 확인 기기제조업체(OEM)은 앱 아이콘에 적용되는 마스크 제공
6. 각 레이어 탭 > Scaling 섹션 > Resize 로 크기 조정
7. Confirm Icon Path 단계. 개별 파일을 클릭하여 미리보기 확인

<br><br><br>

### 벡터 드로어블 파일을 -v26 디렉터리로 이동
1. drawable-anydpi-v26 디렉터리 생성 <br>
   : res 폴더 우클릭 >  New > Android Resource Directory > Directory name, Resource type 선택
2. drawable 폴더, drawable-v24 폴더의 파일 이동
3. 빈 drawable-v24 폴더 삭제

<br><br><br>

 앱에 아이콘 추가
1. Resource Manager 탭 > + 버튼 > Vector Asset 선택
2. Asset Studio 대화상자 > Asset Type 섹션 > Clip Art 선택
3. Clip Art 섹션에서 아이콘 선택
4. API 21 미만의 플랫폼 버전에서는 build.gradle에 아래 코드 추가
```kotlin
android {
  defaultConfig {
    ...
    vectorDrawables.useSupportLibrary = true
   }
   ...
}
```
5. ConstraintLayout의 하위 요소로 imageview 추가
6. 레이아웃 제약조건 수정


<br><br><br>

### 스타일 만들기
스타일을 한 번 정의한 후에는 레이아웃의 모든 TextViews에 적용 가능 <br>
동일한 속성을 스타일과 레이아웃 파일에서 동시에 지정할 경우 레이아웃 파일의 값이 적용됨
1. res > styles.xml 파일 생성
2. 속성 재정의<br>
   > android:minHeight : meterial 가이드라인에 따라 48dp <br>
 android:gravity : 뷰 안의 콘텐츠가 베치되는 방식 제어 (center, center_horizontal, center_vertical, top, bottom)<br>
 android:textAppearance : 텍스트 모양 속성(크기, 글꼴 등)
3. res > dimens.xml 파일로 자주 사용하는 값 관리
4. 테마에 스타일 추가. night도 동시에 변경


<br><br><br>

### 기기 회전
UI가 잘리는 문제 해결 : ConstraintLayout를 ScrollView로 감싸기 

<br><br><br>

### enter키 누르면 키보드 숨기기
1. MainActivity 클래스에 handleKeyEvent() 메서드 추가

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

2. MainActivity 클래스 > onCreate() 에 리스너 설정
   ```kotlin
   binding.costOfServiceEditText.setOnKeyListener{ view, keyCode, _ -> handleKeyEvent(view, keyCode)}
   ```
   onKey{입력 인수 (뷰, 누른 키 코드, 키 이벤트) -> 이벤트핸들러메서드 }
키 이벤트 사용x : `_` 로 표시

<br><br><br>

### 벡터 드로어블의 색조 조정하기
1. res > drawable > <icon>.xml 
2.  android:tint 속성 값 변경 
  
<br><br><br>
<hr>
<br>


### <퀴즈>
* gradle  <br>
a. 앱을 빌드하는 데 사용하는 자동화된 빌드 시스템 <br>
a. 기기에 앱 설치를 처리함 <br>
a. 프로젝트에 대한 Android 관련 옵션을 구성 <br>

* Material Components를 사용하는 이유 <br>
a. 텍스트 필드 및 스위치와 같은 머티리얼 디자인 지침을 따르는 위젯을 제공함 <br>
a. 직접 사용하거나 확장한 다음 사용자 지정할 수 있는 기본 머티리얼 테마를 제공함 <br>
a. ux를 더 빠르게 구축하는데 도움 <br>




