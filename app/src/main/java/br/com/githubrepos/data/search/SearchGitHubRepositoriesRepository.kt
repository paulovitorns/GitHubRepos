package br.com.githubrepos.data.search

import br.com.githubrepos.data.model.SearchRepositoriesResult
import br.com.githubrepos.domain.search.SearchRepository
import br.com.githubrepos.library.retrofit.endpoint.SearchRepositoriesEndpoint
import io.reactivex.Single
import javax.inject.Inject

class SearchGitHubRepositoriesRepository @Inject constructor(
    private val searchRepositoriesEndpoint: SearchRepositoriesEndpoint
) : SearchRepository {

    override fun fetchRepositories(
        query: String,
        sort: String,
        order: String,
        page: Int,
        perPage: Int
    ): Single<SearchRepositoriesResult> {
        return searchRepositoriesEndpoint
            .searchRepositories(query, sort, order, page, perPage).map { response ->
                when (response.code()) {
                    200 -> {
                        val responseBody = response.body()
                        responseBody ?: throw ResultNotFoundException(query)
                    }
                    else -> throw IllegalArgumentException(response.errorBody().toString())
                }
            }
    }
}
