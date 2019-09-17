package br.com.githubrepos.screens

import br.com.githubrepos.library.reactivex.DisposeBag
import javax.inject.Inject

abstract class BasePresenter<T : BaseUi> {

    private var ui: T? = null

    @Inject
    lateinit var disposeBag: DisposeBag

    fun setUi(ui: T?) {
        this.ui = ui
    }

    @Suppress("UNCHECKED_CAST")
    fun <I : BaseUi> baseUi(): I? = ui as I?

    open fun onCreate() {}

    open fun onDestroy() {
        disposeBag.dispose()
        ui = null
    }

    open fun onSaveState() {}
}
