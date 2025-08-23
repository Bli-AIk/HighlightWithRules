package com.bliaik.highlightwithrules.colors

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object ColorKeys {
    val INDEX = TextAttributesKey.createTextAttributesKey(
        "HWR.JSON.INDEX",
        DefaultLanguageHighlighterColors.NUMBER
    )

    val JSON_INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_1", DefaultLanguageHighlighterColors.KEYWORD)
    val JSON_INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_2",  DefaultLanguageHighlighterColors.NUMBER)
    val JSON_INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.JSON.INDEX_3", DefaultLanguageHighlighterColors.LABEL)
    val JSON_UNDERLINE_INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINE_INDEX_1", JSON_INDEX_1)
    val JSON_UNDERLINE_INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINE_INDEX_2", JSON_INDEX_2)
    val JSON_UNDERLINE_INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.JSON.UNDERLINE_INDEX_3", JSON_INDEX_3)

    val YAML_INDEX = TextAttributesKey.createTextAttributesKey(
        "HWR.YAML.INDEX",
        DefaultLanguageHighlighterColors.NUMBER
    )

    val YAML_INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.YAML.INDEX_1", JSON_INDEX_1)
    val YAML_INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.YAML.INDEX_2", JSON_INDEX_2)
    val YAML_INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.YAML.INDEX_3", JSON_INDEX_3)
    val YAML_UNDERLINE_INDEX_1 = TextAttributesKey.createTextAttributesKey("HWR.YAML.UNDERLINE_INDEX_1", JSON_UNDERLINE_INDEX_1)
    val YAML_UNDERLINE_INDEX_2 = TextAttributesKey.createTextAttributesKey("HWR.YAML.UNDERLINE_INDEX_2", JSON_UNDERLINE_INDEX_2)
    val YAML_UNDERLINE_INDEX_3 = TextAttributesKey.createTextAttributesKey("HWR.YAML.UNDERLINE_INDEX_3", JSON_UNDERLINE_INDEX_3)
}
