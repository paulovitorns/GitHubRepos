package br.com.githubrepos.library.di.modules

import br.com.githubrepos.library.di.ActivityScope
import br.com.githubrepos.screens.splashscreen.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity
}
