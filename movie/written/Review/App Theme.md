```
res > values > color.xml
res > values > themes > themes.xml
```

- [색상 도구](https://material.io/resources/color/#!/?view.left=0&view.right=0)
- `color`에 있는 색상을 가지고 와서 `theme`에서 지정해줌

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Theme.MyCinema" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>
        <item name="colorOnSecondary">@color/black</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
        <!-- Customize your theme here. -->
    </style>
</resources>
```

|  #  |    이름    |       테마 속성       |
| :-: | :--------: | :-------------------: |
|  1  |    기본    |     colorPrimary      |
|  2  | 기본 변형  |  colorPrimaryVariant  |
|  3  |    보조    |    colorSecondary     |
|  4  | 보조 변형  | colorSecondaryVariant |
|  5  |    배경    |    colorBackground    |
|  6  |    표면    |     colorSurface      |
|  7  |    오류    |      colorError       |
|  8  | 기본(대비) |    colorOnPrimary     |
|  9  | 보조(대비) |   colorOnSecondary    |
| 10  | 배경(대비) |   colorOnBackground   |
| 11  | 표면(대비) |    colorOnSurface     |
| 12  | 오류(대비) |     colorOnError      |
