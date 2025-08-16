package com.bliaik.highlightwithrules.colors

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object ColorKeys {
    val INDEX = TextAttributesKey.createTextAttributesKey(
        "HWR.JSON.INDEX",
        DefaultLanguageHighlighterColors.NUMBER
    )

    // 如果你想让不同 index 用不同颜色（例如 index1,index2...），就创建多个 key：
    val INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_1", INDEX)
    val INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_2", INDEX)
    val INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_3", INDEX)
    val UNDERLINEINDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINEINDEX_1", INDEX_1)
    val UNDERLINEINDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINEINDEX_2", INDEX_2)
    val UNDERLINEINDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINEINDEX_3", INDEX_3)
}