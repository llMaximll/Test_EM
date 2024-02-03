package com.github.llmaximll.test_em.core.common

import androidx.annotation.StringRes
import com.github.llmaximll.test_em.core.common.models.Item

enum class Sort(
    @StringRes val titleRes: Int
) {
    Standard(
        titleRes = R.string.core_common_sort_standard
    ),
    Popular(
        titleRes = R.string.core_common_sort_popular
    ),
    ReducingPrice(
        titleRes = R.string.core_common_sort_reducing_price
    ),
    AscendingPrice(
        titleRes = R.string.core_common_sort_ascending_price
    );
}