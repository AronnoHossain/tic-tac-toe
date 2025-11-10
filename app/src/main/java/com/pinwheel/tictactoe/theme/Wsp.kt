package com.pinwheel.tictactoe.theme

import android.content.res.Resources
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * This class is intended to map a "watch pixel" to a "Scalable Pixel" (sp).
 *
 * This is done because watches have to scale down the density so that way text is not normal size as normal size text does not fit on the screen.
 *
 * As such, when displaying on the watch we have to represent it as 2 time larger as the scaling ratio makes everything scale down 1/2 size.
 */

object Wsp {
    // The initial watch we are targeting it suppose to be at xhdp (320) but this causes the text to be really large.
    // The manufacturer has then reduced the density (without changing the number of phsyical pixels) which makes things look smaller.
    // This class tries to help with rescaling the content with the new density so we get the proper layout
    val scalingMultiplier: Float
        get() {
            val dpi = Resources.getSystem().displayMetrics.densityDpi
            return 320f / dpi
        }

    val Int.wsp: TextUnit get() = (this * scalingMultiplier).sp

    val Float.wsp: TextUnit get() = (this * scalingMultiplier).sp
}
