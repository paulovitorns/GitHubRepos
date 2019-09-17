package br.com.githubrepos.library.reactivex

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

inline fun withDelay(
    millis: Long,
    scheduler: Scheduler = Schedulers.computation(),
    crossinline doAfter: (() -> Unit)
): Disposable {
    return Completable.timer(millis, TimeUnit.MILLISECONDS, scheduler)
        .subscribe({
            doAfter()
        }, { })
}
