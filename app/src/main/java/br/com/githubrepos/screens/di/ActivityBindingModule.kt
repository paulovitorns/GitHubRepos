package br.com.githubrepos.screens.di

import br.com.githubrepos.domain.search.di.GitHubRepositoriesSearchModule
import br.com.githubrepos.library.di.ActivityScope
import br.com.githubrepos.screens.home.HomeActivity
import br.com.githubrepos.screens.splashscreen.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [GitHubRepositoriesSearchModule::class])
    abstract fun homeActivity(): HomeActivity
}
