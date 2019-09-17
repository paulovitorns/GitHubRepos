package br.com.githubrepos.library.di

import android.app.Application
import br.com.githubrepos.GitHubReposApplication
import br.com.githubrepos.library.di.modules.ActivityBindingModule
import br.com.githubrepos.library.di.modules.GitHubReposModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        GitHubReposModule::class,
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface GitHubReposComponent : AndroidInjector<GitHubReposApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): GitHubReposComponent
    }
}