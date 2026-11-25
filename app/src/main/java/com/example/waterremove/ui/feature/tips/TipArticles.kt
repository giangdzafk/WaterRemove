package com.example.waterremove.ui.feature.tips

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.waterremove.R


data class TipArticle(
    @param:DrawableRes val image: Int,
    @param:StringRes val title: Int,
    @param:StringRes val content: Int,
)

object TipArticles {
    val all = listOf(
        TipArticle(R.drawable.tip_01, R.string.title_topic_1, R.string.Topic_1),
        TipArticle(R.drawable.tip_02, R.string.title_topic_2, R.string.topic_2),
        TipArticle(R.drawable.tip_03, R.string.title_topic_3, R.string.topic_3),
        TipArticle(R.drawable.tip_04, R.string.title_topic_4, R.string.topic_4),
        TipArticle(R.drawable.tip_05, R.string.title_topic_5, R.string.topic_5),
    )
}
