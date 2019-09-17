package br.com.githubrepos.library.reactivex

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun workerThread(): Scheduler
    fun postWorkerThread(): Scheduler
}
