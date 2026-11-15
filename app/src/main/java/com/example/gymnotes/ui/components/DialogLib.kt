package com.example.gymnotes.ui.components

import com.example.gymnotes.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AddGymnasticDialogForCategory(
    visible: Boolean,
    validationMessage: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    onDismission: () -> Unit,
) {
    var gymnasticName by remember { mutableStateOf("") }

    if (visible) {
        AlertDialog(
            modifier = Modifier.width(width = 312.dp),
            onDismissRequest = onDismissRequest,
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.dialog_adding_gymnastic))
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextFieldWithFocus(
                        value = gymnasticName,
                        onValueChange = { gymnasticName = it },
                        labelTextId = R.string.dialog_name_gymnastic,
                        placeHolderTextId = R.string.dialog_enter_gymnastic,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = validationMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (validationMessage.isNotBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.background
                    )
                    Spacer(Modifier.height(8.dp))
                }
            },
            confirmButton = {
                ButtonsLib.TextButton(onClick = { onConfirmation(gymnasticName) }, R.string.create)
                gymnasticName = ""
            },
            dismissButton = {
                ButtonsLib.TextButton(onClick = onDismission, R.string.cancel)
                gymnasticName = ""
            }
        )
    }
}

@Composable
fun AddDetailsGymnasticDialog(
    visible: Boolean,
    validationMessage: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit
) {
    var reps by remember { mutableStateOf("") }
    var heft by remember { mutableStateOf("") }

    if (visible) {
        AlertDialog(
            modifier = Modifier.width(width = 312.dp),
            onDismissRequest = onDismissRequest,
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.dialog_adding_details_gymnastic))
                    Spacer(Modifier.height(16.dp))
                    Row {
                        Spacer(Modifier.width(16.dp))
                        OutlinedNumberField(
                            modifier = Modifier.fillMaxWidth(),
                            value = reps,
                            onValueChange = { reps = it },
                            labelTextId = R.string.reps
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Spacer(Modifier.width(16.dp))
                        OutlinedNumberField(
                            modifier = Modifier.fillMaxWidth(),
                            value = heft,
                            onValueChange = { heft = it },
                            labelTextId = R.string.heft
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = validationMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (validationMessage.isNotBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.background
                    )
                    Spacer(Modifier.height(8.dp))
                }
            },
            confirmButton = {
                ButtonsLib.TextButton(onClick = { onConfirmation(reps, heft) }, R.string.create)
            },
            dismissButton = {
                ButtonsLib.TextButton(onClick = onDismissRequest, R.string.cancel)
            }
        )
    }
    heft = ""
    reps = ""
}
@Composable
fun RenameListDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    onDismission: () -> Unit,
    name: String,
    onNameChange: (String) -> Unit
) {
    val isEmpty = name.isBlank()
    val isTooLong = name.length > Borders.GYM_NAME_MAX

    val errorMessage = when {
        isEmpty -> stringResource(R.string.gym_name_empty)
        isTooLong -> stringResource(R.string.gym_name_over)
        else -> null
    }

    if (visible) {
        AlertDialog(
            modifier = Modifier.width(width = 312.dp),
            onDismissRequest = onDismissRequest,
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.gym_new_name_empty))
                    Spacer(Modifier.height(24.dp))
                    OutlinedTextFieldWithFocus(
                        value = name,
                        onValueChange = onNameChange,
                        labelTextId = R.string.dialog_name_gymnastic,
                        placeHolderTextId = R.string.dialog_enter_gymnastic,
                    )
                    Spacer(Modifier.height(8.dp))
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
            },
            confirmButton = {
                ButtonsLib.TextButton(onClick = { onConfirmation(name) }, R.string.rename)
            },
            dismissButton = {
                ButtonsLib.TextButton(onClick = onDismission, R.string.cancel)
            }
        )
    }
}
@Composable
fun RemoveDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    onDismission: () -> Unit,
    title: String
) {
    if (visible) {
        AlertDialog(
            modifier = Modifier.width(width = 312.dp),
            onDismissRequest = onDismissRequest,
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = title,
                        textAlign = TextAlign.Center)
                }
            },
            confirmButton = {
                ButtonsLib.Button(onClick = { onConfirmation() }, R.string.yes)
            },
            dismissButton = {
                ButtonsLib.FilledTonalButton(onClick = onDismission, R.string.cancel)
            }
        )
    }
}

object Borders {
    const val GYM_NAME_MAX = 100
}