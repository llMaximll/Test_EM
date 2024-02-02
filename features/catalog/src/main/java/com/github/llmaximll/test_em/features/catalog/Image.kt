package com.github.llmaximll.test_em.features.catalog

import androidx.annotation.DrawableRes

enum class Image(
    val ids: List<String>,
    @DrawableRes val drawableRes: Int
) {
    IMAGE_1(
        ids = listOf(
            "54a876a5-2205-48ba-9498-cfecff4baa6e",
            "55f58865-ae74-4b7c-9d81-b78334bb97db",
            "15f88865-ae74-4b7c-9d81-b78334bb97db"
        ),
        drawableRes = R.drawable.cover_1
    ),
    IMAGE_2(
        ids = listOf(
            "54a876a5-2205-48ba-9498-cfecff4baa6e",
            "26f88856-ae74-4b7c-9d85-b68334bb97db"
        ),
        drawableRes = R.drawable.cover_2
    ),
    IMAGE_3(
        ids = listOf(
            "16f88865-ae74-4b7c-9d85-b68334bb97db",
            "26f88856-ae74-4b7c-9d85-b68334bb97db",
            "88f88865-ae74-4b7c-9d81-b78334bb97db"
        ),
        drawableRes = R.drawable.cover_3
    ),
    IMAGE_4(
        ids = listOf(
            "16f88865-ae74-4b7c-9d85-b68334bb97db",
            "88f88865-ae74-4b7c-9d81-b78334bb97db"
        ),
        drawableRes = R.drawable.cover_4
    ),
    IMAGE_5(
        ids = listOf(
            "75c84407-52e1-4cce-a73a-ff2d3ac031b3",
            "cbf0c984-7c6c-4ada-82da-e29dc698bb50",
            "55f58865-ae74-4b7c-9d81-b78334bb97db"
        ),
        drawableRes = R.drawable.cover_5
    ),
    IMAGE_6(
        ids = listOf(
            "cbf0c984-7c6c-4ada-82da-e29dc698bb50",
            "75c84407-52e1-4cce-a73a-ff2d3ac031b3",
            "15f88865-ae74-4b7c-9d81-b78334bb97db"
        ),
        drawableRes = R.drawable.cover_6
    ),
}
