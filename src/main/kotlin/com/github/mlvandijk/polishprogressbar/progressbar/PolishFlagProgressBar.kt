package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.openapi.ui.GraphicsConfig
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.UIUtil
import java.awt.*
import java.awt.geom.RoundRectangle2D
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicProgressBarUI
import kotlin.math.roundToInt

class PolishFlagProgressBar : BasicProgressBarUI() {

    companion object {
        private val WHITE = JBColor(Gray._255, Gray._255)
        private val RED = JBColor(Color(220, 20, 60), Color(220, 20, 60))

        private const val BAR_HEIGHT = 20
        private const val CORNER_RADIUS = 8f
        private const val ICON_PADDING = 2
        private const val ICON_SPEED = 0.2f
    }

    class UICreator {
        companion object {
            @JvmStatic
            fun createUI(c: JComponent): ComponentUI = PolishFlagProgressBar()
        }
    }

    private var iconPosition = 0f
    private var iconDirection = 1f
    private var lastTimeMillis = 0L

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int = availableLength / 2

    override fun paintIndeterminate(g: Graphics, c: JComponent) {
        val g2 = g as? Graphics2D ?: return
        val barWidth = progressBar.width
        val barHeight = progressBar.preferredSize.height

        if (barWidth <= 0 || barHeight <= 0) return

        val config = GraphicsConfig(g2)
        val progressRect = roundRect(0f, 0f, barWidth.toFloat(), barHeight.toFloat())
        val iconSize = barHeight - JBUIScale.scale(ICON_PADDING)
        val maxIconX = (barWidth - iconSize).coerceAtLeast(0)

        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.color = UIUtil.getPanelBackground()
            g2.fill(progressRect)

            g2.withClip(progressRect) {
                paintFlag(g2, barWidth, barHeight)
            }

            updateIconPosition(maxIconX.toFloat())
            paintIcon(
                g = g2,
                icon = PolishFlagIconProvider.SMILE_EMOJI,
                x = iconPosition.roundToInt().coerceIn(0, maxIconX),
                y = (barHeight - iconSize) / 2,
                maxSize = iconSize,
            )

            g2.color = JBColor.GRAY
            g2.draw(progressRect)
        } finally {
            config.restore()
        }

        if (progressBar.isDisplayable) {
            progressBar.repaint()
        }
    }

    private fun updateIconPosition(maxPosition: Float) {
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedSeconds = if (lastTimeMillis == 0L) 0f else (currentTimeMillis - lastTimeMillis) / 1_000f
        lastTimeMillis = currentTimeMillis

        if (maxPosition <= 0f) {
            iconPosition = 0f
            iconDirection = 1f
            return
        }

        iconPosition += iconDirection * ICON_SPEED * elapsedSeconds
        if (iconPosition !in 0f..maxPosition) {
            iconPosition = iconPosition.coerceIn(0f, maxPosition)
            iconDirection *= -1f
        }
    }

    override fun getPreferredSize(c: JComponent): Dimension =
        Dimension(super.getPreferredSize(c).width, JBUIScale.scale(BAR_HEIGHT))

    override fun paintDeterminate(g: Graphics, c: JComponent) {
        val g2 = g as? Graphics2D ?: return

        if (progressBar.orientation != SwingConstants.HORIZONTAL || !c.componentOrientation.isLeftToRight) {
            super.paintDeterminate(g, c)
            return
        }

        val b = progressBar.insets
        val w = progressBar.width
        val h = progressBar.preferredSize.height

        val barRectWidth = w - (b.right + b.left)
        val barRectHeight = h - (b.top + b.bottom)

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }

        val amountFull = getAmountFull(b, barRectWidth, barRectHeight)
        val background = c.parent?.background ?: UIUtil.getPanelBackground()
        val config = GraphicsConfig(g2)
        val transform = g2.transform

        try {
            g2.color = background
            if (c.isOpaque) {
                g2.fillRect(0, 0, w, h)
            }

            g2.translate(0, (c.height - h) / 2)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

            val border = JBUIScale.scale(1f)
            g2.color = progressBar.foreground
            g2.fill(roundRect(0f, 0f, w - border, h - border, JBUIScale.scale(9f)))
            g2.color = background
            g2.fill(roundRect(border, border, w - 3f * border, h - 3f * border))

            if (amountFull > 0) {
                val progressRect = roundRect(
                    2f * border,
                    2f * border,
                    amountFull - JBUIScale.scale(5f),
                    h - JBUIScale.scale(5f),
                    JBUIScale.scale(7f),
                )
                g2.withClip(progressRect) {
                    paintFlag(g2, w, h)
                }

                val iconSize = h - JBUIScale.scale(ICON_PADDING)
                paintIcon(
                    g = g2,
                    icon = PolishFlagIconProvider.SMILE_EMOJI,
                    x = (amountFull - iconSize).coerceIn(0, (w - iconSize).coerceAtLeast(0)),
                    y = (h - iconSize) / 2,
                    maxSize = iconSize,
                )
            }
        } finally {
            g2.transform = transform
            config.restore()
        }

        if (progressBar.isStringPainted) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b)
        }
    }

    private fun paintFlag(g: Graphics2D, width: Int, height: Int) {
        val halfHeight = height / 2

        g.color = WHITE
        g.fillRect(0, 0, width, halfHeight)

        g.color = RED
        g.fillRect(0, halfHeight, width, height - halfHeight)
    }

    private fun paintIcon(g: Graphics2D, icon: Icon, x: Int, y: Int, maxSize: Int) {
        if (maxSize <= 0 || icon.iconWidth <= 0 || icon.iconHeight <= 0) {
            return
        }

        val scale = minOf(1.0, maxSize.toDouble() / icon.iconWidth, maxSize.toDouble() / icon.iconHeight)
        val width = (icon.iconWidth * scale).roundToInt()
        val height = (icon.iconHeight * scale).roundToInt()
        val iconGraphics = g.create() as Graphics2D

        try {
            iconGraphics.translate(x + (maxSize - width) / 2, y + (maxSize - height) / 2)
            iconGraphics.scale(scale, scale)
            icon.paintIcon(progressBar, iconGraphics, 0, 0)
        } finally {
            iconGraphics.dispose()
        }
    }

    private inline fun Graphics2D.withClip(clip: Shape, paint: () -> Unit) {
        val originalClip = this.clip
        try {
            this.clip = clip
            paint()
        } finally {
            this.clip = originalClip
        }
    }

    private fun roundRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radius: Float = JBUIScale.scale(CORNER_RADIUS),
    ) =
        RoundRectangle2D.Float(x, y, width.coerceAtLeast(0f), height.coerceAtLeast(0f), radius, radius)
}
