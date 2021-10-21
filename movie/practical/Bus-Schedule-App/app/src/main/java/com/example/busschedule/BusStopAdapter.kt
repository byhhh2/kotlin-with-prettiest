package com.example.busschedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.busschedule.databinding.BusStopItemBinding
import database.schedule.Schedule
import java.text.SimpleDateFormat
import java.util.*

class BusStopAdapter(private val onItemClicked: (Schedule) -> Unit) : ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallback) {

    // ViewHolder class
    class BusStopViewHolder(private var binding: BusStopItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat") // 오류 방지
        fun bind(schedule: Schedule) { // bind()
            binding.stopNameTextView.text = schedule.stopName
            binding.arrivalTimeTextView.text = SimpleDateFormat(
                "h:mm a").format(Date(schedule.arrivalTime.toLong() * 1000)
            )
        }
    }

    // onCreateViewHolder [return ViewHolder]
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        val viewHolder = BusStopViewHolder(
            BusStopItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            ) // inflate
        )

        // item 하나가 클릭되었을 때
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            // 인자로 받아온 함수 실행 (onItemClicked)
            // FullScheduleFragment.kt에서 navigate() 함수 넘겨줌
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    // onBindViewHolder [bind()]
    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // DiffCallback object - 차이 계산
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Schedule>() {
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }
}

