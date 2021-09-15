package com.example.diceroller

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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

        //시작 시 자동으로 주사위 굴리기
        rollDice() 
    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()

////        textView 사용 : 텍스트로 결과값 보여줌
//        val resultTextView: TextView = findViewById(R.id.textView)
//        //resultTextView.text = "6"
//        resultTextView.text = dice.roll().toString()

////        주사위 2개 동시에
//        val dice2 = Dice(6)
//        val resultTextView2: TextView = findViewById(R.id.textView2)
//        resultTextView2.text = dice2.roll().toString()

        val diceImg: ImageView = findViewById(R.id.imageView)
        val drawableResource = when (diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }

        diceImg.setImageResource(drawableResource)
        diceImg.contentDescription = diceRoll.toString()



    }
}

class Dice(val numSides: Int) {

    fun roll(): Int {
        return (1..numSides).random()
    }


}




