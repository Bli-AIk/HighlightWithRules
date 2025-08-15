package com.bliaik.highlightwithrules

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class JsonFileType private constructor() : LanguageFileType(JsonLanguage.INSTANCE) {

    companion object {
        val INSTANCE: JsonFileType = JsonFileType()
    }

    override fun getName(): String = "JSON File"

    override fun getDescription(): String = "JSON language file"

    override fun getDefaultExtension(): String = "json"

    override fun getIcon(): Icon? = null
}
