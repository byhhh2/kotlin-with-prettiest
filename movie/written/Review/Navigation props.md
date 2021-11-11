```kotlin
 navController = Navigation.findNavController(itemView)
                    val bundle = bundleOf("title" to item.title.replace("<b>", "").replace("</b>", ""), "director" to item.director.replace("|", ""), "star" to "⭐".plus(item.star.toString()))
 navController!!.navigate(R.id.action_navigation_home_to_navigation_dashboard, bundle)

```

```kotlin
binding.TextTitle.setText(arguments?.getString("title"))
binding.TextDirector.setText(arguments?.getString("director"))
binding.TextStar.setText(arguments?.getString("star"))
```
