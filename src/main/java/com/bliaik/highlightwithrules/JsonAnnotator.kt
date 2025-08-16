package com.bliaik.highlightwithrules

import com.intellij.json.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors


class JsonAnnotator : Annotator {
    private val LOG = Logger.getInstance(JsonAnnotator::class.java)

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is JsonProperty && element.name == "index") {
            val indexValueElement = element.value
            if (indexValueElement is JsonNumberLiteral) {
                try {
                    val index = indexValueElement.text.toInt()
                    applyIndexLineHighlight(element, index, holder)
                } catch (e: NumberFormatException) {
                    LOG.warn("index not an integer: ${indexValueElement.text}")
                }
            }
            return
        }

        if (element is JsonStringLiteral) {
            val parentProp = element.parent as? JsonProperty
            if (parentProp?.name == "text" && parentProp.value == element) {
                applyUnderlinesForStringLiteral(element, holder)
            }
        }
    }

    private fun applyIndexLineHighlight(indexProperty: JsonProperty, index: Int, holder: AnnotationHolder) {
        val keyAttr = getColorKeyForIndex(index)
        val valueAttr = getColorKeyForIndex(index)

        indexProperty.value?.let { valueElement ->
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(valueElement.textRange)
                .textAttributes(valueAttr)
                .create()
        }

        indexProperty.nameElement.let { nameEl ->
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameEl.textRange)
                .textAttributes(keyAttr)
                .create()
        }

    }


    /**
     * Underline all indexes specified by simultaneous config inside the text string
     */
    private fun applyUnderlinesForStringLiteral(textValue: JsonStringLiteral, holder: AnnotationHolder) {
        val containingObject = (textValue.parent as? JsonProperty)?.parent as? JsonObject ?: return
        val configProp = containingObject.findProperty("config") ?: return
        val configArray = configProp.value as? JsonArray ?: return

        val rawText = textValue.text
        if (rawText.length < 2) return

        for (element in configArray.valueList) {
            val obj = element as? JsonObject ?: continue
            val idxProp = obj.findProperty("index") ?: continue
            val idxVal = idxProp.value as? JsonNumberLiteral ?: continue
            val indexInt = try {
                idxVal.text.toInt()
            } catch (e: NumberFormatException) {
                continue
            }

            val offsetInFile = mapDecodedIndexToOffset(textValue, indexInt)
            if (offsetInFile != null) {
                val underlineRange = TextRange(offsetInFile, offsetInFile + 1)
                val colorKey = getColorKeyForIndex(indexInt)

                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(underlineRange)
                    .textAttributes(colorKey)
                    .create()
            } else {
                LOG.debug("Could not map index $indexInt to source offset in string: ${textValue.text}")
            }
        }
    }

    /**
     * Map indexes based on "nth character of parsed string" (starting from 1)
     * to source code offset (returns absolute offset in file)
     * Simple processing of common escapes: \\, \", \n, \t, \r, \b, \f, \uXXXX
     */
    private fun mapDecodedIndexToOffset(textValue: JsonStringLiteral, charIndex: Int): Int? {
        val raw = textValue.text
        if (raw.length < 2) return null

        var decodedCount = 0
        var i = 1
        val last = raw.length - 1

        while (i < last) {
            val c = raw[i]
            if (c == '\\' && i + 1 < last) {
                val next = raw[i + 1]
                if (next == 'u' && i + 5 < last) {
                    // \uXXXX -> A total of 6 source characters, counted as 1 logical character
                    decodedCount++
                    if (decodedCount == charIndex) {
                        return textValue.textOffset + i
                    }
                    i += 6
                    continue
                } else {
                    // Ordinary escapes, such as \n, \", \\, etc.
                    // The source code occupies 2 characters, which is counted as 1 logical character.
                    decodedCount++
                    if (decodedCount == charIndex) {
                        return textValue.textOffset + i
                    }
                    i += 2
                    continue
                }
            } else {
                // Regular characters
                decodedCount++
                if (decodedCount == charIndex) {
                    return textValue.textOffset + i
                }
                i++
            }
        }

        // Out of range
        return null
    }

    private fun getColorKeyForIndex(index: Int): TextAttributesKey {
        return TextAttributesKey.createTextAttributesKey(
            "JSON.DEMO",
            DefaultLanguageHighlighterColors.KEYWORD
        )
    }
}
