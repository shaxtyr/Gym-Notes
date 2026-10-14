package com.example.gymnotes

import android.app.Application
import com.example.gymnotes.di.dataModule
import com.example.gymnotes.di.interactorModule
import com.example.gymnotes.di.repositoryModule
import com.example.gymnotes.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                interactorModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}