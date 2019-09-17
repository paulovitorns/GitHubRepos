package br.com.githubrepos.screens.splashscreen

import br.com.githubrepos.library.di.ActivityScope
import br.com.githubrepos.screens.BasePresenter
import br.com.githubrepos.screens.BaseUi
import javax.inject.Inject

@ActivityScope
class SplashPresenter @Inject constructor() : BasePresenter<BaseUi>() {

    override fun onCreate() {
        super.onCreate()
        // TODO:: Redirect to home screen
    }
}
