package com.example.gymnotes.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoteDateHeaderItem(date: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = date,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
        )
        SimpleLine()
    }
}
@Composable
fun SimpleLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)           // Толщина линии
            .background(Color.Gray) // Цвет
    )
}

@Preview
@Composable
fun NoteDateHeaderItemPreview() {
    NoteDateHeaderItem(date = "11")
}