package com.example.affirmations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

// 추상클래스 RecyclerView.Adapter, 뷰 홀더 유형 ItemAdapter.ItemViewHolder
// 추상클래스의 구현해야 하는 클래스 알려면 ctrl + i (itemAdapter에 마우스)
class ItemAdapter(private val context: Context, private val dataset: List<Affirmation>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    // context : 문자열 리소스 확인 정보, 기타 앱 관련 정보
    // ViewHolder : 항목 레이아웃 안 개별 뷰 참조, 뷰를 효율적으로 이동하기 위한 정보

    //중첩클래스, ItemAdapter만 ItemViewHolder를 사용
    //ItemViewHolder는 ViewHolder의 서브클래스
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        // list_item.xml에서의 item
    }

    //RecyclerView의 새 뷰 홀더를 만들기 위해 레이아웃 관리자가 호출 (기존 뷰 홀더가 없는 경우)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // onCreateViewHolder() 메서드는 두 매개변수를 사용하며 새 ViewHolder를 반환
        // parent : (상위 요소 = RecyclerView) 새 목록 항목 뷰가 하위요소로 사용되어 연결되는 뷰 그룹
        // viewType : RecyclerView에 항목 뷰 유형이 여러개 있을 때 (같은 뷰로 이뤄지지 않은)
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        //LayoutInflater : xml 레이아웃을 뷰 객체의 계층 구조로 확장하는 방법을 앎..
        //inflage() 메서드를 호출하면, apapterLayout은 목록 항목 뷰의 참조를 보유

        return ItemViewHolder(adapterLayout)
    }

    // 목록 항목 뷰의 콘텐츠를 바꾸기 위해 레이아웃 관리자가 호출
    // 위치를 기반으로 데이터 세트에서 올바른 Affirmation 객체를 찾는다.
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //holder : onCreateViewHolder()에서 생성된 ItemViewHolder
        //position : 목록에서 현재 항목의 position을 나타냄

        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.stringResourceId)
        holder.imageView.setImageResource(item.imageResourceId)
        //context.resources.getString(<문자열 리소스 ID>)
    }

    override fun getItemCount() = dataset.size
    // 데이터 세트의 크기 반환
    // = (==) return

}