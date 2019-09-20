package br.com.githubrepos.screens.splash

import br.com.githubrepos.library.di.ActivityScope
import br.com.githubrepos.library.reactivex.SchedulerProvider
import br.com.githubrepos.library.reactivex.withDelay
import br.com.githubrepos.screens.BasePresenter
import br.com.githubrepos.screens.BaseUi
import javax.inject.Inject

@ActivityScope
class SplashPresenter @Inject constructor(
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<BaseUi>() {

    private val splashUi: SplashUi? get() = baseUi()

    override fun onCreate() {
        super.onCreate()
        withDelay(300, schedulerProvider.postWorkerThread()) {
            splashUi?.openHomeScreen()
        }
    }
}
