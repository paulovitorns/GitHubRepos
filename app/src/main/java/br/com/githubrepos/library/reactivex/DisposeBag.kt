package br.com.githubrepos.library.reactivex

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DisposeBag @Inject constructor() {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    fun dispose() {
        subscriptions.clear()
    }

    fun add(disposable: Disposable) {
        subscriptions.add(disposable)
    }
}

fun Disposable.addDisposableTo(bag: DisposeBag): Disposable = apply { bag.add(this) }
