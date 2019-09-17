package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.Repository
import br.com.githubrepos.library.reactivex.SchedulerProvider
import br.com.githubrepos.library.reactivex.applyObservableSchedulers
import io.reactivex.Observable
import javax.inject.Inject

class GetPaginatedGitHubRepositories @Inject constructor(
    private val searchRepository: SearchRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(
        query: String,
        sort: String,
        order: String,
        page: Int
    ): Observable<List<Repository>> {

        return searchRepository.fetchRepositories(
            query = query.compoundQueryString(),
            sort = sort,
            order = order,
            page = page,
            perPage = 10
        ).map {
            it.repositories
        }.compose(applyObservableSchedulers(schedulerProvider))
    }
}
