package com.bliaik.highlightwithrules

import com.intellij.lang.Language
import com.intellij.json.JsonLanguage as IJJsonLanguage

class JsonLanguage() : Language(IJJsonLanguage.INSTANCE.toString()) {

    companion object {
        val INSTANCE: JsonLanguage = JsonLanguage()
    }
}
