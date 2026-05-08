package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.ui.JBColor
import java.awt.*
import javax.swing.Icon

/**
 * A simple smile emoji icon implementation.
 * This is used as a fallback if the PNG icon is not available.
 */
class SmileEmojiIcon(private val size: Int) : Icon {

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        val g2d = g as Graphics2D
        val oldHints = g2d.getRenderingHints()
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        
        // Draw face
        g2d.color = JBColor.YELLOW
        g2d.fillOval(x, y, size, size)
        
        // Draw outline
        g2d.color = JBColor.BLACK
        g2d.drawOval(x, y, size, size)
        
        // Draw eyes
        val eyeSize = size / 5
        val eyeY = y + size / 3
        
        // Left eye
        g2d.fillOval(x + size / 3 - eyeSize / 2, eyeY, eyeSize, eyeSize)
        
        // Right eye
        g2d.fillOval(x + 2 * size / 3 - eyeSize / 2, eyeY, eyeSize, eyeSize)
        
        // Draw neutral mouth
        val mouthY = y + 2 * size / 3
        g2d.drawLine(x + size / 3, mouthY, x + 2 * size / 3, mouthY)
        
        g2d.setRenderingHints(oldHints)
    }

    override fun getIconWidth(): Int = size

    override fun getIconHeight(): Int = size
}