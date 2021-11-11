```
-- ConstraintLayout (하위 뷰를 여러개 배치하고 싶을 때)
 ⊦ TextViews
 ⊦ Buttons
 ⊦ ..
```

<br>

### 패딩

```
android:padding="16dp"
```

<br>

### 위치 제약조건

```
layout_constraint<Source>_to<Target>Of

app:layout_constraintStart_toStartOf="parent"
app:layout_constraintTop_toTopOf="parent"
app:layout_constraintTop_toBottomOf="@id/cost_of_service"
```

<br>

### 세로방향 or 가로방향

```
android:orientation="vertical"
android:orientation="horizontal"
```

<br>

### 라디오 버튼

```xml
<RadioGroup
   android:id="@+id/tip_options"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   app:layout_constraintTop_toBottomOf="@id/service_question"
   app:layout_constraintStart_toStartOf="parent"
   android:orientation="vertical">

   <RadioButton
       android:id="@+id/option_twenty_percent"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="Amazing (20%)" />

   <RadioButton
       android:id="@+id/option_eighteen_percent"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="Good (18%)" />

   <RadioButton
       android:id="@+id/option_fifteen_percent"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:text="OK (15%)" />

</RadioGroup>
```

<br>

### `ConstraintLayout`의 UI 요소에는 `match_parent`를 설정할 수 없다

- 대신 뷰의 시작 및 끝 가장자리를 제한하고 너비를 0dp로 설정해야 합니다. 너비를 0dp로 설정하면 시스템에서 너비를 계산하지 않고 뷰에 적용된 제약 조건을 일치시키려고만 합니다.

```xml
<Switch
    android:id="@+id/round_up_switch"
    android:layout_width="0dp" //
    android:layout_height="wrap_content"
    android:checked="true"
    android:text="Round up tip?"
    app:layout_constraintEnd_toEndOf="parent" //
    app:layout_constraintStart_toStartOf="parent" //
    app:layout_constraintTop_toBottomOf="@id/tip_options" />
```

- `tip_options` 밑에 배치 하면서 너비를 부모와 맞추기 위해

<br>
