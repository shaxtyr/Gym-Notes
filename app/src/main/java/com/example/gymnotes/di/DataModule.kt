package com.example.gymnotes.di

import androidx.room.Room
import com.example.gymnotes.data.db.AppDatabase
import com.example.gymnotes.data.db.dao.GymnasticDao
import com.example.gymnotes.data.db.dao.NoteDao
import com.example.gymnotes.data.mapper.GymnasticDbConverter
import com.example.gymnotes.data.mapper.NoteDbConverter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "gym_database"
        ).build()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "gym_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<NoteDao> { get<AppDatabase>().noteDao() }
    single< GymnasticDao> { get<AppDatabase>().gymnasticDao() }

    single<NoteDbConverter> { NoteDbConverter() }
    single<GymnasticDbConverter> { GymnasticDbConverter() }

}