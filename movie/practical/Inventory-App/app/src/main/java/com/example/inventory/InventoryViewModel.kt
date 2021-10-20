package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()
    // DAO getItems()함수를 통해 Items 가져오기

    // insert
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            //코루틴 시작
            itemDao.insert(item) //suspend 함수 호출
        }
    }

    //문자열을 통해 Item을 채우고 반환
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
                itemName = itemName,
                itemPrice = itemPrice.toDouble(),
                quantityInStock = itemCount.toInt()
        )
    }

    // Item을 만들고 Insert하는 함수
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    // TextFields가 비어있지 않은지 확인하는 함수
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    //Item의 세부사항 보여주는 함수
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    //Item을 업데이트 : 팔았을 때 재고 하나 줄이는거 update
    // : Item 내용 수정
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    //Item 을 sell하는 함수
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    // 수량이 0개가 넘어서 판매 가능한지 확인하는 함수
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    // Item을 삭제하는 함수
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            //코루틴 시작
            itemDao.delete(item)
        }
    }

    // Item을 업데이트하는 함수
    private fun getUpdatedItemEntry(
            itemId: Int,
            itemName: String,
            itemPrice: String,
            itemCount: String
    ): Item {
        return Item( // Item return
                id = itemId,
                itemName = itemName,
                itemPrice = itemPrice.toDouble(),
                quantityInStock = itemCount.toInt()
        )
    }

    //getUpdatedItemEntry 호출해서 수정한 Item을 리턴 받고 그 Item으로 수정
    fun updateItem(
            itemId: Int,
            itemName: String,
            itemPrice: String,
            itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        // ViewModel 반환
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}