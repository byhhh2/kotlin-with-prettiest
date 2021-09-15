package com.example.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  //set start layout

        //버튼 클릭시 이벤트 발생
        val rollButton: Button = findViewById(R.id.button)
        rollButton.setOnClickListener {
            //toast 사용
            //val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT)
            //toast.show()

            //TextView 업데이트
            rollDice()

        }
    }

    private fun rollDice() {
        val dice1 = Dice(6)
        val dice2 = Dice(6)
//        val dice1Roll = dice1.roll()
        val resultTextView1: TextView = findViewById(R.id.textView)
        val resultTextView2: TextView = findViewById(R.id.textView2)
        //resultTextView.text = "6"
        resultTextView1.text = dice1.roll().toString()
        resultTextView2.text = dice2.roll().toString()

    }
}

class Dice(val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }


}




