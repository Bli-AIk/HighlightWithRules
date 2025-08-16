package com.bliaik.highlightwithrules.colors

import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.json.JsonLanguage
import javax.swing.Icon

class HWRColorSettingsPage : ColorSettingsPage {
    private val DESCRIPTORS = arrayOf(
        AttributesDescriptor("Index 1", ColorKeys.INDEX_1),
        AttributesDescriptor("Index 2", ColorKeys.INDEX_2),
        AttributesDescriptor("Index 3", ColorKeys.INDEX_3),
        AttributesDescriptor("Underline Index 1", ColorKeys.UNDERLINE_INDEX_1),
        AttributesDescriptor("Underline Index 2", ColorKeys.UNDERLINE_INDEX_2),
        AttributesDescriptor("Underline Index 3", ColorKeys.UNDERLINE_INDEX_3)
    )

    override fun getDisplayName(): String = "HWR JSON"
    override fun getIcon(): Icon? = null // 可选：你的文件类型图标
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors() = com.intellij.openapi.options.colors.ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter(): SyntaxHighlighter =SyntaxHighlighterFactory.getSyntaxHighlighter(JsonLanguage.INSTANCE, null, null) // 参见 SDK 文档示例

    override fun getDemoText(): String {
        // demoText 可以包含标签，通过 getAdditionalHighlightingTagToDescriptorMap 映射到 TextAttributesKey
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
        m["index"] = ColorKeys.INDEX_1
        return m
    }
}
