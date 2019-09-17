package br.com.githubrepos.domain.search

import br.com.githubrepos.data.model.SearchRepositoriesResult
import io.reactivex.Single

interface SearchRepository {
    fun fetchRepositories(
        query: String,
        sort: String,
        order: String,
        page: Int,
        perPage: Int
    ): Single<SearchRepositoriesResult>
}
