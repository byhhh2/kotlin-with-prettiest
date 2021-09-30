/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.wordsapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.ActivityMainBinding

/**
 * Main Activity and entry point for the app. Displays a RecyclerView of letters.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true //레이아웃 상태 추적

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        // Sets the LinearLayoutManager of the recyclerview

        //recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = LetterAdapter()
        // 위의 두줄 작업을 chooseLayout에서 해주므로 지운다
        chooseLayout()
    }

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        recyclerView.adapter = LetterAdapter()
        //어댑터 할당
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this, R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this, R.drawable.ic_linear_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 옵션 메뉴를 확장하여 추가 설정 실행
        // 레이아웃에 따라 아이콘이 올바른지 확인 = setIcon

        menuInflater.inflate(R.menu.layout_menu, menu)
        //실제 뷰로 만듦

        val layoutButton = menu?.findItem(R.id.action_switch_layout)
        //레이아웃 변경하는 아이템을 찾아서 걔를 setIcon에 넣어줘서 icon 변경
        setIcon(layoutButton)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //레이아웃 변경 아이템 버튼이 클릭될 때 chooseLayout()호출
        //id가 action_switch_layout인 항목을 클릭하면
        //isLinearLayoutManager 값을 바꿔줌


        //메뉴가 클릭될 때마다 호출되는데 그 메뉴중에 어떤 항목을 누르는지 확인해야함
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout() //레이아웃 바꾸고
                setIcon(item) //그 레이아웃에 맞게 아이콘도 바꿈

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
