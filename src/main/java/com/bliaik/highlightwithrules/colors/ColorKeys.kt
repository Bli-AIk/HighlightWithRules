package com.bliaik.highlightwithrules.colors

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object ColorKeys {
    val INDEX = TextAttributesKey.createTextAttributesKey(
        "HWR.JSON.INDEX",
        DefaultLanguageHighlighterColors.NUMBER
    )

    val INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_1", INDEX)
    val INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_2", INDEX)
    val INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_3", INDEX)
    val UNDERLINE_INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINE_INDEX_1", INDEX_1)
    val UNDERLINE_INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINE_INDEX_2", INDEX_2)
    val UNDERLINE_INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINE_INDEX_3", INDEX_3)
}