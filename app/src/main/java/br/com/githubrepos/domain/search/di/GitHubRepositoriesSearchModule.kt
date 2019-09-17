package br.com.githubrepos.domain.search.di

import br.com.githubrepos.data.search.SearchGitHubRepositoriesRepository
import br.com.githubrepos.domain.search.SearchRepository
import br.com.githubrepos.library.di.ActivityScope
import br.com.githubrepos.library.retrofit.di.SearchRepositoriesEndpointModule
import dagger.Binds
import dagger.Module

@Module(includes = [SearchRepositoriesEndpointModule::class])
abstract class GitHubRepositoriesSearchModule {

    @ActivityScope
    @Binds
    abstract fun bindGitHubSearchRepositories(
        searchGitHubRepositoriesRepository: SearchGitHubRepositoriesRepository
    ): SearchRepository
}
