package com.example.affirmations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.adapter.ItemAdapter
import com.example.affirmations.data.Datasource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myDataset = Datasource().loadAffirmations()
        //DataSourse인스턴스를 만들고 loadAffirmations() 호출

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, myDataset)
        //ItemAdapter 클래스를 사용하도록 recyclerView에 알리기

        recyclerView.setHasFixedSize(true) // 성능개선
        // RecyclerView 레이아웃의 크기가 고정되어 있으므로
    }
}