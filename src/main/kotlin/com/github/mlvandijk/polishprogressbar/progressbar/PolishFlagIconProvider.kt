package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.openapi.util.IconLoader
import com.intellij.ui.scale.JBUIScale
import javax.swing.Icon

object PolishFlagIconProvider {
    val SMILE_EMOJI: Icon = try {
        IconLoader.getIcon("/icons/smile.png", PolishFlagIconProvider::class.java)
    } catch (e: Exception) {
        // Fallback to programmatically created icon
        SmileEmojiIcon(JBUIScale.scale(16))
    }
}
