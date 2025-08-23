package com.bliaik.highlightwithrules

import com.bliaik.highlightwithrules.colors.ColorKeys
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.*

class YAMLAnnotator : Annotator {
    private val LOG = Logger.getInstance(YAMLAnnotator::class.java)

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is YAMLKeyValue && element.keyText == "index") {
            val indexValueElement = element.value
            if (indexValueElement is YAMLScalar) {
                try {
                    val index = indexValueElement.text.toInt()
                    applyIndexLineHighlight(element, index, holder)
                } catch (e: NumberFormatException) {
                    LOG.warn("index not an integer: ${indexValueElement.text}")
                }
            }
            return
        }

        if (element is YAMLScalar) {
            val parentProp = element.parent as? YAMLKeyValue
            if (parentProp?.keyText == "text" && parentProp.value == element) {
                applyUnderlinesForStringLiteral(element, holder)
            }
        }
    }

    private fun applyIndexLineHighlight(indexProperty: YAMLKeyValue, index: Int, holder: AnnotationHolder) {
        val keyAttr = getColorKeyForIndex(index, false)
        val valueAttr = getColorKeyForIndex(index, false)

        indexProperty.value?.let { valueElement ->
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(valueElement.textRange)
                .textAttributes(valueAttr)
                .create()
        }

        indexProperty.key?.let { nameEl ->
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(nameEl.textRange)
                .textAttributes(keyAttr)
                .create()
        }
    }

    private fun applyUnderlinesForStringLiteral(textValue: YAMLScalar, holder: AnnotationHolder) {
        val containingObject = (textValue.parent as? YAMLKeyValue)?.parent as? YAMLMapping ?: return
        val configProp = containingObject.getKeyValueByKey("config") ?: return
        val configArray = configProp.value as? YAMLSequence ?: return

        val rawText = textValue.text
        if (rawText.isEmpty()) return

        for (element in configArray.items) {
            val obj = element.value as? YAMLMapping ?: continue
            val idxProp = obj.getKeyValueByKey("index") ?: continue
            val idxVal = idxProp.value as? YAMLScalar ?: continue
            val indexInt = try {
                idxVal.text.toInt()
            } catch (e: NumberFormatException) {
                continue
            }

            val offsetInFile = mapDecodedIndexToOffset(textValue, indexInt)
            if (offsetInFile != null) {
                val underlineRange = TextRange(offsetInFile, offsetInFile + 1)
                val colorKey = getColorKeyForIndex(indexInt, true)

                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(underlineRange)
                    .textAttributes(colorKey)
                    .create()
            } else {
                LOG.debug("Could not map index $indexInt to source offset in string: ${textValue.text}")
            }
        }
    }

    private fun mapDecodedIndexToOffset(textValue: YAMLScalar, charIndex: Int): Int? {
        val raw = textValue.text
        if (raw.isEmpty()) return null

        // Detect quoting style
        val isQuoted = raw.first() == '"' || raw.first() == '\''
        val isDoubleQuoted = raw.first() == '"'

        // Start and end indices inside the raw text to examine
        var i = if (isQuoted) 1 else 0
        val last = if (isQuoted) raw.length - 1 else raw.length

        var decodedCount = 0

        while (i < last) {
            val c = raw[i]
            if (isDoubleQuoted && c == '\\' && i + 1 < last) {
                // handle \uXXXX or simple escapes like \n, \t, \\
                val next = raw[i + 1]
                if (next == 'u' && i + 5 < last) {
                    // \uXXXX -> counts as one decoded character
                    decodedCount++
                    if (decodedCount - 1 == charIndex) {
                        return textValue.textOffset + i
                    }
                    i += 6
                    continue
                } else {
                    // other \x escapes -> counts as one decoded character
                    decodedCount++
                    if (decodedCount - 1 == charIndex) {
                        return textValue.textOffset + i
                    }
                    i += 2
                    continue
                }
            } else {
                // plain character (for plain scalar or single-quoted or non-escape char in double-quoted)
                decodedCount++
                if (decodedCount - 1 == charIndex) {
                    return textValue.textOffset + i
                }
                i++
            }
        }

        return null
    }

    private fun getColorKeyForIndex(index: Int, underline: Boolean): TextAttributesKey {
        val cyclicalIndex = index % 3 + 1
        return if (!underline) {
            when (cyclicalIndex) {
                1 -> ColorKeys.YAML_INDEX_1
                2 -> ColorKeys.YAML_INDEX_2
                3 -> ColorKeys.YAML_INDEX_3
                else -> ColorKeys.YAML_INDEX
            }
        } else {
            when (cyclicalIndex) {
                1 -> ColorKeys.YAML_UNDERLINE_INDEX_1
                2 -> ColorKeys.YAML_UNDERLINE_INDEX_2
                3 -> ColorKeys.YAML_UNDERLINE_INDEX_3
                else -> ColorKeys.YAML_INDEX
            }
        }
    }
}
