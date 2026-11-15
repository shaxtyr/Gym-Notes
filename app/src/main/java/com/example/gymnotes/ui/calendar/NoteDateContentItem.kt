package com.example.gymnotes.ui.calendar

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NoteDateContentItem(
    dayNote: String,
    dayOfWeekNote: String,
    categories: List<String>,
    onItemClick: (String) -> Unit,
    onItemLongClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .padding(4.dp)
            .combinedClickable(
                onClick = { onItemClick(dayNote) },
                onLongClick = { onItemLongClick(dayNote) }
            ),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            // Оставил на случай отображения в календаре категорий выполненных упражнений

            /*Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    categories.forEach { letter ->
                        Text(
                            text = letter,
                            style = MaterialTheme.typography.labelSmall,
                            //color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }*/
            Text(
                text = dayNote,
                style = MaterialTheme.typography.titleLarge,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = dayOfWeekNote,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun NoteItemPreview() {
    //NoteDateContentItem("7")
}