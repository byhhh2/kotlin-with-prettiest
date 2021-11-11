## gradle

```
//build.gradle(project)

buildscript {
    ext {
        ..
        nav_version = "2.3.1" //
    }
}

//build.gradle(app)

..
implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

```

<br>

## NavHostFragment 설정하기 (XML)

```xml
//main_activity.xml
<ConstraintLayout>
    <Fragment
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment" // NavHostFragment로 설정
        app:navGraph="@navigation/mobile_navigation" // 네비게이션(Fragment) 연결
        >
    </Fragment>
</ConstraintLayout>

//mobile_navigation.xml
//File > New > Android Resource File > Resource Type : Navigation

<navigation
    android:id="@+id/mobile_navigation"
    app:startDestination="@+id/navigation_home" // 시작 Fragment
>
    <fragment
        android:id="@+id/navigation_home"
    />
    <fragment />
    <fragment />
    ..
</navigation>
```

<br>

## NavHostFragment 설정하기 (Main_Activity)

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view) // bottom nav

        val navController = findNavController(R.id.nav_host_fragment) //nav controller 설정

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))

        setupActionBarWithNavController(navController, appBarConfiguration) // 상단 액션바 설정

        navView.setupWithNavController(navController) // bottom nav의 nav controller 설정
    }
```

<br>

## 인수 전달

```kotlin
// RecyclerView Adapter
        holder.apply {
            with(holder.itemView) {
                itemView.setOnClickListener{
                    navController = Navigation.findNavController(itemView)
                    val bundle = bundleOf("title" to item.title.replace("<b>", "").replace("</b>", ""), "director" to item.director.replace("|", ""), "star" to "⭐".plus(item.star.toString()))
                    navController!!.navigate(R.id.action_navigation_home_to_navigation_dashboard, bundle)
                }
            }
        }


// 도착지 onCreateView

binding.TextTitle.setText(arguments?.getString("title"))
binding.TextDirector.setText(arguments?.getString("director"))
binding.TextStar.setText(arguments?.getString("star"))

```
