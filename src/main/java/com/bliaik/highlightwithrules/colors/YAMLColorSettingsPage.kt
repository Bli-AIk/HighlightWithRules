package com.bliaik.highlightwithrules.colors

import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import org.jetbrains.yaml.YAMLLanguage
import javax.swing.Icon

class YAMLColorSettingsPage : ColorSettingsPage {
    private val DESCRIPTORS = arrayOf(
        AttributesDescriptor("Index 1", ColorKeys.YAML_INDEX_1),
        AttributesDescriptor("Index 2", ColorKeys.YAML_INDEX_2),
        AttributesDescriptor("Index 3", ColorKeys.YAML_INDEX_3),
        AttributesDescriptor("Underline Index 1", ColorKeys.YAML_UNDERLINE_INDEX_1),
        AttributesDescriptor("Underline Index 2", ColorKeys.YAML_UNDERLINE_INDEX_2),
        AttributesDescriptor("Underline Index 3", ColorKeys.YAML_UNDERLINE_INDEX_3)
    )

    override fun getDisplayName(): String = "HWR YAML"
    override fun getIcon(): Icon? = null
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors() = com.intellij.openapi.options.colors.ColorDescriptor.EMPTY_ARRAY
    override fun getHighlighter(): SyntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(YAMLLanguage.INSTANCE, null, null)

    override fun getDemoText(): String {
        return """
text: "This is a Text"
config:
  - index: 1
    event: "SetSound_Default"
  - index: 2
    event: "SetAnimation_Idle"
  - index: 3
    event: "SetColor_Red"
  - index: 4
    event: "Summon_Object"
  - index: 5
    event: "SetPosition_Origin"
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
        val m = HashMap<String, TextAttributesKey>()
        m["index"] = ColorKeys.YAML_INDEX_1
        return m
    }
}