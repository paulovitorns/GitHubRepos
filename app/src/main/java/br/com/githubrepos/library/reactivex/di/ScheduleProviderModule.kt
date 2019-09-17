package br.com.githubrepos.library.reactivex.di

import br.com.githubrepos.library.reactivex.DefaultSchedulerProvider
import br.com.githubrepos.library.reactivex.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ScheduleProviderModule {

    @Binds
    abstract fun bindDefaultScheduleProvider(
        defaultSchedulerProvider: DefaultSchedulerProvider
    ): SchedulerProvider
}
