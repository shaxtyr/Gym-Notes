package com.example.gymnotes.ui.components

import com.example.gymnotes.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AddGymnasticDialogForCategory(
    visible: Boolean,
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
                    Spacer(Modifier.height(24.dp))
                }
            },
            confirmButton = {
                ButtonsLib.TextButton(onClick = { onConfirmation(gymnasticName) }, R.string.create)
            },
            dismissButton = {
                ButtonsLib.TextButton(onClick = onDismission, R.string.cancel)
            }
        )
    }
}

@Composable
fun AddDetailsGymnasticDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String) -> Unit,
    onDismission: () -> Unit,
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
                    Spacer(Modifier.height(24.dp))
                    Row {
                        Text(text = stringResource(R.string.reps))
                        Spacer(Modifier.width(16.dp))
                        OutlinedNumberField(
                            modifier = Modifier.fillMaxWidth(),
                            value = reps,
                            onValueChange = { reps = it },
                            labelTextId = R.string.reps
                        )
                    }
                    Row {
                        Text(text = stringResource(R.string.heft))
                        Spacer(Modifier.width(16.dp))
                        OutlinedNumberField(
                            modifier = Modifier.fillMaxWidth(),
                            value = heft,
                            onValueChange = { heft = it },
                            labelTextId = R.string.heft
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                }
            },
            confirmButton = {
                ButtonsLib.TextButton(onClick = { onConfirmation(reps, heft) }, R.string.create)
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
                ButtonsLib.Button(onClick = onConfirmation, R.string.remove)
            },
            dismissButton = {
                ButtonsLib.FilledTonalButton(onClick = onDismission, R.string.cancel)
            }
        )
    }
}