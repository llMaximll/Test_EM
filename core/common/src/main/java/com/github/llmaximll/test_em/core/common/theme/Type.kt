package com.github.llmaximll.test_em.core.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.github.llmaximll.test_em.core.common.R as ResCommon

val Typography = Typography()

object CustomTypography {
    val largeTitle1 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 20.sp
    )

    val title1 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 16.sp
    )

    val title2 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 14.sp
    )

    val title3 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 12.sp
    )

    val title4 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_regular)),
        fontSize = 14.sp
    )

    val text1 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_regular)),
        fontSize = 12.sp
    )

    val caption1 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_regular)),
        fontSize = 10.sp
    )

    val button1 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 12.sp
    )

    val button2 = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 14.sp
    )

    val element = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_regular)),
        fontSize = 9.sp
    )

    val price = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_medium)),
        fontSize = 24.sp
    )

    val placeholder = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_regular)),
        fontSize = 16.sp
    )

    val link = TextStyle(
        fontFamily = FontFamily(Font(ResCommon.font.sf_pro_display_regular)),
        fontSize = 10.sp
    )
}