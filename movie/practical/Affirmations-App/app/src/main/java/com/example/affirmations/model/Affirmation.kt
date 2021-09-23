package com.example.affirmations.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Affirmation(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
) { //데이터 클래스
// annotation : 매개변수에 부가적인 정보 줌 (뭐가 뭔지)


}