package com.example.gymnotes.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OutlinedTextFieldWithFocus(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelTextId: Int? = null,
    @StringRes placeHolderTextId: Int,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        focusRequester.requestFocus()
    }
    val focusColor = MaterialTheme.colorScheme.tertiaryContainer

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            labelTextId?.let {
                Text(
                    stringResource(it),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        singleLine = true,
        placeholder = {
            if (value.isEmpty()) {
                Text(
                    stringResource(placeHolderTextId),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .height(64.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusColor,
            focusedLabelColor = focusColor,
        )
    )
}

@Composable
fun OutlinedNumberField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelTextId: Int? = null,
) {
    LaunchedEffect(Unit) {
        delay(100.milliseconds)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isEmpty = value.isEmpty()

    val focusColor = MaterialTheme.colorScheme.tertiaryContainer

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { newText -> onValueChange(newText.filter { it.isDigit() }) },
        singleLine = true,
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            if (isFocused || !isEmpty) {
                Text(
                    stringResource(labelTextId!!),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    stringResource(labelTextId!!),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusColor,
            focusedLabelColor = focusColor
        ),
        interactionSource = interactionSource
    )
}
