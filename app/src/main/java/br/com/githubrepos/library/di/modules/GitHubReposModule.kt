package br.com.githubrepos.library.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class GitHubReposModule {

    @Binds
    abstract fun bindContext(application: Application): Context
}
