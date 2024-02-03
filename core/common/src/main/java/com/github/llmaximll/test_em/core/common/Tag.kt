package com.github.llmaximll.test_em.core.common

import androidx.annotation.StringRes

enum class Tag(
    @StringRes val titleRes: Int,
    val tag: String?
) {
    All(
        titleRes = R.string.core_common_filter_all,
        tag = null
    ),
    Face(
        titleRes = R.string.core_common_filter_face,
        tag = "face"
    ),
    Body(
        titleRes = R.string.core_common_filter_body,
        tag = "body"
    ),
    Suntan(
        titleRes = R.string.core_common_filter_suntan,
        tag = "suntan"
    ),
    Mask(
        titleRes = R.string.core_common_filter_mask,
        tag = "mask"
    ),
}