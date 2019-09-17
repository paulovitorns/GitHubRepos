package br.com.githubrepos.library.reactivex

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TestSchedulerProvider @Inject constructor() : SchedulerProvider {

    override fun workerThread(): Scheduler = Schedulers.trampoline()

    override fun postWorkerThread(): Scheduler = Schedulers.trampoline()
}
