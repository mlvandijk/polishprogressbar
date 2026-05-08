package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.application.ApplicationManager
import javax.swing.UIManager

/**
 * Provider for the Polish flag progress bar.
 * Registers the custom progress bar UI with the UIManager.
 */
class PolishFlagProgressIndicatorProvider : LafManagerListener {

    init {
        updateProgressBarUi()
    }

    override fun lookAndFeelChanged(source: com.intellij.ide.ui.LafManager) {
        updateProgressBarUi()
    }

    private fun updateProgressBarUi() {
        UIManager.put("ProgressBarUI", PolishFlagProgressBar.UICreator::class.java.name)
        UIManager.getDefaults().put(PolishFlagProgressBar.UICreator::class.java.name, PolishFlagProgressBar.UICreator::class.java)
    }

    companion object {
        fun getInstance(): PolishFlagProgressIndicatorProvider = ApplicationManager.getApplication().getService(PolishFlagProgressIndicatorProvider::class.java)
    }
}
