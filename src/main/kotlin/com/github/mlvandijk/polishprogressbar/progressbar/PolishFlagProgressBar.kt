package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.openapi.ui.GraphicsConfig
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.UIUtil
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.ComponentUI
import javax.swing.plaf.basic.BasicGraphicsUtils
import javax.swing.plaf.basic.BasicProgressBarUI

/**
 * Polish flag progress bar with a neutral smile emoji.
 * The progress bar shows the Polish flag colors (white and red) and a neutral smile emoji.
 */
class PolishFlagProgressBar : BasicProgressBarUI() {

    companion object {
        // Polish flag colors
        private val WHITE = JBColor(Color(255, 255, 255), Color(255, 255, 255))
        private val RED = JBColor(Color(220, 20, 60), Color(220, 20, 60))

        // Dimensions
        private const val ANIMATION_DELAY = 10 // Reduced for smoother animation
        private const val ANIMATION_SPEED = 0.2f // 5 times slower (1/5 = 0.2)
        private const val CYCLE_TIME_MS = 5000 // 5 times longer cycle
    }

    class UICreator {
        companion object {
            @JvmStatic
            fun createUI(c: JComponent): ComponentUI {
                return PolishFlagProgressBar()
            }
        }
    }

    private var position = 0f // Using float for smoother animation
    private var velocity = 1f
    private var lastTimeMillis = 0L

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int {
        return availableLength / 2
    }

    override fun paintIndeterminate(g: Graphics, c: JComponent) {
        if (!(g is Graphics2D)) {
            return
        }

        val g2d = g as Graphics2D

        // Setup
        val config = GraphicsConfig(g2d)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Calculate dimensions
        val barWidth = progressBar.width
        val barHeight = progressBar.preferredSize.height

        // Draw background
        g2d.color = UIUtil.getPanelBackground()
        val rect = RoundRectangle2D.Float(0f, 0f, barWidth.toFloat(), barHeight.toFloat(), 
                                          JBUIScale.scale(8f), JBUIScale.scale(8f))
        g2d.fill(rect)

        // Calculate animation position
        updatePosition()

        // Create a rounded rectangle for the progress bar area
        val progressRect = RoundRectangle2D.Float(
            0f, 0f, 
            barWidth.toFloat(), barHeight.toFloat(), 
            JBUIScale.scale(8f), JBUIScale.scale(8f)
        )

        // Save the original clip
        val originalClip = g2d.clip

        // Set clip to the progress rectangle
        g2d.clip = progressRect

        // Draw the white top half - spans the full width
        g2d.color = WHITE
        g2d.fillRect(0, 0, barWidth, barHeight / 2)

        // Draw the red bottom half - spans the full width
        g2d.color = RED
        g2d.fillRect(0, barHeight / 2, barWidth, barHeight / 2)

        // Restore original clip
        g2d.clip = originalClip

        // Draw smile emoji
        val emojiSize = barHeight - JBUIScale.scale(2)
        val emojiX = position.toInt() % barWidth
        val emojiY = (barHeight - emojiSize) / 2

        val icon = PolishFlagIconProvider.SMILE_EMOJI
        icon.paintIcon(progressBar, g2d, emojiX, emojiY)

        // Draw a border around the progress bar
        g2d.color = JBColor.GRAY
        g2d.draw(progressRect)

        config.restore()

        // Trigger repaint for animation
        if (progressBar.isDisplayable) {
            progressBar.repaint()
        }
    }

    private fun updatePosition() {
        val currentTimeMillis = System.currentTimeMillis()

        if (lastTimeMillis == 0L) {
            lastTimeMillis = currentTimeMillis
            return
        }

        // Calculate exact time delta for continuous animation
        val timeDelta = currentTimeMillis - lastTimeMillis
        lastTimeMillis = currentTimeMillis

        // Always update position for every frame, making animation continuous
        // Apply ANIMATION_SPEED to slow down by factor of 5
        val movement = velocity * timeDelta * ANIMATION_SPEED / 1000.0f
        position += movement // Use float for smooth movement

        // Ensure position stays within bounds and create a smooth loop
        if (position < 0f) {
            position = 0f
            velocity = 1f
        } else if (position > progressBar.width.toFloat()) {
            position = progressBar.width.toFloat()
            velocity = -1f
        }
    }

    override fun getPreferredSize(c: JComponent): Dimension {
        return Dimension(super.getPreferredSize(c).width, JBUIScale.scale(20))
    }

