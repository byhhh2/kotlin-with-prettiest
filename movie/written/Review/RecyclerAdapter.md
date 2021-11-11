```kotlin
package com.example.mycinema.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycinema.data.Movie
import com.example.mycinema.R


class ItemAdapter(private val context: Context, private val dataset: List<Movie>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.item_title)
        val textViewDirector: TextView = view.findViewById(R.id.item_director)
        val textViewStar: TextView = view.findViewById(R.id.item_star)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)


        return ItemViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = dataset[position]
        holder.textViewTitle.text = item.title
        holder.textViewDirector.text = item.director
        holder.textViewStar.text = item.star.toString()

    }

    override fun getItemCount() = dataset.size
    // 데이터 세트의 크기 반환
    // = (==) return

}
```

```kotlin
binding.recyclerView.adapter = context?.let { it1 -> ItemAdapter(it1, html) }
```
