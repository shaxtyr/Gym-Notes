package com.example.gymnotes.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.gymnotes.R
import com.example.gymnotes.util.FabItem

object ButtonsLib {
    @Composable
    fun Button(onClick: () -> Unit, @StringRes stringId: Int) {
        Button(
            onClick = onClick,
            /*colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )*/
        ) {
            Text(
                text = stringResource(stringId),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    @Composable
    fun FilledTonalButton(onClick: () -> Unit, @StringRes stringId: Int) {
        FilledTonalButton(
            onClick = onClick,
            /*colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )*/
        ) {
            Text(
                text = stringResource(stringId),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    @Composable
    fun TextButton(onClick: () -> Unit, @StringRes stringId: Int) {
        TextButton(
            onClick = onClick,
            /*colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )*/
        ) {
            Text(
                text = stringResource(stringId),
                style = MaterialTheme.typography.labelLarge)
        }
    }

    @Composable
    fun IconButton(
        onClick: () -> Unit,
        @DrawableRes iconId: Int,
        @StringRes stringId: Int,
        modifier: Modifier = Modifier
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = stringResource(stringId)
            )
        }
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun FabMenu(
        @DrawableRes iconIdAdd: Int,
        @DrawableRes iconIdClose: Int,
        onCategorySelected: (String) -> Unit,
        fabItems: List<FabItem> = emptyList()
    ) {
        var expanded by remember {
            mutableStateOf(false)
        }
        FloatingActionButtonMenu(
            expanded = expanded,
            button = {
                ToggleFloatingActionButton(
                    checked = expanded,
                    onCheckedChange = { expanded = it }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            if (expanded) iconIdClose else iconIdAdd
                        ),
                        contentDescription = null,
                        tint = if (expanded) Color.White else MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            fabItems.forEach { item ->
                FloatingActionButtonMenuItem(
                    onClick = {
                        onCategorySelected(item.text)
                        expanded = false
                              },
                    text = { Text(item.text) },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(item.iconRes),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun SwipeActionButton(
        onClick: () -> Unit,
        @DrawableRes iconId: Int,
        contentDescription: String,
        modifier: Modifier = Modifier,
        backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
        tint: Color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            modifier = modifier
                .background(backgroundColor, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    @Composable
    fun QuantityButton(
        onClick: () -> Unit,
        @DrawableRes iconId: Int,
        contentDescription: String,
        enabled: Boolean,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier
                .size(40.dp)
                .background(
                    color = if (enabled) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    },
                    shape = CircleShape
                )
                .clickable(enabled = enabled) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = contentDescription,
                modifier = Modifier.size(24.dp),
                tint = if (enabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

