package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.openapi.util.IconLoader
import com.intellij.ui.scale.JBUIScale
import javax.swing.Icon

object PolishFlagIconProvider {
    val SMILE_EMOJI = runCatching { IconLoader.getIcon("/icons/smile.png", javaClass) }
        .getOrElse { SmileEmojiIcon(JBUIScale.scale(16)) }
}
