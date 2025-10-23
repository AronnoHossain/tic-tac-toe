package com.pinwheel.tictactoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pinwheel.tictactoe.Wp.wp

/**
 * A composable card that arranges its content in a horizontal row.
 *
 * @param onClick Optional click handler for the card. If null, the card is non-clickable.
 * @param enabled Controls whether the card is interactive when `onClick` is provided.
 * @param modifier Modifier to customize the appearance and layout of the card.
 * @param horizontalArrangement Defines the spacing between row elements.
 * @param colors Defines the card's background and content colors.
 * @param content The composable content to be displayed inside the row.
 */
@Composable
fun RowAppContentCard(
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.wp),
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    content: @Composable (RowScope.() -> Unit)
) {
    val cardContent: @Composable ColumnScope.() -> Unit = {
        Row(
            modifier = Modifier.then(modifier),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.Companion.CenterVertically,
            content = content
        )
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            enabled = enabled,
            colors = colors,
            shape = MaterialTheme.shapes.extraLarge,
            content = cardContent
        )
    } else {
        Card(
            colors = colors,
            shape = MaterialTheme.shapes.extraLarge,
            content = cardContent
        )
    }
}

@Composable
fun ColumnAppContentCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.wp),
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    content: @Composable (ColumnScope.() -> Unit)
) {
    val cardContent: @Composable ColumnScope.() -> Unit = {
        Column(
            modifier = Modifier.then(modifier),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            colors = colors,
            shape = MaterialTheme.shapes.extraLarge,
            content = cardContent
        )
    } else {
        Card(
            colors = colors,
            shape = MaterialTheme.shapes.extraLarge,
            content = cardContent
        )
    }
}