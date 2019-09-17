package br.com.githubrepos.library.reactivex

import io.reactivex.Single
import io.reactivex.SingleTransformer

/**
 * Applies the schedulers to a single source without change the upstream Single.
 * @param schedulerProvider the SchedulerProvider instance that contains the workerThread and the postWorkerThread
 * @return the transformed SingleSource instance
 */
fun <T> applySingleSchedulers(schedulerProvider: SchedulerProvider): SingleTransformer<T, T> {
    return SingleTransformer { observer ->
        observer.flatMap { Single.just(it) }
            .subscribeOn(schedulerProvider.workerThread())
            .observeOn(schedulerProvider.postWorkerThread())
    }
}
