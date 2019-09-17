package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.Item
import br.com.githubrepos.library.reactivex.SchedulerProvider
import br.com.githubrepos.library.reactivex.applySingleSchedulers
import io.reactivex.Single
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
    ): Single<List<Item>> {

        return searchRepository.fetchRepositories(
            query = query,
            sort = sort,
            order = order,
            page = page,
            perPage = 10
        ).map {
            it.items
        }.compose(applySingleSchedulers(schedulerProvider))
    }
}