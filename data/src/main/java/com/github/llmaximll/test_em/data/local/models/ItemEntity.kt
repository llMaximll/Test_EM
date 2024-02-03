package com.github.llmaximll.test_em.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.data.remote.models.ItemDto

@Entity("item")
data class ItemEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("subtitle") val subtitle: String,
    @Embedded val price: PriceEntity?,
    @Embedded val feedback: FeedbackEntity?,
    @ColumnInfo("tags") val tags: List<String>,
    @ColumnInfo("available") val available: Int,
    @ColumnInfo("description") val description: String,
    val info: List<InfoEntity>,
    @ColumnInfo("ingredients") val ingredients: String,
    @ColumnInfo val isFavorite: Boolean
) {
    data class PriceEntity(
        @ColumnInfo("price") val price: Int,
        @ColumnInfo("discount") val discount: Int,
        @ColumnInfo("priceWithDiscount") val priceWithDiscount: Int,
        @ColumnInfo("unit") val unit: String,
    )

    data class FeedbackEntity(
        @ColumnInfo("count") val count: Int,
        @ColumnInfo("rating") val rating: Float
    )

    data class InfoEntity(
        @ColumnInfo("title") val title: String,
        @ColumnInfo("value") val value: String
    )

    companion object {
        fun ItemEntity.asModel(): Item {
            return Item(
                id = this.id,
                title = this.title,
                subtitle = this.subtitle,
                price = this.price?.asModel(),
                feedback = this.feedback?.asModel(),
                tags = this.tags,
                available = this.available,
                description = this.description,
                info = this.info.map { it.asModel() },
                ingredients = this.ingredients,
                isFavorite = this.isFavorite
            )
        }

        private fun PriceEntity.asModel(): Item.Price {
            return Item.Price(
                price = this.price,
                discount = this.discount,
                priceWithDiscount = this.priceWithDiscount,
                unit = this.unit
            )
        }

        private fun FeedbackEntity.asModel(): Item.Feedback {
            return Item.Feedback(
                count = this.count,
                rating = this.rating
            )
        }

        private fun InfoEntity.asModel(): Item.Info {
            return Item.Info(
                title = this.title,
                value = this.value
            )
        }
    }
}