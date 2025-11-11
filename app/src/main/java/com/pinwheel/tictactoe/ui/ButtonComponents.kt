package com.pinwheel.tictactoe.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinwheel.tictactoe.theme.Wp.wp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IconTextButton(
    imageVector: ImageVector?,
    buttonText: String,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = colors,
        enabled = enabled,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.wp),
            horizontalArrangement = Arrangement.spacedBy(8.wp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (imageVector != null) {
                Icon(
                    modifier = Modifier.size(20.wp),
                    imageVector = imageVector,
                    contentDescription = "$buttonText Button",
                    tint = if (enabled) colors.contentColor else colors.disabledContentColor
                )
            }
            Text(
                text = buttonText,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun IconButton(imageVector: ImageVector, contentDescription: String, onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(48.wp)
    ) {
        Icon(
            modifier = Modifier.size(24.wp),
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircularIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    active: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null
) {
    val backgroundColor =
        if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val contentColor =
        if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    val haptics = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(50))
            .background(backgroundColor)
            .size(56.wp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = if (onLongClick != null) { ->
                    haptics.performHapticFeedback(HapticFeedbackType.Companion.LongPress)
                    onLongClick()
                } else null
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(28.wp)
        )
    }
}

@Composable
fun IconSwitchButton(
    icon: ImageVector?,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
    checked: Boolean,
    enabled: Boolean = true,
    iconSize: Dp = 20.wp,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    onCheckedChange: (Boolean) -> Unit,
) {
    RowAppContentCard(
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.wp),
        horizontalArrangement = Arrangement.spacedBy(6.wp),
        colors = colors
    ) {
        if (icon != null) Icon(
            modifier = Modifier.size(iconSize),
            imageVector = icon,
            contentDescription = "$text Button",
            tint = LocalContentColor.current
        )

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = textStyle
        )

        Switch(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.scale(.9f),
            enabled = enabled
        )
    }
}

@Composable
fun IconRadioButton(
    icon: ImageVector?,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    RowAppContentCard(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.wp)
    ) {
        if (icon != null) Icon(
            modifier = Modifier.size(20.wp),
            imageVector = icon,
            contentDescription = "$text Button",
            tint = LocalContentColor.current
        )

        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = textStyle
        )

        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.scale(1.5f),
            enabled = enabled
        )
    }
}

@Composable
fun DebouncedIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    debounceDuration: Long = 500L,
    onClick: () -> Unit
) {
    var isClickable by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    FilledIconButton(
        onClick = {
            if (isClickable) {
                onClick()
                isClickable = false
                coroutineScope.launch {
                    delay(debounceDuration)
                    isClickable = true
                }
            }
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.size(72.wp),
        enabled = isClickable
    ) {
        Icon(
            modifier = Modifier.size(36.wp),
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun ExpandableIconRadioButton(
    onClick: () -> Unit,
    icon: ImageVector?,
    label: String,
    isSelected: Boolean,
    enabled: Boolean = true,
    verticalArrangementForMoreOptions: Arrangement.Vertical = Arrangement.Top,
    moreOptions: @Composable (ColumnScope.() -> Unit)
) {
    val density = LocalDensity.current
    var moreOptionsWidth by remember { mutableStateOf(0.dp) }
    val visibleState = remember(isSelected) { MutableTransitionState(initialState = false).apply { targetState = isSelected } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size -> moreOptionsWidth = with(density) { size.width.toDp() * 0.75f } },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconRadioButton(
            icon = icon,
            text = label,
            selected = isSelected,
            enabled = enabled,
            onClick = onClick
        )

        AnimatedVisibility(
            visibleState = visibleState,
            modifier = Modifier
                .clip(
                    MaterialTheme.shapes.extraLarge.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                )
                .width(moreOptionsWidth)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = verticalArrangementForMoreOptions,
                content = moreOptions
            )
        }
    }
}