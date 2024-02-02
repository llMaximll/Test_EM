package com.github.llmaximll.test_em.data.remote.models

import com.github.llmaximll.test_em.core.common.models.Item
import com.github.llmaximll.test_em.data.local.models.ItemEntity
import com.google.gson.annotations.SerializedName

data class ItemsDto(
    @SerializedName("items") val items: List<ItemDto>
)

data class ItemDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("price") val price: PriceDto? = null,
    @SerializedName("feedback") val feedback: FeedbackDto? = null,
    @SerializedName("tags") val tags: List<String>? = null,
    @SerializedName("available") val available: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("info") val info: List<InfoDto>? = null,
    @SerializedName("ingredients") val ingredients: String? = null,
) {
    data class PriceDto(
        @SerializedName("price") val price: String? = null,
        @SerializedName("discount") val discount: Int? = null,
        @SerializedName("priceWithDiscount") val priceWithDiscount: String? = null,
        @SerializedName("unit") val unit: String? = null,
    )

    data class FeedbackDto(
        @SerializedName("count") val count: Int? = null,
        @SerializedName("rating") val rating: Float? = null
    )

    data class InfoDto(
        @SerializedName("title") val title: String? = null,
        @SerializedName("value") val value: String? = null
    )

    companion object {
        fun ItemDto.asModel(): Item? {
            return Item(
                id = this.id ?: return null,
                title = this.title ?: "",
                subtitle = this.subtitle ?: "",
                price = this.price?.asModel(),
                feedback = this.feedback?.asModel(),
                tags = this.tags ?: emptyList(),
                available = this.available ?: 0,
                description = this.description ?: "",
                info = this.info?.map { it.asModel() } ?: emptyList(),
                ingredients = this.ingredients ?: ""
            )
        }

        private fun PriceDto.asModel(): Item.Price? {
            return Item.Price(
                price = this.price?.toIntOrNull() ?: return null,
                discount = this.discount ?: 0,
                priceWithDiscount = this.priceWithDiscount?.toIntOrNull() ?: 0,
                unit = this.unit ?: ""
            )
        }

        private fun FeedbackDto.asModel(): Item.Feedback? {
            return Item.Feedback(
                count = this.count ?: return null,
                rating = this.rating ?: return null
            )
        }

        private fun InfoDto.asModel(): Item.Info {
            return Item.Info(
                title = this.title ?: "",
                value = this.value ?: ""
            )
        }

        fun ItemDto.asEntity(): ItemEntity? {
            return ItemEntity(
                id = this.id ?: return null,
                title = this.title ?: "",
                subtitle = this.subtitle ?: "",
                price = this.price?.asEntity(),
                feedback = this.feedback?.asEntity(),
                tags = this.tags ?: emptyList(),
                available = this.available ?: 0,
                description = this.description ?: "",
                info = this.info?.map { it.asEntity() } ?: emptyList(),
                ingredients = this.ingredients ?: ""
            )
        }

        private fun PriceDto.asEntity(): ItemEntity.PriceEntity? {
            return ItemEntity.PriceEntity(
                price = this.price?.toIntOrNull() ?: return null,
                discount = this.discount ?: 0,
                priceWithDiscount = this.priceWithDiscount?.toIntOrNull() ?: 0,
                unit = this.unit ?: ""
            )
        }

        private fun FeedbackDto.asEntity(): ItemEntity.FeedbackEntity? {
            return ItemEntity.FeedbackEntity(
                count = this.count ?: return null,
                rating = this.rating ?: return null
            )
        }

        private fun InfoDto.asEntity(): ItemEntity.InfoEntity {
            return ItemEntity.InfoEntity(
                title = this.title ?: "",
                value = this.value ?: ""
            )
        }
    }
}