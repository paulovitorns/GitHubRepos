package br.com.githubrepos.screens.splash

import android.content.Intent
import br.com.githubrepos.screens.BaseActivity
import br.com.githubrepos.screens.BaseUi
import br.com.githubrepos.screens.home.HomeActivity

interface SplashUi : BaseUi {
    fun openHomeScreen()
}

class SplashActivity : BaseActivity<SplashPresenter>(), SplashUi {
    override val layoutRes: Int? = null

    override fun openHomeScreen() {
        Intent(this, HomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
