package com.bliaik.highlightwithrules

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

object JsonSyntaxHighlighter {
    val INDEX_1_KEY: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "JSON.INDEX_1",
        DefaultLanguageHighlighterColors.KEYWORD
    )

    val INDEX_2_KEY: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "JSON.INDEX_2",
        DefaultLanguageHighlighterColors.STRING
    )

    val INDEX_3_KEY: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
        "JSON.INDEX_3",
        DefaultLanguageHighlighterColors.NUMBER
    )
}