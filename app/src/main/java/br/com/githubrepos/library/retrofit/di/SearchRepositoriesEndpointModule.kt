package br.com.githubrepos.library.retrofit.di

import br.com.githubrepos.domain.config.EnvironmentConfig
import br.com.githubrepos.library.di.ActivityScope
import br.com.githubrepos.library.retrofit.RetrofitFactory
import br.com.githubrepos.library.retrofit.endpoint.SearchRepositoriesEndpoint
import dagger.Module
import dagger.Provides

@Module
class SearchRepositoriesEndpointModule {

    @ActivityScope
    @Provides
    fun providesSearchRepositoriesEndpoint(
        retrofitFactory: RetrofitFactory
    ): SearchRepositoriesEndpoint {
        val baseUrl = EnvironmentConfig().baseUrl
        return retrofitFactory.create(SearchRepositoriesEndpoint::class.java, baseUrl)
    }
}
