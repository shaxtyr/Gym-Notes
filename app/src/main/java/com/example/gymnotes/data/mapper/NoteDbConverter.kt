package com.example.gymnotes.data.mapper

import com.example.gymnotes.data.db.entity.NoteEntity
import com.example.gymnotes.domain.model.Note
import com.example.gymnotes.util.DateUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteDbConverter {
    fun fromModelToEntity(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id,
            sets = note.sets,
            reps = note.reps,
            heft = note.heft,
            gymnasticName = note.gymnasticName,
            category = note.category,
            addedAt = note.addedAt
        )
    }
    fun fromEntityToModel(noteEntity: NoteEntity): Note {
        //val localDateTime = LocalDateTime.parse(noteEntity.addedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        //val localDate = localDateTime.toLocalDate().toString()
        return Note(
            id = noteEntity.id,
            sets = noteEntity.sets,
            reps = noteEntity.reps,
            heft = noteEntity.heft,
            gymnasticName = noteEntity.gymnasticName,
            category = noteEntity.category,
            addedAt = noteEntity.addedAt,
            //addedAt = localDate,
            monthYear = DateUtils.getMonthYear(DateUtils.parseIsoStringToLocalDate(noteEntity.addedAt)),
            day = DateUtils.getDay(DateUtils.parseIsoStringToLocalDate(noteEntity.addedAt)),
            dayOfWeek = DateUtils.getDayOfWeek(DateUtils.parseIsoStringToLocalDate(noteEntity.addedAt))
        )
    }
}