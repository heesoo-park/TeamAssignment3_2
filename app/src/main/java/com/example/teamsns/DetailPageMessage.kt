package com.example.teamsns

import androidx.annotation.StringRes

enum class DetailPageMessage(
    @StringRes val message: Int,
){
    MYPAGE(R.string.detail_activity_my_page),
    DETAIL(R.string.detail_activity_detail),
    SHOWMORE(R.string.show_more),
    SHOWCLOSE(R.string.show_close)
}