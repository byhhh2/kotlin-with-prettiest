/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentAddItemBinding

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    // ViewModel 선언
    private val viewModel: InventoryViewModel by activityViewModels { //람다
        // InventoryViewModelFactory() 생성자를 호출
        InventoryViewModelFactory(
                (activity?.application as InventoryApplication).database
                        .itemDao() //DAO 전달 (itemDao 생성자를 호출)
        )
    }

    lateinit var item: Item

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    // TextFields가 비어있는지 확인하는 함수
    // ViewModel에 있는 TextFields가 비어있는지 확인하는 함수 호출 후 return값 return
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString()
        )
    }

    // Item을 추가하는 함수
    private fun addNewItem() {
        // TextFields가 비어있지 않다면 Item추가
        if (isEntryValid()) {
            viewModel.addNewItem(
                    binding.itemName.text.toString(),
                    binding.itemPrice.text.toString(),
                    binding.itemCount.text.toString(),
            )
        }

        // 아이템 리스트로 되돌아가기
        val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) { // * EDIT
            viewModel.retrieveItem(id).observe(this.viewLifecycleOwner) { selectedItem ->
                item = selectedItem
                bind(item) //이 item으로 textView 채워 넣기 // ==
            }
        } else { // * ADD
            // 클릭리스너 추가
            // SAVE 버튼 누르면 실행
            binding.saveAction.setOnClickListener {
                addNewItem() // ==
            }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    //edit창으로 사용할 때 TextView 채워 놓기 (수정이니깐)
    private fun bind(item: Item) {
        val price = "%.2f".format(item.itemPrice)

        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE) //spannable 디자인 효과
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() } // Edit 누르고 save 클릭
        }
    }

    private fun updateItem() {
        if (isEntryValid()) { // 수정할때도 Textfield 채워야하므로 비워있는지 확인
            viewModel.updateItem( // Item 수정하는 함수 호출 
                    this.navigationArgs.itemId,
                    this.binding.itemName.text.toString(),
                    this.binding.itemPrice.text.toString(),
                    this.binding.itemCount.text.toString()
            )
            
            //수정하고 List로 이동
            val action = AddItemFragmentDirections.actionAddItemFragmentToItemListFragment()
            findNavController().navigate(action)
        }
    }

}
