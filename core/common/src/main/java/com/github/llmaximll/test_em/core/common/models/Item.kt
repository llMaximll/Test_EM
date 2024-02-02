package com.github.llmaximll.test_em.core.common.models

data class Item(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: Price?,
    val feedback: Feedback?,
    val tags: List<String>,
    val available: Int,
    val description: String,
    val info: List<Info>,
    val ingredients: String,
) {
    data class Price(
        val price: Int,
        val discount: Int,
        val priceWithDiscount: Int,
        val unit: String,
    )

    data class Feedback(
        val count: Int,
        val rating: Float
    )

    data class Info(
        val title: String,
        val value: String
    )
}