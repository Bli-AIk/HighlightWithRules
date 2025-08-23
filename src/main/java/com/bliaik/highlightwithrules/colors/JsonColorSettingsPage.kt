package com.bliaik.highlightwithrules.colors

import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.json.JsonLanguage
import javax.swing.Icon

class JsonColorSettingsPage : ColorSettingsPage {
    private val DESCRIPTORS = arrayOf(
        AttributesDescriptor("Index 1", ColorKeys.JSON_INDEX_1),
        AttributesDescriptor("Index 2", ColorKeys.JSON_INDEX_2),
        AttributesDescriptor("Index 3", ColorKeys.JSON_INDEX_3),
        AttributesDescriptor("Underline Index 1", ColorKeys.JSON_UNDERLINE_INDEX_1),
        AttributesDescriptor("Underline Index 2", ColorKeys.JSON_UNDERLINE_INDEX_2),
        AttributesDescriptor("Underline Index 3", ColorKeys.JSON_UNDERLINE_INDEX_3)
    )

    override fun getDisplayName(): String = "HWR JSON"
    override fun getIcon(): Icon? = null
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors() = com.intellij.openapi.options.colors.ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter(): SyntaxHighlighter =SyntaxHighlighterFactory.getSyntaxHighlighter(JsonLanguage.INSTANCE, null, null) // 参见 SDK 文档示例

    override fun getDemoText(): String {
        return """
  {
    "text": "This is a Text",
    "config": [
      {
        "index": 1,
        "event": "SetSound_Default"
      },
      {
        "index": 2,
        "event": "SetAnimation_Idle"
      },
      {
        "index": 3,
        "event": "SetColor_Red"
      },
      {
        "index": 4,
        "event": "Summon_Object"
      },
      {
        "index": 5,
        "event": "SetPosition_Origin"
      }
    ]
  }
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
        val m = HashMap<String, TextAttributesKey>()
        m["index"] = ColorKeys.JSON_INDEX_1
        return m
    }
}
