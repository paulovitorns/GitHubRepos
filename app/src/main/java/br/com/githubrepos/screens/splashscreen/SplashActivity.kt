package br.com.githubrepos.screens.splashscreen

import br.com.githubrepos.screens.BaseActivity
import br.com.githubrepos.screens.BaseUi

interface SplashUi : BaseUi

class SplashActivity : BaseActivity<SplashPresenter>(), SplashUi {

    override val layoutRes: Int? = null
}
