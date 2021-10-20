package com.example.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding

/**
 * [ListAdapter] implementation for the recyclerview.
 */

//onItemClicked() 생성자 매개변수
class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
        ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback) {
        //ListAdapter로 확장 (매개변수 : Item, ItemListAdapter.ItemViewHolder)
        //DiffCallback 생성자 매개변수 : 변경내용 파악

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
                ItemListItemBinding.inflate(
                        LayoutInflater.from(
                                parent.context
                        )
                ) // ViewHolder 반환
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position) //현재 항목의 위치 전달

        //Item이 클릭되면
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    //ViewHolder
    class ItemViewHolder(private var binding: ItemListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        //bind
        fun bind(item: Item) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemQuantity.text = item.quantityInStock.toString()
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }
        }
    }
}