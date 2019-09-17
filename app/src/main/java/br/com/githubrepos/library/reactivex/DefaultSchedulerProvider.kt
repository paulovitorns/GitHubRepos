package br.com.githubrepos.library.reactivex

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefaultSchedulerProvider @Inject constructor() : SchedulerProvider {
    override fun workerThread(): Scheduler = Schedulers.io()
    override fun postWorkerThread(): Scheduler = AndroidSchedulers.mainThread()
}
