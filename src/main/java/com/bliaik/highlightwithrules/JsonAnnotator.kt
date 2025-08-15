package com.bliaik.highlightwithrules

import com.intellij.json.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class JsonAnnotator : Annotator {
    private val LOG = Logger.getInstance(JsonAnnotator::class.java)

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        // 处理 index 属性：只在 index 自身范围做注解（不会越界）
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

        // 处理字符串文字：如果这是 "text" 的值，则查找同级 config 中的 index 并在该字符串内做下划线
        if (element is JsonStringLiteral) {
            // 确保它是名为 "text" 的 property 的值
            val parentProp = element.parent as? JsonProperty
            if (parentProp?.name == "text") {
                applyUnderlinesForStringLiteral(element, holder)
            }
        }
    }

    private fun applyIndexLineHighlight(indexProperty: JsonProperty, index: Int, holder: AnnotationHolder) {
        val key = getColorKeyForIndex(index) ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(indexProperty.textRange)
            .textAttributes(key)
            .create()
    }

    /**
     * 在 text 字符串内部为所有由同级 config 指定的 index 加下划线
     */
    private fun applyUnderlinesForStringLiteral(textValue: JsonStringLiteral, holder: AnnotationHolder) {
        // 找到包含 text property 的对象（例如 { "text": "...", "config": [ {...}, {...} ] }）
        val containingObject = (textValue.parent as? JsonProperty)?.parent as? JsonObject ?: return

        // 找到同级的 config 数组
        val configProp = containingObject.findProperty("config") ?: return
        val configArray = configProp.value as? JsonArray ?: return

        val rawText = textValue.text
        if (rawText.length < 2) return

        // 遍历 config 数组里的对象，取出 index 并标注
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
                val colorKey = getColorKeyForIndex(indexInt) ?: continue

                // 这里我们正在 annotate 的 element 是 textValue（字符串文字），范围属于它，所以合法
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
     * 将基于“解析后字符串的第 n 个字符”（从 1 开始）的索引映射到源码偏移（返回在文件中的绝对偏移）
     * 简单处理常见转义：\\, \", \n, \t, \r, \b, \f, \uXXXX
     */
    private fun mapDecodedIndexToOffset(textValue: JsonStringLiteral, charIndex: Int): Int? {
        val raw = textValue.text // 包含引号，如: "\"Hello\\nworld\""
        if (raw.length < 2) return null

        var decodedCount = 0
        var i = 1 // 从第一个引号后的字符开始（raw 的索引）
        val last = raw.length - 1

        while (i < last) {
            val c = raw[i]
            if (c == '\\' && i + 1 < last) {
                val next = raw[i + 1]
                if (next == 'u' && i + 5 < last) {
                    // \uXXXX -> 共 6 个源码字符，算作 1 个逻辑字符
                    decodedCount++
                    if (decodedCount == charIndex) {
                        return textValue.textOffset + i
                    }
                    i += 6
                    continue
                } else {
                    // 普通转义，如 \n, \", \\ 等，源码占 2 个字符，算作 1 个逻辑字符
                    decodedCount++
                    if (decodedCount == charIndex) {
                        return textValue.textOffset + i
                    }
                    i += 2
                    continue
                }
            } else {
                // 常规字符
                decodedCount++
                if (decodedCount == charIndex) {
                    return textValue.textOffset + i
                }
                i++
            }
        }

        // 超出范围
        return null
    }

    private fun getColorKeyForIndex(index: Int): TextAttributesKey? {
        return when (index) {
            1 -> JsonSyntaxHighlighter.INDEX_1_KEY
            2 -> JsonSyntaxHighlighter.INDEX_2_KEY
            3 -> JsonSyntaxHighlighter.INDEX_3_KEY
            else -> null
        }
    }
}
