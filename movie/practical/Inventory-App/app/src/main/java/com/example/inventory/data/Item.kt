package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

//데이터 클래스
@Entity(tableName = "item")
data class Item( //생성자
    @PrimaryKey(autoGenerate = true) //각 Item의 아이디 자동 생성
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val itemName: String,
    @ColumnInfo(name = "price")
    val itemPrice: Double,
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int
)

// 가격을 나라에 맞게
fun Item.getFormattedPrice(): String =
        NumberFormat.getCurrencyInstance().format(itemPrice)