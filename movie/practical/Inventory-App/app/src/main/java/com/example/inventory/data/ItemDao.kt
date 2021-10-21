package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE) //ID가 충돌할 경우 무시하는 전략
    suspend fun insert(item: Item)
    //데이터베이스 작업이 오래 걸리므로, 코루틴에서 함수를 호출하도록 suspend함수로 만들어 준다.

    // update
    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    // id기반으로 Item찾기
    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>
    // Flow를 통해 데이터를 자동 업데이트
    // Flow 반환 type을 사용하면 Room이 자동으로 백그라운드 스레드에서 쿼리를 수행하므로 suspend 사용할 필요 없음

    //이름 오름차순으로 SELECT ALL
    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>
}