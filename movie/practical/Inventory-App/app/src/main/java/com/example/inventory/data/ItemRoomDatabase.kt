package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Item을 entities 목록이 있는 유일한 클래스로
// version은 스키마를 변경할 때마다 높아짐
// exportSchema = false 스키마 버전 기록 백업 유지 안함
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao //ItemDao 반환

    // 데이터베이스를 만들거나 있으면 반환
    companion object {
        // 데이터베이스의 단일 인스턴스 참조
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null


        //데이터 베이스를 반환
        fun getDatabase(context: Context): ItemRoomDatabase {
            // 동시에 데이터베이스 인스턴스를 요쳥할 때 방지용
            // synchronized 블록 내에 데이터베이스를 가져오면 한 번에 한 스레드만 이 코드 블록에 들어갈 수 있음
            // 무조건 데베가 한번만 초기화 된다.
            return INSTANCE ?: synchronized(this) { // 인스턴스를 로킹
                val instance = Room.databaseBuilder( //데이터베이스 가져오기
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration() //스키마 변경에 대한것.. migrate(이전), 간단한 방법으로 데베 제거후 다시빌드
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}