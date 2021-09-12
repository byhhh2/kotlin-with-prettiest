package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity //프레임워크
import android.os.Bundle
import android.widget.Button //Alt + enter로 추가, 자동가져오기 설정하면 자동으로 가져와짐
import android.widget.ImageView
import android.widget.TextView

//import android.widget.Toast
//import java.util.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //main함수가 존재하지 않고 앱이 처음 열릴때 onCreate함수 실행
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //시작 레이아웃 설정

        val rollButton: Button = findViewById(R.id.button) //id == button
        //객체 참조 저장

        rollButton.setOnClickListener { //클릭 리스너
            //Toast : 사용자에게 표시되는 간략한 메시지
            //Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show()
            //val resultTextView: TextView = findViewById(R.id.textView)
            //resultTextView.text = "6" //버튼이 클릭될 때 text의 문자 6으로 바꾸기
            rollDice()
        }
        rollDice() //처음 시작했을 때 주사위 보이게
    }

    private fun rollDice() {
        val dice = Dice(6) //6면 주사위 만들기
        val diceRoll = dice.roll() //주사위 굴리기
        val diceImage: ImageView = findViewById(R.id.imageView)
//        when (diceRoll) {
//            1 -> diceImage.setImageResource(R.drawable.dice_1)
//            2 -> diceImage.setImageResource(R.drawable.dice_2)
//            3 -> diceImage.setImageResource(R.drawable.dice_3)
//            4 -> diceImage.setImageResource(R.drawable.dice_4)
//            5 -> diceImage.setImageResource(R.drawable.dice_5)
//            6 -> diceImage.setImageResource(R.drawable.dice_6)
//        }
        val drawableResource = when (diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            6 -> R.drawable.dice_6
            else -> R.drawable.dice_6
        }

        diceImage.setImageResource(drawableResource)
        diceImage.contentDescription = diceRoll.toString() //콘텐츠 설명 업데이트
    }
}

class Dice(val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}

