package br.com.githubrepos.screens.home

import br.com.githubrepos.R
import br.com.githubrepos.screens.BaseActivity
import br.com.githubrepos.screens.BaseUi

interface HomeUi : BaseUi

class HomeActivity : BaseActivity<HomePresenter>(), HomeUi {

    override val layoutRes: Int? = R.layout.activity_home
}
