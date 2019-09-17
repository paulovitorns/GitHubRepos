package br.com.githubrepos.library.di.modules

import android.app.Application
import android.content.Context
import br.com.githubrepos.domain.search.di.GitHubRepositoriesSearchModule
import dagger.Binds
import dagger.Module

@Module(includes = [GitHubRepositoriesSearchModule::class])
abstract class GitHubReposModule {

    @Binds
    abstract fun bindContext(application: Application): Context
}
