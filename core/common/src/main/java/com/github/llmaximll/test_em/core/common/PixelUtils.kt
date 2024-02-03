package com.github.llmaximll.test_em.core.common

import android.content.Context

fun Float.ptToPx(context: Context): Float {
    return this * (1.0f / 0.75f)
}