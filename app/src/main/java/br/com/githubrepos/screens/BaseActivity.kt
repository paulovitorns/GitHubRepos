package br.com.githubrepos.screens

import android.os.Bundle
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

interface BaseUi

abstract class BaseActivity<T : BasePresenter<BaseUi>> : DaggerAppCompatActivity(), BaseUi {

    @Inject
    lateinit var presenter: T
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
        setupViews()
        presenter.setUi(this)
        presenter.onCreate()
    }

    open fun setupViews() {}

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        presenter.setUi(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
