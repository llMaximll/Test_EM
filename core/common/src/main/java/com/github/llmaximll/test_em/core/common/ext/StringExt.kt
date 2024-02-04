package com.github.llmaximll.test_em.core.common.ext

import android.telephony.PhoneNumberUtils
import java.net.URLDecoder
import java.net.URLEncoder

fun String.asUrlEncoded(): String? = try {
    URLEncoder.encode(this, Charsets.UTF_8.name())
} catch (e: Exception) {
    null
}

fun String.asUrlDecoded(): String? = try {
    URLDecoder.decode(this, Charsets.UTF_8.name())
} catch (e: Exception) {
    null
}

fun String.asFormattedPhoneNumber(): String {
    val formattedNumber = PhoneNumberUtils.formatNumber(this, "RU")
    return "+7 $formattedNumber"
}