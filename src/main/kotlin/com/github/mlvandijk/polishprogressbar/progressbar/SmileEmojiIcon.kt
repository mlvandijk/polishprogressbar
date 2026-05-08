package com.github.mlvandijk.polishprogressbar.progressbar

import com.intellij.ui.JBColor
import java.awt.*
import javax.swing.Icon

class SmileEmojiIcon(private val size: Int) : Icon {

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        val g2 = (g.create() as? Graphics2D) ?: return

        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.color = JBColor.YELLOW
            g2.fillOval(x, y, size, size)

            g2.color = JBColor.BLACK
            g2.drawOval(x, y, size, size)

            val eyeSize = size / 5
            val eyeY = y + size / 3
            g2.fillOval(x + size / 3 - eyeSize / 2, eyeY, eyeSize, eyeSize)
            g2.fillOval(x + 2 * size / 3 - eyeSize / 2, eyeY, eyeSize, eyeSize)

            val mouthY = y + 2 * size / 3
            g2.drawLine(x + size / 3, mouthY, x + 2 * size / 3, mouthY)
        } finally {
            g2.dispose()
        }
    }

    override fun getIconWidth(): Int = size

    override fun getIconHeight(): Int = size
}
