package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.components.Service
import javax.swing.UIManager

/**
 * Provider for the Polish progress bar.
 * Registers the custom progress bar UI with the UIManager.
 */
@Service
class PolishProgressIndicatorProvider : LafManagerListener {

    init {
        updateProgressBarUi()
    }

    override fun lookAndFeelChanged(source: com.intellij.ide.ui.LafManager) {
        updateProgressBarUi()
    }

    private fun updateProgressBarUi() {
        UIManager.put("ProgressBarUI", PolishProgressBar.UICreator::class.java.name)
        UIManager.getDefaults()[PolishProgressBar.UICreator::class.java.name] = PolishProgressBar.UICreator::class.java
    }
}
