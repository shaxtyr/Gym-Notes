package com.example.gymnotes.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.example.gymnotes.util.FabItem

object ButtonsLib {
    @Composable
    fun Button(onClick: () -> Unit, @StringRes stringId: Int) {
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.small
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
        ) {
            Text(
                text = stringResource(stringId),
                style = MaterialTheme.typography.labelLarge)
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
}

