package com.bliaik.highlightwithrules

import com.intellij.json.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class JsonAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is JsonProperty) return

        if (element.name == "index") {
            val indexValueElement = element.value
            if (indexValueElement is JsonNumberLiteral) {
                try {
                    val index = indexValueElement.text.toInt()

                    applyIndexLineHighlight(element, index, holder)
                    applyTextUnderline(element, index, holder)

                } catch (e: NumberFormatException) {
                }
            }
        }
    }

    /**
     * Apply highlighting for the entire line containing "index"
     */
    private fun applyIndexLineHighlight(indexProperty: JsonProperty, index: Int, holder: AnnotationHolder) {
        val key = getColorKeyForIndex(index) ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(indexProperty.textRange)
            .textAttributes(key)
            .create()
    }

    /**
     * Add an underscore to the corresponding characters in the "text" string
     */
    private fun applyTextUnderline(indexProperty: JsonProperty, index: Int, holder: AnnotationHolder) {
        val parentObject = PsiTreeUtil.getParentOfType(indexProperty, JsonObject::class.java) ?: return
        val topLevelObject = PsiTreeUtil.getParentOfType(parentObject, JsonObject::class.java) ?: return
        val textProperty = topLevelObject.findProperty("text") ?: return
        val textValue = textProperty.value

        if (textValue is JsonStringLiteral) {
            val textContent = textValue.text
            val textContentLength = textContent.length - 2

            if (index in 1..textContentLength) {
                val underlineStartOffset = textValue.textOffset + 1 + (index - 1)
                val underlineRange = TextRange(underlineStartOffset, underlineStartOffset + 1)

                val colorKey = getColorKeyForIndex(index) ?: return

                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(underlineRange)
                    .textAttributes(colorKey)
                    .create()
            }
        }
    }

    /**
     * Return the corresponding TextAttributesKey according to index
     */
    private fun getColorKeyForIndex(index: Int): TextAttributesKey? {
        return when (index) {
            1 -> JsonSyntaxHighlighter.INDEX_1_KEY
            2 -> JsonSyntaxHighlighter.INDEX_2_KEY
            3 -> JsonSyntaxHighlighter.INDEX_3_KEY
            else -> null
        }
    }
}