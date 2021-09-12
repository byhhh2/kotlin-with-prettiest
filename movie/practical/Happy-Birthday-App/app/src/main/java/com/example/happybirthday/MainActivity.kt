package com.example.happybirthday

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cnt : Int = 0;
        val _image : ImageView = findViewById(R.id.imageView2)
        val fire : TextView = findViewById(R.id.textView5)

        _image.setOnClickListener {
            cnt++

            if (cnt % 2 != 0) {
                fire.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else {
//                fire.setTextColor(0x01060016)
                fire.setTextColor(Color.RED)
            }
        }
    }
}