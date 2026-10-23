package com.example.gymnotes.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gymnotes.R
import com.example.gymnotes.domain.model.NoteItem
@Composable
fun ShowNotes(notesItem: List<NoteItem>) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(notesItem) { noteItem ->
            when(noteItem) {
                is NoteItem.HeaderNoteItem -> NoteItemHeader(noteItem.gymName, noteItem.category)
                is NoteItem.ContentNoteItem -> NoteRowCard(noteItem.sets, noteItem.reps, noteItem.heft)
            }
        }
    }
}

@Composable
fun NoteItemHeader(
    gymName: String,
    category: String
) {
    val icon = when(category) {
        stringResource(R.string.hands) -> R.drawable.ic_hand_48_px
        stringResource(R.string.chest) -> R.drawable.ic_chest_48_px
        stringResource(R.string.back) -> R.drawable.ic_back_48_px
        else -> R.drawable.ic_leg_48_px
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.small,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = gymName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HorizontalDivider(
                modifier = Modifier.height(1.dp),
                thickness = DividerDefaults.Thickness,
            )
            Spacer(modifier = Modifier.height(4.dp))
            MakeTitleRow()
        }
    }
}
@Composable
fun MakeTitleRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.sets),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.reps),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.heft),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
@Composable
fun NoteRowCard(
    sets: String,
    reps: String,
    heft: String
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = sets,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = reps,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = heft,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}