package com.acostadev.acostademo.di

import android.content.Context
import com.acostadev.acostademo.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder
        fun build(): ApplicationComponent
    }

    fun mainViewModelFactory(): ViewModelFactory<MainViewModel>
}