    override fun paintDeterminate(g: Graphics, c: JComponent) {
        if (!(g is Graphics2D)) {
            return
        }

        if (progressBar.orientation != SwingConstants.HORIZONTAL || !c.componentOrientation.isLeftToRight) {
            super.paintDeterminate(g, c)
            return
        }

        val config = GraphicsConfig(g)
        val b = progressBar.insets // area for border
        val w = progressBar.width
        val h = progressBar.preferredSize.height

        val barRectWidth = w - (b.right + b.left)
        val barRectHeight = h - (b.top + b.bottom)

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }

        val amountFull = getAmountFull(b, barRectWidth, barRectHeight)

        val parent = c.parent
        val background = parent?.background ?: UIUtil.getPanelBackground()

        g.color = background
        val g2 = g as Graphics2D
        if (c.isOpaque) {
            g.fillRect(0, 0, w, h)
        }

        val R = JBUIScale.scale(8f)
        val R2 = JBUIScale.scale(9f)
        val off = JBUIScale.scale(1f)

        g2.translate(0, (c.height - h) / 2)
        g2.color = progressBar.foreground
        g2.fill(RoundRectangle2D.Float(0f, 0f, w - off, h - off, R2, R2))
        g2.color = background
        g2.fill(RoundRectangle2D.Float(off, off, w - 2f * off - off, h - 2f * off - off, R, R))

        // Use Polish flag colors instead of rainbow
        if (amountFull > 0) {
            // Create a rounded rectangle for the progress area
            val progressRect = RoundRectangle2D.Float(
                2f * off, 2f * off, 
                amountFull - JBUIScale.scale(5f), h - JBUIScale.scale(5f), 
                JBUIScale.scale(7f), JBUIScale.scale(7f)
            )

            // Save the original clip
            val originalClip = g2.clip

            // Set clip to the progress rectangle
            g2.clip = progressRect

            // Draw the white top half - spans the full width
            g2.color = WHITE
            g2.fillRect(0, 0, w, h / 2)

            // Draw the red bottom half - spans the full width
            g2.color = RED
            g2.fillRect(0, h / 2, w, h / 2)

            // Restore original clip
            g2.clip = originalClip

            // Draw the smile emoji at the end of the progress
            val icon = PolishFlagIconProvider.SMILE_EMOJI
            val emojiSize = icon.iconHeight
            val emojiY = (h - emojiSize) / 2
            icon.paintIcon(progressBar, g2, amountFull - JBUIScale.scale(10), emojiY)
        }

        g2.translate(0, -(c.height - h) / 2)

        // Deal with possible text painting
        if (progressBar.isStringPainted) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b)
        }

        config.restore()
    }

    private fun paintString(g: Graphics, x: Int, y: Int, w: Int, h: Int, fillStart: Int, amountFull: Int) {
        if (!(g is Graphics2D)) {
            return
        }

        val g2 = g as Graphics2D
        val progressString = progressBar.string
        g2.font = progressBar.font
        val renderLocation = getStringPlacement(g2, progressString, x, y, w, h)
        val oldClip = g2.clipBounds

        if (progressBar.orientation == SwingConstants.HORIZONTAL) {
            g2.color = selectionBackground
            BasicGraphicsUtils.drawString(progressBar, g2, progressString, renderLocation.x.toFloat(), renderLocation.y.toFloat())
            g2.color = selectionForeground
            g2.clipRect(fillStart, y, amountFull, h)
            BasicGraphicsUtils.drawString(progressBar, g2, progressString, renderLocation.x.toFloat(), renderLocation.y.toFloat())
        } else { // VERTICAL
            g2.color = selectionBackground
            val rotate = AffineTransform.getRotateInstance(Math.PI / 2)
            g2.font = progressBar.font.deriveFont(rotate)
            val verticalRenderLocation = getStringPlacement(g2, progressString, x, y, w, h)
            BasicGraphicsUtils.drawString(progressBar, g2, progressString, verticalRenderLocation.x.toFloat(), verticalRenderLocation.y.toFloat())
            g2.color = selectionForeground
            g2.clipRect(x, fillStart, w, amountFull)
            BasicGraphicsUtils.drawString(progressBar, g2, progressString, verticalRenderLocation.x.toFloat(), verticalRenderLocation.y.toFloat())
        }

        g2.clip = oldClip
    }
}
