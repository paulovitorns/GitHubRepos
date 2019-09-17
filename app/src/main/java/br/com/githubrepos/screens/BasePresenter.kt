package br.com.githubrepos.screens

abstract class BasePresenter<T : BaseUi> {

    private var ui: T? = null

    open fun onCreate() {}

    open fun onStart() {}

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {}

    open fun onDestroy() {
        ui = null
    }

    fun setUi(ui: T?) {
        this.ui = ui
    }

    @Suppress("UNCHECKED_CAST")
    fun <I : BaseUi> baseUi(): I? = ui as I?
}
